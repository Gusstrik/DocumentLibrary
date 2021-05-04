package com.strelnikov.doclib.web.servlets;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocTypeActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class DocumentTypeServlet extends HttpServlet {


    private DocTypeActions docTypeActions = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        docTypeActions = ApplicationContextHolder.getApplicationContext().getBean(DocTypeActions.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        docTypeActions.refreshListDocumentType();
        String str = DocTypeDto.typesList.toString();
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(str.getBytes());
        outputStream.flush();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("type");
        if (param!=null){
            DocTypeDto docTypeDto = new DocTypeDto(param);
            if(DocTypeDto.typesList.contains(docTypeDto.getDocType())){
                docTypeActions.deleteDocumentType(docTypeDto);
            }else{
                docTypeActions.addDocumentType(docTypeDto);
            }

        }
    }
}
