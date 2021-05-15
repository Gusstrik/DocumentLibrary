package com.strelnikov.doclib.service.dtomapper;

import com.strelnikov.doclib.model.roles.SecuredObject;

public interface DtoClassMapper {

    SecuredObject mapClass(Object object);
}
