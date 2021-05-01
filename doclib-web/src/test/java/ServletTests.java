
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.*;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServletTests {

    private static Server server;

    @BeforeClass
    public static void beforeServletTests()throws Exception{
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
        server.stop();
    }
    @Test
    public void connectToServerTest()throws Exception{
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/").openConnection();
        httpURLConnection.connect();
        int actual = httpURLConnection.getResponseCode();
        Assert.assertEquals(200,actual);
    }

    @Test
    public void catalogShowTest()throws Exception{
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/catalog").openConnection();
        httpURLConnection.connect();
        int actual = httpURLConnection.getResponseCode();
        Assert.assertEquals(200,actual);
    }

    @Test
    public void catalogAddTest()throws Exception{
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/catalog").openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/catalog.json"));
        httpURLConnection.getOutputStream().write(requestBody);
        httpURLConnection.connect();
        int actual = httpURLConnection.getResponseCode();
        Assert.assertEquals(200,actual);
    }

    @Test
    public void typeShowTest()throws Exception{
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/type").openConnection();
        httpURLConnection.connect();
        int actual = httpURLConnection.getResponseCode();
        Assert.assertEquals(200,actual);
    }
    @Test
    public void documentShowTest()throws Exception{
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document").openConnection();
        httpURLConnection.setRequestProperty("name","Order_1");
        httpURLConnection.setRequestProperty("version","0");
        httpURLConnection.setRequestProperty("type","Order");
        httpURLConnection.connect();
        int actual = httpURLConnection.getResponseCode();
        Assert.assertEquals(200,actual);
    }
    @Test
    public void newDocumentAddTest()throws Exception{
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document").openConnection();
        httpURLConnection.setDoOutput(true);
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/newDocument.json"));
        httpURLConnection.getOutputStream().write(requestBody);
        httpURLConnection.connect();
        int actual = httpURLConnection.getResponseCode();
        Assert.assertEquals(200,actual);
    }


    @Test
    public void newDocVersionAddTest() throws Exception{
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL("http://localhost:12135/doclib-app/document").openConnection();
        httpURLConnection.setDoOutput(true);
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/newDocVersion.json"));
        httpURLConnection.getOutputStream().write(requestBody);
        httpURLConnection.connect();
        int actual = httpURLConnection.getResponseCode();
        Assert.assertEquals(200,actual);
    }

}
