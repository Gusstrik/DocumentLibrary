package com.strelnikov.doclib.web.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.dto.DocumentDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServletUtils {

    public static String convertToJson(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(o);
    }

    public static Object convertToDtoCatalog(String str) throws JsonProcessingException {
        CatalogDto catalogDto = new CatalogDto();
        ObjectMapper objectMapper = new ObjectMapper();
        return  objectMapper.readValue(str,catalogDto.getClass());
    }

    public static Object convertToDtoDocument(String str) throws JsonProcessingException {
        DocumentDto documentDto = new DocumentDto();
        ObjectMapper objectMapper = new ObjectMapper();
        return  objectMapper.readValue(str,documentDto.getClass());
    }

}
