package com.strelnikov.doclib.web.servlets;

import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import com.strelnikov.doclib.web.servlets.utils.ApplicationContextHolder;
import com.strelnikov.doclib.web.servlets.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


@Slf4j
@WebServlet
public class DocumentServlet extends HttpServlet {

    private DtoMapper dtoMapper=null;
    private DocumentActions documentActions;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dtoMapper = ApplicationContextHolder.getApplicationContext().getBean(DtoMapper.class);
        documentActions = ApplicationContextHolder.getApplicationContext().getBean(DocumentActions.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("id");
        if (param!=null){
            try {
                DocumentDto documentDto = documentActions.loadDocument(Integer.parseInt(param));
                response.setContentType("application/json");
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(ServletUtils.convertToJson(documentDto).getBytes());
                outputStream.flush();
            } catch (UnitNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = new String(request.getInputStream().readAllBytes());
        DocumentDto documentDto = null;
        documentDto = (DocumentDto) ServletUtils.convertToDtoDocument(requestBody);
        try {
            documentDto = documentActions.saveDocument(documentDto);
        } catch (UnitIsAlreadyExistException | VersionIsAlreadyExistException e) {
            e.printStackTrace();
        }
    }
}
