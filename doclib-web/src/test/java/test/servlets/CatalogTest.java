package test.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CatalogTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final DtoMapper dtoMapper = appContext.getBean(DtoMapper.class);

    private static Server server;

    @BeforeClass
    public static void initDb() throws Exception {
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
    public static void deleteTestData() throws Exception {
        creator.runScript("src/test/resources/dbscripts/deletedb.sql");
        server.stop();
    }

    @After
    public void initMainCat() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:12135/doclib-app/catalog").openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/CatalogTests/clearMainCat.json"));
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.getOutputStream().write(requestBody);
        connection.getOutputStream().flush();
        String responseBody = new String(connection.getInputStream().readAllBytes());
        connection.disconnect();
    }

    @Test
    public void getCatalog() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:12135/doclib-app/catalog/1").openConnection();
        String responseBody = new String(connection.getInputStream().readAllBytes());
        ObjectMapper objectMapper = new ObjectMapper();
        CatalogDto catalogDto = objectMapper.readValue(responseBody, CatalogDto.class);
        connection.disconnect();
        Assert.assertEquals("/", catalogDto.getName());
    }

    @Test
    public void createNewCatalog() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:12135/doclib-app/catalog").openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/CatalogTests/addCatalog.json"));
        connection.getOutputStream().write(requestBody);
        connection.getOutputStream().flush();
        String responseBody = new String(connection.getInputStream().readAllBytes());
        connection.disconnect();
        ObjectMapper objectMapper = new ObjectMapper();
        CatalogDto catalogDto = objectMapper.readValue(responseBody, CatalogDto.class);
        connection = (HttpURLConnection) new URL("http://localhost:12135/doclib-app/catalog/" + catalogDto.getId()).openConnection();
        connection.setRequestMethod("DELETE");
        connection.getResponseCode();
        connection.disconnect();
        Assert.assertEquals("test_catalog1",catalogDto.getName());
    }

    @Test
    public void editCatalogTest() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:12135/doclib-app/catalog").openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/CatalogTests/editCatalog.json"));
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.getOutputStream().write(requestBody);
        connection.getOutputStream().flush();
        String responseBody = new String(connection.getInputStream().readAllBytes());
        connection.disconnect();
        CatalogDto mainCatalog = objectMapper.readValue(responseBody,CatalogDto.class);
        Assert.assertEquals("changed name", mainCatalog.getName());
    }

    @Test
    public void deleteCatalogTest() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:12135/doclib-app/catalog").openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/CatalogTests/addCatalog.json"));
        connection.getOutputStream().write(requestBody);
        connection.getOutputStream().flush();
        String responseBody = new String(connection.getInputStream().readAllBytes());
        connection.disconnect();
        ObjectMapper objectMapper = new ObjectMapper();
        CatalogDto catalogDto = objectMapper.readValue(responseBody, CatalogDto.class);
        connection = (HttpURLConnection) new URL("http://localhost:12135/doclib-app/catalog/" + catalogDto.getId()).openConnection();
        connection.setRequestMethod("DELETE");
        int actual = connection.getResponseCode();
        connection.disconnect();
        Assert.assertEquals(200,actual);
    }
}
