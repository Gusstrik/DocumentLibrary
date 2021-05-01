package com.strelnikov.doclib.web.servlets;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.model.conception.Unit;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServletUtils {

    public static void writeCatalogJson(HttpServletResponse response, CatalogDto catalog) {
        String str="{\n";
        str+="  "+quotesWrap("name")+": " + quotesWrap(catalog.getName())+",\n";
        str+="  "+quotesWrap("contentList")+": " + "[";
        for (int i=0;i<catalog.getContentList().size();i++){
            if (i!=0){
                str+=",\n";
            }
            str+="\n   {\n";
            str+="    "+quotesWrap("name")+": "+quotesWrap(catalog.getContentList().get(i).getName());
            str+=",\n    "+quotesWrap("type")+": "+quotesWrap(catalog.getContentList().get(i).getUnitType().toString());
            str+="\n   }";
        }
        str+="\n  ]\n}";
        response.setContentType("application/json");
        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(str.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String quotesWrap(String str) {
        return "\"" + str + "\"";
    }

    public static CatalogDto parseJsonToCatalog(InputStream inputStream){
        String requestBody="";
        CatalogDto catalogDto=null;
        try {
            requestBody=new String(inputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String [] strArray = requestBody.split(",");
        String catalogName=null;
        String parentName=null;
        for (String str:strArray){
            if (str.contains("\"name\"")){
                catalogName = str.substring(str.indexOf(": \"")+3,str.lastIndexOf('"'));
            }
        }
        for (String str:strArray){
            if (str.contains("\"parent\"")){
                parentName = str.substring(str.indexOf(": \"")+3,str.lastIndexOf('"'));
            }
        }
        if (parentName==null){
            catalogDto = new CatalogDto(catalogName,"/");
        }else{
            catalogDto = new CatalogDto(catalogName,parentName);
        }
        return catalogDto;
    }

}



