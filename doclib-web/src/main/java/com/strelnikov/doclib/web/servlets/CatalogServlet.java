package com.strelnikov.doclib.web.servlets;


import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class CatalogServlet extends HttpServlet {

    private DtoMapper dtoMapper=null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dtoMapper = ApplicationContextHolder.getApplicationContext().getBean(DtoMapper.class);
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("name");
        CatalogDto catalogDto;
        if(param==null||param.isEmpty()){
            catalogDto = dtoMapper.mapCatalog("/");

        }else{
            catalogDto = dtoMapper.mapCatalog(param);
        }
        ServletUtils.writeCatalogJson(response,catalogDto);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        CatalogDto catalogDto = ServletUtils.parseJsonToCatalog(request.getInputStream());
        response.setContentType("text/html");
        OutputStream outputStream = response.getOutputStream();
        if (!(catalogDto == null)){
            dtoMapper.mapCatalog(catalogDto);
            outputStream.write("Catalog was successfully added".getBytes());
        }else {
            outputStream.write("Catalog wasn't added. Incorrect input data".getBytes());
        }
        outputStream.flush();
    }
}
