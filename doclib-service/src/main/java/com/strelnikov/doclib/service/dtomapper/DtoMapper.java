package com.strelnikov.doclib.service.dtomapper;


import com.strelnikov.doclib.dto.*;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.Permission;


public interface DtoMapper {

    UnitDto mapUnit(Unit unit);

    Unit mapUnit(UnitDto unitDto);

    CatalogDto mapCatalog(Catalog catalog);

    Catalog mapCatalog(CatalogDto catalogDto);

    DocTypeDto mapDocType(DocumentType documentType);

    DocumentType mapDocType(DocTypeDto docTypeDto);

    DocFileDto mapDocFile(DocumentFile docFile);

    DocumentFile mapDocFile(DocFileDto docFileDto);

    DocumentVersion mapDocVersion(DocVersionDto docVersionDto);

    DocVersionDto mapDocVersion(DocumentVersion documentVersion);

    Document mapDocument(DocumentDto documentDto);

    DocumentDto mapDocument(Document document);

    DocumentDto mapDocument(Document document, int version);

    PermissionDto mapPermission(Permission permission);

    Permission mapPermission(PermissionDto permissionDto);

    Client mapClient(ClientDto clientDto);

    ClientDto mapClient(Client client);
}
