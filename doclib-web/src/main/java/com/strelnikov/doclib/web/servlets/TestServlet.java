package com.strelnikov.doclib.web.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;

public class TestServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        System.out.println(config+"initialized!");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.printf(request.getServletPath());
        System.out.println(request.getServerPort());
        System.out.println(request.getRequestURI());
        System.out.println(request.getMethod());
        response.setContentType("text/html");
        PrintWriter printWriter=response.getWriter();
        printWriter.write("Test");
        printWriter.close();
    }

}
