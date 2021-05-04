package com.strelnikov.doclib.web.servlets;

import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.dtomapper.impl.DtoMapperImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@Slf4j
public class DocumentServlet extends HttpServlet {

    private DtoMapper dtoMapper=null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dtoMapper = ApplicationContextHolder.getApplicationContext().getBean(DtoMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write("IT IS A DOCUEMNT SERVLET".getBytes());
        outputStream.flush();
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
//        String mode;
//        InputStream inputStream = null;
//        OutputStream outputStream = null;
//        String result = "";
//        String requestBody=null;
//        response.setContentType("text/html");
//        try {
//            inputStream = request.getInputStream();
//            outputStream = response.getOutputStream();
//            requestBody=ServletUtils.getRequestBody(inputStream);
//        } catch (IOException e) {
//            log.error(e.getMessage(),e);
//        }
//        mode = ServletUtils.parseDocumentAddingMode(requestBody);
//        if (mode != null) {
//            if (mode.equals("newDoc")) {
//                DocumentDto documentDto = ServletUtils.parseNewDocument(requestBody);
//                if (documentDto != null) {
//                    dtoMapper.mapNewDocument(documentDto);
//                    result = "New document was created";
//                } else {
//                    result = "Incorrect input data";
//                    response.setStatus(400);
//                }
//            }else if(mode.equals("newVer")){
//                DocumentDto documentDto = ServletUtils.parseNewDocVersion(requestBody);
//                if (documentDto != null) {
//                    dtoMapper.mapNewDocVersion(documentDto);
//                    result = "New version of document was created";
//                } else {
//                    result = "Incorrect input data";
//                    response.setStatus(400);
//                }
//            }else{
//                result="Incorrect adding mode";
//                response.setStatus(400);
//            }
//        } else {
//            response.setStatus(400);
//            result = "Please specify adding mode:\n" +
//                    "\"newDoc\" - for adding new Document\n" +
//                    "\"newVer\" - for adding new Version";
//        }
//        try {
//            outputStream.write(result.getBytes());
//            outputStream.flush();
//        } catch (IOException e) {
//            log.error(e.getMessage(),e);
//        }
//    }
}
