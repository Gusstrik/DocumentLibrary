package servicetest;

import com.strelnikov.doclib.dto.ClientDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.ClientActions;
import com.strelnikov.doclib.service.SecurityActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.dtomapper.configuration.DtoMapperConfiguration;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.UserNotFoundException;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ClientImplTest {
    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class,
            RepositoryConfiguration.class, DtoMapperConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final ClientActions clientActions = appContext.getBean(ClientActions.class);
    private final DtoMapper dtoMapper = appContext.getBean(DtoMapper.class);
    private final CatalogActions catalogActions = appContext.getBean(CatalogActions.class);
    private final SecurityActions securityActions = appContext.getBean(SecurityActions.class);

    @Test
    public void createNewClientTest() throws UserNotFoundException, UnitNotFoundException {
        List<PermissionType> permissionTypeList = new ArrayList<>();
        permissionTypeList.add(PermissionType.READING);
        List<PermissionDto> permissionDtoList = new ArrayList<>();
        permissionDtoList.add(new PermissionDto("test","/","Catalog",permissionTypeList));
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        ClientDto clientDto = new ClientDto(0,"test","123",roles,permissionDtoList);
        clientDto = clientActions.saveClient(clientDto);
        boolean actual = securityActions.checkPermission(catalogActions.loadCatalog(1), clientDto.getLogin(), PermissionType.READING);
        clientActions.deleteClient(clientDto.getId());
        Assert.assertTrue(actual);
    }

    @Test
    public void updateClientTest() throws UserNotFoundException {
        ClientDto clientDto = clientActions.loadClient(1);
        Client client = dtoMapper.mapClient(clientDto);
        client.setLogin("test");
        clientDto = dtoMapper.mapClient(client);
        clientActions.saveClient(clientDto);
        clientDto = clientActions.loadClient(1);
        String actual = clientDto.getLogin();
        client.setLogin("root");
        clientDto = dtoMapper.mapClient(client);
        clientActions.saveClient(clientDto);
        Assert.assertEquals("test",actual);
    }
}
