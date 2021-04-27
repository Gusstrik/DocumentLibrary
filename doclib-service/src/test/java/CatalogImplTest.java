import com.strelnikov.doclib.database.CatalogDao;
import com.strelnikov.doclib.database.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.impl.CatalogImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CatalogImplTest {

    private final CatalogActions catalogAction = new CatalogImpl();

    @BeforeClass
    public static void beforCataloImpTests() {
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Test
    public void createNewCatalogTest() {
        Catalog catalog = catalogAction.createNewCatalog("test_catalog2","test_parent");
        List<String> expected = new ArrayList();
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
        List<String> expected = new ArrayList();
        expected.add("test_catalog");
        expected.add("test_doc");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void showContentCatalogTest(){
        Catalog parent = new Catalog("test_parent");
        List<String> expected = new ArrayList();
        expected.add("test_catalog");
        expected.add("test_doc");
        Assert.assertEquals(expected,catalogAction.showContent(parent));
    }

    @Test
    public void loadCatalogTest(){
        List<String> expected = catalogAction.showContent(new Catalog("/"));
        Catalog catalog = catalogAction.loadCatalog("/");
        List<String> actual = new ArrayList();
        for (Unit u:catalog.getContentList()){
            actual.add(u.getName());
        }
        Assert.assertEquals(expected,actual);
    }
}


