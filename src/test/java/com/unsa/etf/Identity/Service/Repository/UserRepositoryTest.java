package com.unsa.etf.Identity.Service.Repository;

import com.unsa.etf.Identity.Service.TestConfig;
import com.unsa.etf.Identity.Service.Model.User;
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
        classes = {TestConfig.class},
        loader = AnnotationConfigContextLoader.class
)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void initialStateShouldBeEmpty() {
        List<User> users = userRepository.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    public void findAllUsersTest() {

        User faruk = new User(
                "Faruk",
                "Smajlovic",
                "fsmajlovic",
                "do3218uejd",
                "fsmajlovic2@etf.unsa.ba",
                "061111222",
                "Envera Sehovica 24",
                null);
        User kemal = new User(
                "Kemal",
                "Lazovic",
                "klazovic1",
                "jdoa9920",
                "klazovic1@etf.unsa.ba",
                "061333444",
                "Podbudakovici 4",
                null);

        User taida = new User(
                "Taida",
                "Kadric",
                "tkadric28",
                "da09dasp",
                "tkadric1@etf.unsa.ba",
                "061555666",
                "Sulejmana Filipovica 10",
                null);

        userRepository.saveAll(Arrays.asList(faruk, kemal, taida));

        List<User> userList = userRepository.findAll();

        assertEquals(userList, Arrays.asList(faruk, kemal, taida));
    }

    @Test
    public void addUserTest() {
        User user = new User(
                "Taida",
                "Kadric",
                "tkadric28",
                "da09dasp",
                "tkadric1@etf.unsa.ba",
                "061555666",
                "Sulejmana Filipovica 10",
                null);
        userRepository.save(user);
        assertTrue(userRepository.findAll().contains(user));
    }

    @Test
    public void deleteUserTest() {
        User user = new User(
                "Taida",
                "Kadric",
                "tkadric28",
                "da09dasp",
                "tkadric1@etf.unsa.ba",
                "061555666",
                "Sulejmana Filipovica 10",
                null);
        userRepository.save(user);
        assertTrue(userRepository.findAll().contains(user));

        userRepository.delete(user);
        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    public void getUsersByName() {
        User faruk = new User(
                "Faruk",
                "Smajlovic",
                "fsmajlovic",
                "do3218uejd",
                "fsmajlovic2@etf.unsa.ba",
                "061111222",
                "Envera Sehovica 24",
                null);
        User kemal = new User(
                "Kemal",
                "Lazovic",
                "klazovic1",
                "jdoa9920",
                "klazovic1@etf.unsa.ba",
                "061333444",
                "Podbudakovici 4",
                null);

        User taida = new User(
                "Taida",
                "Kadric",
                "tkadric28",
                "da09dasp",
                "tkadric1@etf.unsa.ba",
                "061555666",
                "Sulejmana Filipovica 10",
                null);

        userRepository.saveAll(Arrays.asList(faruk, kemal, taida));

        List<User> users = userRepository.findUsersByFirstOrLastName("vic");
        assertEquals(users, Arrays.asList(faruk, kemal));
    }
}