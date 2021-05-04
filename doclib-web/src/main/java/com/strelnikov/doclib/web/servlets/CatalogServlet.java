package com.strelnikov.doclib.web.servlets;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import lombok.SneakyThrows;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class CatalogServlet extends HttpServlet {

    private DtoMapper dtoMapper = null;
    private CatalogActions catalogActions = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dtoMapper = ApplicationContextHolder.getApplicationContext().getBean(DtoMapper.class);
        catalogActions = ApplicationContextHolder.getApplicationContext().getBean(CatalogActions.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException {
        String param = request.getParameter("id");
        if (param == null) {
            param = "1";
        }
        try {
            CatalogDto catalogDto = catalogActions.loadCatalog(Integer.parseInt(param));
            String cat = ServletUtils.convertToJson(catalogDto);
            response.setContentType("application/json");
            response.getOutputStream().write(cat.getBytes());
            response.getOutputStream().flush();
        } catch (UnitNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException {
        String requestBody = new String(request.getInputStream().readAllBytes());
        CatalogDto catalogDto = null;
        catalogDto = (CatalogDto) ServletUtils.convertToDtoCatalog(requestBody);
        try {
            catalogDto = catalogActions.saveCatalog(catalogDto);
        } catch (UnitIsAlreadyExistException e) {
            e.printStackTrace();
        }
    }
}
