package test.restcontroller;

import com.strelnikov.doclib.dto.CatalogDto;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CatalogTest {

    private static Server server;

    @BeforeClass
    public static void initDb() throws Exception {
        server = new Server(8080);
        WebAppContext wcon = new WebAppContext();
        wcon.setContextPath("/");
        wcon.setDescriptor("src/test/webapp/web.xml");
        wcon.setResourceBase("src/test/webapp");
        wcon.setConfigurationDiscovered(true);
        server.setHandler(wcon);
        server.start();
    }

    @AfterClass
    public static void deleteTestData() throws Exception {
        server.stop();
    }

    @Test
    public void catalogGetTest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("root", "root");
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<CatalogDto> responseEntity = restTemplate.exchange(
                "http://localhost:8080/rest/catalog/get/1",
                HttpMethod.GET,
                httpEntity,
                CatalogDto.class);
        CatalogDto catalogDto = responseEntity.getBody();
        Assert.assertEquals("/", catalogDto.getName());
    }

    @Test
    public void createNewCatalogTest() throws IOException {
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/CatalogTests/testCatalog.json"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("root", "root");
        headers.add("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CatalogDto> responseEntity = restTemplate.exchange(
                "http://localhost:8080/rest/catalog/post",
                HttpMethod.POST,
                httpEntity,
                CatalogDto.class);
        CatalogDto catalogDto = responseEntity.getBody();
        httpEntity = new HttpEntity(headers);
        responseEntity = restTemplate.exchange(
                "http://localhost:8080/rest/catalog/delete/" + catalogDto.getId(),
                HttpMethod.DELETE,
                httpEntity,
                CatalogDto.class);
        Assert.assertEquals("test_catalog1", catalogDto.getName());
    }

    @Test
    public void updateCatalogTest() throws IOException {
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/CatalogTests/testCatalog.json"));
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("root", "root");
        headers.add("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CatalogDto> responseEntity = restTemplate.exchange(
                "http://localhost:8080/rest/catalog/post",
                HttpMethod.POST,
                httpEntity,
                CatalogDto.class);
        CatalogDto catalogDto = responseEntity.getBody();
        CatalogDto changedCat = new CatalogDto(catalogDto.getId(), "changed_name", 1, catalogDto.getContentList());
        httpEntity = new HttpEntity(changedCat, headers);
        responseEntity = restTemplate.exchange(
                "http://localhost:8080/rest/catalog/post",
                HttpMethod.POST,
                httpEntity,
                CatalogDto.class);
        catalogDto = responseEntity.getBody();
        httpEntity = new HttpEntity(headers);
        responseEntity = restTemplate.exchange(
                "http://localhost:8080/rest/catalog/delete/" + catalogDto.getId(),
                HttpMethod.DELETE,
                httpEntity,
                CatalogDto.class);
        Assert.assertEquals("changed_name", catalogDto.getName());
    }
}