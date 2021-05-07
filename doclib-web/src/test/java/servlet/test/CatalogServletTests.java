package servlet.test;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import com.strelnikov.doclib.web.servlets.utils.ServletUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CatalogServletTests {
    private static Server server;

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);

    @BeforeClass
    public static void beforeServletTests()throws Exception{
        creator.runScript("src/test/resources/dbscripts/insertestdb.sql");
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
        creator.runScript("src/test/resources/dbscripts/deletedb.sql");
        server.stop();
    }

    @After
    public void afterEachTest() throws IOException {
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
    public void catalogGetTest() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/catalog?id=1").openConnection();
        httpURLConnection.connect();
        String response=new String(httpURLConnection.getInputStream().readAllBytes());
        CatalogDto catalogDto = (CatalogDto) ServletUtils.convertToDtoCatalog(response);
        httpURLConnection.disconnect();
        Assert.assertEquals("/",catalogDto.getName());
    }

    @Test
    public void addCatalogTest() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/catalog").openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setDoOutput(true);
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/CatalogTests/addCatalog.json"));
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(requestBody);
        outputStream.flush();
        httpURLConnection.getResponseCode();
        httpURLConnection.disconnect();
        httpURLConnection.connect();
        httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/catalog?id=1").openConnection();
        httpURLConnection.connect();
        String response=new String(httpURLConnection.getInputStream().readAllBytes());
        CatalogDto catalogDto = (CatalogDto) ServletUtils.convertToDtoCatalog(response);
        httpURLConnection.disconnect();
        Assert.assertEquals(3,catalogDto.getContentList().size());
    }

}
