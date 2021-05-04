import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.dto.UnitDto;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import com.strelnikov.doclib.web.servlets.utils.ServletUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentServletTests {
    private static Server server;

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);

    @BeforeClass
    public static void beforeServletTests()throws Exception{
        creator.runScript("src/test/resources/insertestdb.sql");
        server = new Server(12135);
        WebAppContext wcon = new WebAppContext();
        wcon.setContextPath("/doclib-app");
        wcon.setDescriptor("src/test/webapp/web.xml");
        wcon.setResourceBase("src/test/webapp");
        wcon.setConfigurationDiscovered(true);
        server.setHandler(wcon);
        server.start();
    }
    @AfterClass
    public static void afterServletTests()throws Exception{
        creator.runScript("src/test/resources/deletedb.sql");
        server.stop();
    }

    @After
    public void initialValueOfTestDoc() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document").openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/DocumentTests/initialValueTestDoc.json"));
        httpURLConnection.getOutputStream().write(requestBody);
        httpURLConnection.getOutputStream().flush();
        httpURLConnection.getResponseCode();
        httpURLConnection.disconnect();
    }
    @After
    public void initialValueOfMainCat() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/catalog?id=1").openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/CatalogTests/clearMainCat.json"));
        httpURLConnection.getOutputStream().write(requestBody);
        httpURLConnection.getOutputStream().flush();
        httpURLConnection.getResponseCode();
        httpURLConnection.disconnect();
    }

    @Test
    public void documentGetTest() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document?id=1").openConnection();
        String response=new String(httpURLConnection.getInputStream().readAllBytes());
        DocumentDto documentDto = (DocumentDto) ServletUtils.convertToDtoDocument(response);
        httpURLConnection.disconnect();
        Assert.assertEquals("test_doc",documentDto.getName());
    }

    @Test
    public void documentAddVersionTest() throws IOException{
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document").openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/DocumentTests/addDocVersionTest.json"));
        httpURLConnection.getOutputStream().write(requestBody);
        httpURLConnection.getOutputStream().flush();
        httpURLConnection.getResponseCode();
        httpURLConnection.disconnect();
        httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document?id=1").openConnection();
        String response=new String(httpURLConnection.getInputStream().readAllBytes());
        DocumentDto documentDto = (DocumentDto) ServletUtils.convertToDtoDocument(response);
        httpURLConnection.disconnect();
        Assert.assertEquals(1,documentDto.getVersionList().get(0).getVersion());
    }

    @Test
    public void documentAddTest() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document").openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/DocumentTests/addDocumentTest.json"));
        httpURLConnection.getOutputStream().write(requestBody);
        httpURLConnection.getOutputStream().flush();
        httpURLConnection.getResponseCode();
        httpURLConnection.disconnect();
        httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/catalog?id=1").openConnection();
        String response=new String(httpURLConnection.getInputStream().readAllBytes());
        CatalogDto catalogDto = (CatalogDto) ServletUtils.convertToDtoCatalog(response);
        int docId=0;
        for (UnitDto unitDto:catalogDto.getContentList()){
            if(unitDto.getName().equals("test_doc2")){
                docId=unitDto.getId();
            }
        }
        httpURLConnection.disconnect();
        httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document?id="+docId).openConnection();
        response=new String(httpURLConnection.getInputStream().readAllBytes());
        DocumentDto documentDto = (DocumentDto) ServletUtils.convertToDtoDocument(response);
        httpURLConnection.disconnect();
        Assert.assertEquals("test description of new test doc",documentDto.getVersionList().get(0).getDescription());
    }
}
