package mappertest;

import com.strelnikov.doclib.dto.*;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.documnets.*;
import com.strelnikov.doclib.repository.*;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.dtomapper.configuration.DtoMapperConfiguration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.print.Doc;
import java.util.ArrayList;


public class MapperTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(DtoMapperConfiguration.class);
    private static DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private DtoMapper dtoMapper=appContext.getBean(DtoMapper.class);
    private DocTypeDao docTypeDao = appContext.getBean("DocTypeJpa",DocTypeDao.class);
    private DocVersionDao docVersionDao = appContext.getBean("DocVersionJpa", DocVersionDao.class);
    private DocFileDao docFileDao = appContext.getBean("DocFileJpa",DocFileDao.class);
    private DocumentDao docDao = appContext.getBean("DocumentJpa",DocumentDao.class);
    private CatalogDao catDao = appContext.getBean("CatalogJpa",CatalogDao.class);

    @BeforeClass
    public static void initDataBase(){
        creator.runScript("src/test/resources/insertestdb.sql");
    }
    @AfterClass
    public static void deleteTestData(){
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Test
    public void unitMapTest(){
        UnitDto unitDto = new UnitDto(1,"test unit", "CATALOG",0);
        Unit unit = dtoMapper.mapUnit(unitDto);
        Assert.assertEquals("test unit", unit.getName());
    }

    @Test
    public void unitDtoMapTest(){
        Unit unit = new Catalog();
        unit.setId(1);
        unit.setName("test unit");
        unit.setCatalogId(0);
        UnitDto unitDto = dtoMapper.mapUnit(unit);
        Assert.assertEquals("test unit", unitDto.getName());
    }

    @Test
    public void typeDtoMapTest(){
        DocumentType docType = docTypeDao.loadType(1);
        DocTypeDto docTypeDto = dtoMapper.mapDocType(docType);
        Assert.assertEquals("test_type",docTypeDto.getDocType());
    }

    @Test
    public void  typeMapTest(){
        DocumentType docType = docTypeDao.loadType(1);
        DocTypeDto docTypeDto = dtoMapper.mapDocType(docType);
        docType = dtoMapper.mapDocType(docTypeDto);
        Assert.assertEquals("test_type",docType.getCurentType());
    }

    @Test
    public void fileDtoMapTest(){
        DocumentVersion docVersion = docVersionDao.loadDocVersion(1);
        DocumentFile docFile = docVersion.getFilesList().get(0);
        DocFileDto docFileDto = dtoMapper.mapDocFile(docFile);
        Assert.assertEquals("test_file",docFileDto.getName());
    }

    @Test
    public void fileMapTest(){
        DocumentVersion docVersion = docVersionDao.loadDocVersion(1);
        DocumentFile docFile = docVersion.getFilesList().get(0);
        DocFileDto docFileDto = dtoMapper.mapDocFile(docFile);
        docFile = dtoMapper.mapDocFile(docFileDto);
        Assert.assertEquals("test_file",docFile.getFileName());
    }

    @Test
    public void versionDtoMapTest(){
        DocumentVersion docVersion = docVersionDao.loadDocVersion(1);
        DocVersionDto docVersionDto = dtoMapper.mapDocVersion(docVersion);
        Assert.assertEquals(1,docVersionDto.getId());
    }

    @Test
    public void versionMapTest(){
        DocumentVersion docVersion = docVersionDao.loadDocVersion(1);
        DocVersionDto docVersionDto = dtoMapper.mapDocVersion(docVersion);
        docVersion = dtoMapper.mapDocVersion(docVersionDto);
        Assert.assertEquals(1,docVersionDto.getId());
    }

    @Test
    public void documentDtoMapTest(){
        Document doc= docDao.loadDocument(1);
        DocumentDto docDto= dtoMapper.mapDocument(doc);
        Assert.assertEquals("test_doc",docDto.getName());
    }

//    @Test
//    public void documentMapTest(){
//        Document doc= docDao.loadDocument(1);
//        DocumentDto docDto= dtoMapper.mapDocument(doc);
//        doc = dtoMapper.mapDocument(docDto);
//        Assert.assertEquals("test_doc",doc.getName());
//    }

    @Test
    public void catalogDtoMapTest(){
        Catalog cat= catDao.loadCatalog(1);
        CatalogDto catDto= dtoMapper.mapCatalog(cat);
        Assert.assertEquals("/",catDto.getName());
    }

    @Test
    public void catalogMapTest(){
        Catalog cat= catDao.loadCatalog(1);
        CatalogDto catDto= dtoMapper.mapCatalog(cat);
        cat = dtoMapper.mapCatalog(catDto);
        Assert.assertEquals("/",cat.getName());
    }


}
