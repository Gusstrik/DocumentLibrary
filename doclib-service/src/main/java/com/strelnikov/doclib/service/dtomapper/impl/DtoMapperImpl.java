package com.strelnikov.doclib.service.dtomapper.impl;

import com.strelnikov.doclib.dto.*;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.UnitType;
import com.strelnikov.doclib.model.documnets.*;
import com.strelnikov.doclib.model.roles.Authority;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.Permission;
import com.strelnikov.doclib.repository.*;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.model.catalogs.Catalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DtoMapperImpl implements DtoMapper {

    private final DocumentDao docDao;
    private final DocTypeDao docTypeDao;
    private final PermissionDao permissionDao;
    private final ClientDao clientDao;

    public DtoMapperImpl(@Qualifier("DocumentJpa") DocumentDao docDao, @Qualifier("DocTypeJpa") DocTypeDao docTypeDao,
                         @Autowired PermissionDao permissionDao, @Autowired ClientDao clientDao){
        this.permissionDao=permissionDao;
        this.clientDao=clientDao;
        this.docTypeDao=docTypeDao;
        this.docDao=docDao;
    }

    @Override
    public UnitDto mapUnit(Unit unit) {
        if (unit.getId() == 1) {
            return new UnitDto(unit.getId(), unit.getName(), unit.getUnitType().toString(), 0);
        } else {
            return new UnitDto(unit.getId(), unit.getName(), unit.getUnitType().toString(), unit.getCatalogId());
        }
    }

    @Override
    public Unit mapUnit(UnitDto unitDto) {
        Unit unit;
        if (unitDto.getUnitType().equals(UnitType.CATALOG.toString())) {
            unit = new Catalog();
        } else {
            unit = new Document();
        }
        unit.setName(unitDto.getName());
        unit.setId(unitDto.getId());
        if (unitDto.getId() != 1) {
            unit.setCatalogId(unitDto.getParentId());
        } else {
            unit.setCatalogId(0);
        }
        return unit;
    }

    @Override
    public CatalogDto mapCatalog(Catalog catalog) {
        List<UnitDto> list = new ArrayList<>();
        for (Unit unit : catalog.getContentList()) {
            list.add(mapUnit(unit));
        }
        if (catalog.getId() == 1) {
            return new CatalogDto(catalog.getId(), catalog.getName(), 0, list);
        } else {
            return new CatalogDto(catalog.getId(), catalog.getName(), catalog.getCatalogId(), list);
        }
    }

    @Override
    public Catalog mapCatalog(CatalogDto catalogDto) {
        Catalog catalog = new Catalog();
        catalog.setId(catalogDto.getId());
        catalog.setName(catalogDto.getName());
        List<Unit> list = new ArrayList<>();
        for (UnitDto unitDto : catalogDto.getContentList()) {
            list.add(mapUnit(unitDto));
        }
        if (catalogDto.getParentId() != 0) {
            catalog.setCatalogId(catalogDto.getParentId());
        }
        catalog.setContentList(list);
        return catalog;
    }

    @Override
    public DocTypeDto mapDocType(DocumentType documentType) {
        return new DocTypeDto(documentType.getId(),documentType.getCurentType());
    }

    @Override
    public DocumentType mapDocType(DocTypeDto docTypeDto) {
        DocumentType documentType = new DocumentType();
        documentType.setId(docTypeDto.getId());
        documentType.setCurentType(docTypeDto.getDocType());
        return documentType;
    }

    @Override
    public DocFileDto mapDocFile(DocumentFile docFile) {
        return new DocFileDto(docFile.getId(), docFile.getFileName(), docFile.getFilePath());
    }

    @Override
    public DocumentFile mapDocFile(DocFileDto docFileDto) {
        DocumentFile docFile = new DocumentFile();
        docFile.setId(docFileDto.getId());
        docFile.setFileName(docFileDto.getName());
        docFile.setFilePath(docFileDto.getPath());
        return docFile;
    }

    @Override
    public DocumentVersion mapDocVersion(DocVersionDto docVersionDto) {
        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setId(docVersionDto.getId());
        if (docVersionDto.getDocumentId() != 0)
            docVersion.setParentDocument(docDao.loadDocument(docVersionDto.getDocumentId()));
        docVersion.setVersion(docVersionDto.getVersion());
        docVersion.setImportance(Importance.valueOf(docVersionDto.getImportance()));
        docVersion.setModerated(docVersionDto.isModerated());
        if (docVersionDto.getDescription() == null) {
            docVersion.setDescription("");
        } else {
            docVersion.setDescription(docVersionDto.getDescription());
        }
        List<DocumentFile> list = new ArrayList<>();
        for (DocFileDto fileDto : docVersionDto.getFileList()) {
            list.add(mapDocFile(fileDto));
        }
        docVersion.setFilesList(list);
        return docVersion;
    }

    @Override
    public DocVersionDto mapDocVersion(DocumentVersion documentVersion) {
        List<DocFileDto> list = new ArrayList<>();
        for (DocumentFile file : documentVersion.getFilesList()) {
            list.add(mapDocFile(file));
        }
        return new DocVersionDto(documentVersion.getId(), documentVersion.getParentDocument().getId(), documentVersion.getVersion(),
                documentVersion.getDescription(), documentVersion.getImportance().toString(), documentVersion.isModerated(), list);
    }

    @Override
    public Document mapDocument(DocumentDto documentDto) {
        Document document = new Document();
        document.setId(documentDto.getId());
        document.setDocumentType(docTypeDao.loadType(documentDto.getType()));
        document.setName(documentDto.getName());
        document.setCatalogId(documentDto.getCatalogId());
        document.setActualVersion(documentDto.getActualVersion());
        document.setVersionsList(new ArrayList<>());
        document.getVersionsList().add(mapDocVersion(documentDto.getVersion()));
        return document;
    }

    @Override
    public DocumentDto mapDocument(Document document) {
        DocVersionDto docVersionDto = mapDocVersion(document.getDocumentVersion());
        return new DocumentDto(document.getId(), document.getName(), document.getDocumentType().getId(),
                document.getActualVersion(), document.getCatalogId(), docVersionDto);
    }
    @Override
    public DocumentDto mapDocument(Document document, int version) {
        DocVersionDto docVersionDto = mapDocVersion(document.getDocumentVersion(version));
        return new DocumentDto(document.getId(), document.getName(), document.getDocumentType().getId(),
                document.getActualVersion(), document.getCatalogId(), docVersionDto);
    }

    @Override
    public PermissionDto mapPermission(Permission permission) {
        return new PermissionDto(permission.getClient().getLogin(),permission.getSecuredObject().getName(),
                permission.getSecuredObject().getClass().getSimpleName(),permission.getPermissionList());
    }

    @Override
    public Permission mapPermission(PermissionDto permissionDto) {
        Permission permission = new Permission();
        permission.setClient(clientDao.findBylogin(permissionDto.getClientLogin()));
        permission.setPermissionList(permissionDto.getPermissionTypeList());
        permission.setSecuredObject(permissionDao.getSecuredObjectByObjectName(permissionDto.getObjectName(),permissionDto.getObjectType()));
        return permission;
    }

    @Override
    public Client mapClient(ClientDto clientDto) {
        Client client = new Client();
        client.setLogin(clientDto.getLogin());
        client.setPassword(clientDto.getPassword());
        client.setId(clientDto.getId());
        for (String role:clientDto.getRoles()){
            client.getAuthorities().add(new Authority(role));
        }
        return client;
    }

    @Override
    public ClientDto mapClient(Client client) {
        List<PermissionDto> permissionDtoList = new ArrayList<>();
        for (Permission permission:permissionDao.getClientPermissions(client)){
            permissionDtoList.add(mapPermission(permission));
        }
        return new ClientDto(client.getId(),client.getLogin(),client.getPassword(),
                client.getAuthorities().stream().map(authority -> authority.getName().toString()).toList(),
                permissionDtoList);
    }
}

