package com.strelnikov.doclib.service.dtomapper.impl;


import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.model.roles.SecuredObject;
import com.strelnikov.doclib.service.dtomapper.DtoClassMapper;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DtoClassMapperImpl implements DtoClassMapper {

    @Autowired
    private DtoMapper dtoMapper;

    @Override
    public SecuredObject mapClass(Object object) {
        switch (object.getClass().getSimpleName()) {
            case "CatalogDto":
                return dtoMapper.mapCatalog((CatalogDto) object);
            case "DocumentDto":
                return dtoMapper.mapDocument((DocumentDto) object);
            case "DocFileDto":
                return dtoMapper.mapDocFile((DocFileDto) object);
        }
        return null;
    }
}
