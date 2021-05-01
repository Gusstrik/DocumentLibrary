package com.strelnikov.doclib.web.servlets;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.service.DocumentTypeActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.dtomapper.impl.DtoMapperImpl;
import com.strelnikov.doclib.service.impl.DocumentTypeImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DocumentTypeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DtoMapper dtoMapper = new DtoMapperImpl();
        DocTypeDto typeDto = dtoMapper.mapDocType();
        ServletUtils.writeDocTypesJson(response,typeDto);

    }
}
