package com.unsa.etf.Identity.Service.Repository;

import com.unsa.etf.Identity.Service.AppConfig;
import com.unsa.etf.Identity.Service.Model.Permission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration (
        classes = {AppConfig.class},
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class PermissionRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PermissionRepository permissionRepository;


    @Test
    public void initialStateShouldBeEmpty() {
        List<Permission>  permissions = permissionRepository.findAll();
        assertTrue(permissions.isEmpty());
    }

    @Test
    public void findAllPermissionsTest() {
        Permission permission0 = new Permission("Permisija0");
        Permission permission1 = new Permission("Permisija1");
        Permission permission2 = new Permission("Permisija2");
        Permission permission3 = new Permission("Permisija3");

        testEntityManager.persist(permission0);
        testEntityManager.persist(permission1);
        testEntityManager.persist(permission2);
        testEntityManager.persist(permission3);
        testEntityManager.flush();

        List<Permission> permissionList = permissionRepository.findAll();

//        assertTrue(permissionList.contains(Arrays.asList(permission0, permission1, permission2, permission3)));
        assertEquals(permissionList, Arrays.asList(permission0, permission1, permission2, permission3));
    }

    @Test
    public void addPermissionTest() {
        Permission permission = new Permission("Permisija");
        assertTrue(permissionRepository.findPermissionByName("Permisija").isEmpty());
        permissionRepository.save(permission);
        assertTrue(permissionRepository.findAll().contains(permission));
    }

    @Test
    public void deletePermissionTest() {
        Permission permission = new Permission("Permisija");
        permissionRepository.save(permission);
        assertTrue(permissionRepository.findAll().contains(permission));

        permissionRepository.delete(permission);
        assertTrue(permissionRepository.findPermissionByName("Permisija").isEmpty());
    }
}