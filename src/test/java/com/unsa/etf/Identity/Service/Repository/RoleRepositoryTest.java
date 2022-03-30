package com.unsa.etf.Identity.Service.Repository;

import com.unsa.etf.Identity.Service.AppConfig;
import com.unsa.etf.Identity.Service.Model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(
        classes = {AppConfig.class},
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void initialStateShouldBeEmpty() {
        List<Role> roles = roleRepository.findAll();
        assertTrue(roles.isEmpty());
    }

    @Test
    public void findAllRoles() {
        Role role1 = new Role("R1");
        Role role2 = new Role("R2");
        Role role3 = new Role("R3");

        testEntityManager.persist(role1);
        testEntityManager.persist(role2);
        testEntityManager.persist(role3);
        testEntityManager.flush();

        List<Role> roleList = roleRepository.findAll();

        assertEquals(roleList, Arrays.asList(role1, role2, role3));
    }

    @Test
    public void addRoleTest() {
        Role role = new Role("Role");
        assertTrue(roleRepository.findRoleByName("Role").isEmpty());
        roleRepository.save(role);
        assertTrue(roleRepository.findAll().contains(role));
    }

    @Test
    public void deleteRoleTest() {
        Role role = new Role("Role");
        roleRepository.save(role);
        assertTrue(roleRepository.findAll().contains(role));

        roleRepository.delete(role);
        assertTrue(roleRepository.findRoleByName("Role").isEmpty());
    }

}