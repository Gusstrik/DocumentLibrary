package com.strelnikov.doclib.web.servlets;

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

    public static String getRequestBody(InputStream inputStream){
        String requestBody="";
        try {
            requestBody=new String(inputStream.readAllBytes());
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return requestBody;
    }

    private static String getParameterValue(String parameterName, String[] splitedRequestBody) {
        String parameterValue = null;
        for (String str:splitedRequestBody) {
            if (str.contains(quotesWrap(parameterName))) {
                str=str.replaceAll("\\s","");
                parameterValue= str.substring(str.indexOf(":\"")+2,str.lastIndexOf('"'));
            }
        }
        return parameterValue;
    }

    private static void writeInJson(HttpServletResponse response, String value){
        response.setContentType("application/json");
        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(value.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    public static  void writeDocumentJson(HttpServletResponse response, DocumentDto document){
        String str="{\n";
        str+="  "+quotesWrap("name")+": " + quotesWrap(document.getName())+",\n";
        str+="  "+quotesWrap("version")+": " + document.getVersion()+",\n";
        str+="  "+quotesWrap("docType")+": " + quotesWrap(document.getType())+",\n";
        str+="  "+quotesWrap("fileList")+": " + "[";
        List<DocFileDto> fileList = document.getFileList();
        for (int i=0;i<document.getFileList().size();i++){
            if (i!=0){
                str+=",\n";
            }
            str+="\n   {\n";
            str+="    "+quotesWrap("fileName")+": "+quotesWrap(fileList.get(i).getName());
            str+=",\n    "+quotesWrap("filePath")+": "+quotesWrap(fileList.get(i).getPath());
            str+="\n   }";
        }
        str+="\n  ]\n}";
        writeInJson(response,str);
    }

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
        writeInJson(response,str);
    }

    public static void writeDocTypesJson(HttpServletResponse response, DocTypeDto typeDto){

        String str = "{\n";
        str += "  \"typeList\":[";
        for (int i=0;i<typeDto.getTypeList().size();i++){
            if (i!=0){
                str+=",";
            }
            str+=quotesWrap(typeDto.getTypeList().get(i));
        }
        str+="]\n}";
        writeInJson(response,str);
    }

    private static String quotesWrap(String str) {
        return "\"" + str + "\"";
    }

    public static CatalogDto parseJsonToCatalog(InputStream inputStream){
        String requestBody=getRequestBody(inputStream);
        CatalogDto catalogDto;
        String [] strArray = requestBody.split(",");
        String catalogName=getParameterValue("name",strArray);
        String parentName=getParameterValue("parent",strArray);;
        if (catalogName==null){
            log.error("catalogName = null");
            return null;
        }
        if (parentName==null){
            catalogDto = new CatalogDto(catalogName,"/");
        }else{
            catalogDto = new CatalogDto(catalogName,parentName);
        }
        return catalogDto;
    }

    public static String parseDocumentAddingMode(String requestBody){
        String [] strArray = requestBody.split(",");
        return getParameterValue("mode",strArray);
    }

    public static DocumentDto parseNewDocument(String requestBody){
        String [] strArray = requestBody.split(",");
        String docName = getParameterValue("name",strArray);
        String type = getParameterValue("type",strArray);
        String catalogName = getParameterValue("catalogName",strArray);
        DocumentDto documentDto = null;
        if(docName!=null&&type!=null&&catalogName!=null){
            documentDto=new DocumentDto(docName,0,type,catalogName);
        }else{
            log.debug("docName = "+docName);
            log.debug("type = "+type);
            log.debug("catalogName = "+catalogName);
        }
        return documentDto;
    }

    public static DocumentDto parseNewDocVersion(String requestBody){
        String [] strArray = requestBody.split(",");
        int beginOfList =0;

        while (!strArray[beginOfList].contains("[")&& beginOfList <strArray.length){
            beginOfList++;
        }
        int endOfList=0;
        while (!strArray[endOfList].contains("]")&& endOfList <strArray.length){
            endOfList++;
        }
        String[] filePart=null;
        if(beginOfList !=strArray.length){
            filePart = new String[(endOfList-beginOfList+1)/2];
            for (int i=0;i<filePart.length;i++){
                filePart[i]=strArray[beginOfList+i*2]+","+strArray[beginOfList+i*2+1];
            }
        }
        List<DocFileDto> fileList = new ArrayList();
        if (filePart==null){
            log.error("Couldn't parse file list");
        }else{
            for(String str:filePart){
                fileList.add(parseJsonToFile(str));
            }
        }
        String docName = getParameterValue("name",strArray);
        String type = getParameterValue("type",strArray);
        String strVersion = getParameterValue("version", strArray);
        String catalogName = getParameterValue("catalogName",strArray);
        DocumentDto documentDto = null;
        if(docName!=null&&type!=null&&catalogName!=null&&strVersion!=null){
            documentDto=new DocumentDto(docName,Integer.parseInt(strVersion),type,fileList,false, catalogName);
        }else{
            log.debug("docName = "+docName);
            log.debug("version = "+strVersion);
            log.debug("type = "+type);
            log.debug("catalogName = "+catalogName);
        }
        return documentDto;
    }

    public  static DocFileDto parseJsonToFile(String requestBody){
        String[] strArray=requestBody.split(",");
        String fileName = getParameterValue("fileName",strArray);
        String filePath = getParameterValue("path",strArray);
        DocFileDto docFileDto=null;
        if (fileName!=null && filePath != null){
            docFileDto=new DocFileDto(fileName,filePath);
        }else{
            log.error("fileName = " + fileName);
            log.error("filePath = " + filePath);
        }
        return docFileDto;
    }



}



