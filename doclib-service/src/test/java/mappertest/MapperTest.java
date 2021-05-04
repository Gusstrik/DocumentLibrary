package mappertest;

import com.strelnikov.doclib.dto.UnitDto;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.dtomapper.configuration.DtoMapperConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class MapperTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(DtoMapperConfiguration.class);

    private DtoMapper dtoMapper=appContext.getBean(DtoMapper.class);

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
        unit.setParent_id(0);
        UnitDto unitDto = dtoMapper.mapUnit(unit);
        Assert.assertEquals("test unit", unitDto.getName());
    }
}
