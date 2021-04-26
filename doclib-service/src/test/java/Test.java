import service.catalogs.Catalog;

public class Test {
    public static void main(String[] args) {
        Catalog catalog = new Catalog("test");
        catalog.setName("abc");
        System.out.println(catalog.getName());
    }


}
