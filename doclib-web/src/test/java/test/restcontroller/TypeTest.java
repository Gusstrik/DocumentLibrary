package test.restcontroller;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TypeTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);

    private static Server server;

    @BeforeClass
    public static void init() throws Exception {
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
    public void typeControllerTest() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        byte[] requestBody = Files.readAllBytes(Paths.get("src/test/resources/JSON/testType.json"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.setBasicAuth("root", "root");
        HttpEntity httpEntity = new HttpEntity(requestBody,headers);
        ResponseEntity<DocTypeDto> responseEntity = restTemplate.exchange(
                "http://localhost:8080/rest/type/post",
                HttpMethod.POST,
                httpEntity,
                DocTypeDto.class);
        httpEntity = new HttpEntity(headers);
        ResponseEntity<DocTypeDto[]> response = restTemplate.exchange(
                "http://localhost:8080/rest/type/get",
                HttpMethod.GET,
                httpEntity,
                DocTypeDto[].class);
        DocTypeDto docTypeDto = null;
        DocTypeDto[] docTypeDtos = response.getBody();
        for (DocTypeDto docType:docTypeDtos){
            if (docType.getDocType().equals("testType")){
                docTypeDto= new DocTypeDto(docType.getId(),docType.getDocType());
            }
        }
        response = restTemplate.exchange(
                "http://localhost:8080/rest/type/delete/"+docTypeDto.getId(),
                HttpMethod.DELETE,
                httpEntity,
                DocTypeDto[].class);
        Assert.assertEquals("testType",docTypeDto.getDocType());
    }
}
