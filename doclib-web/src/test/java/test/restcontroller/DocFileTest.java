package test.restcontroller;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.dto.DocTypeDto;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;


public class DocFileTest {

    /*
    Can be tested only with running boot application.
    Also can't delete file, or get without creating a doc,
    because file inherit his permission from documents which
    contains file.
     */

    @Test
    public void fileTest() throws IOException {
        File file = new File("src/test/resources/testFile.jpg");
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileSystemResource);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBasicAuth("root", "root");
        HttpEntity httpEntity = new HttpEntity(body,headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DocFileDto> responseEntity = restTemplate.exchange(
                "http://localhost:8080/rest/file/post",
                HttpMethod.POST,
                httpEntity,
                DocFileDto.class);
    }

}
