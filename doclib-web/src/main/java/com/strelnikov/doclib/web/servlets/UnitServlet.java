package com.strelnikov.doclib.web.servlets;

import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.web.servlets.utils.ApplicationContextHolder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class UnitServlet extends HttpServlet {

    private CatalogActions catalogActions;
    private DtoMapper dtoMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dtoMapper = ApplicationContextHolder.getApplicationContext().getBean(DtoMapper.class);
        catalogActions = ApplicationContextHolder.getApplicationContext().getBean(CatalogActions.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("id");
        if (param!=null) {
            try {
                catalogActions.loadCatalog(Integer.parseInt(param));
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/catalog");
                requestDispatcher.forward(request, response);
            } catch (UnitNotFoundException e) {
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/document");
                requestDispatcher.forward(request, response);
            }
        }else {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write("Please specify unit id in URL".getBytes());
            outputStream.flush();
        }
    }


}
