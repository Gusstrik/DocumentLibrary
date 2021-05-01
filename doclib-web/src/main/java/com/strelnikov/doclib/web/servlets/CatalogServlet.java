package com.strelnikov.doclib.web.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.dtomapper.impl.DtoMapperImpl;


import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

public class CatalogServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println(config+"initialized!");
    }

    private final DtoMapper dtoMapper = new DtoMapperImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("name");
        //CatalogActions catalogActions = new CatalogImpl();
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
        if (!(catalogDto == null)){
            dtoMapper.mapCatalog(catalogDto);
        }
    }
}
