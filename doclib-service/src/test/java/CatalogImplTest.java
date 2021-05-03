import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class CatalogImplTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class, RepositoryConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final CatalogActions catalogAction = appContext.getBean(CatalogActions.class);

    @BeforeClass
    public static void beforeCatalogImpTests() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Test
    public void createNewCatalogTest() {
        Catalog catalog = catalogAction.createNewCatalog("test_catalog2","test_parent");
        List<String> expected = new ArrayList<>();
        expected.add("test_catalog");
        expected.add("test_catalog2");
        expected.add("test_doc");
        List<String> actual = catalogAction.showContent(new Catalog("test_parent"));
        catalogAction.deleteCatalog(catalog.getName());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteCatalogTest(){
        Catalog catalog = catalogAction.createNewCatalog("test_catalog2","test_parent");
        catalogAction.deleteCatalog(catalog.getName());
        Catalog parent = new Catalog("test_parent");
        List<String> actual = catalogAction.showContent(parent);
        List<String> expected = new ArrayList<>();
        expected.add("test_catalog");
        expected.add("test_doc");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void showContentCatalogTest(){
        Catalog parent = new Catalog("test_parent");
        List<String> expected = new ArrayList<>();
        expected.add("test_catalog");
        expected.add("test_doc");
        Assert.assertEquals(expected,catalogAction.showContent(parent));
    }

    @Test
    public void loadCatalogTest(){
        List<String> expected = catalogAction.showContent(new Catalog("/"));
        Catalog catalog = catalogAction.loadCatalog("/");
        List<String> actual = new ArrayList<>();
        for (Unit u:catalog.getContentList()){
            actual.add(u.getName());
        }
        Assert.assertEquals(expected,actual);
    }
}


