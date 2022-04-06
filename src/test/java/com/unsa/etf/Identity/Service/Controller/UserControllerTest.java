package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.User;
import com.unsa.etf.Identity.Service.Service.UserService;
import com.unsa.etf.Identity.Service.Validator.BodyValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BodyValidator bodyValidator;

    @Test
    void shouldGetNoUsers() throws Exception {
        doReturn(Lists.newArrayList()).when(userService).getUsers();
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldGetUsers() throws Exception {
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
        doReturn(Lists.newArrayList(faruk, kemal, taida)).when(userService).getUsers();
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(faruk.getId())))
                .andExpect(jsonPath("$[0].firstName", is("Faruk")))
                .andExpect(jsonPath("$[0].lastName", is("Smajlovic")))
                .andExpect(jsonPath("$[0].username", is("fsmajlovic")))
                .andExpect(jsonPath("$[0].passwordHash", is("do3218uejd")))
                .andExpect(jsonPath("$[0].email", is("fsmajlovic2@etf.unsa.ba")))
                .andExpect(jsonPath("$[0].phoneNumber", is("061111222")))
                .andExpect(jsonPath("$[0].shippingAddress", is("Envera Sehovica 24")))

                .andExpect(jsonPath("$[1].id", is(kemal.getId())))
                .andExpect(jsonPath("$[1].firstName", is("Kemal")))
                .andExpect(jsonPath("$[1].lastName", is("Lazovic")))
                .andExpect(jsonPath("$[1].username", is("klazovic1")))
                .andExpect(jsonPath("$[1].passwordHash", is("jdoa9920")))
                .andExpect(jsonPath("$[1].email", is("klazovic1@etf.unsa.ba")))
                .andExpect(jsonPath("$[1].phoneNumber", is("061333444")))
                .andExpect(jsonPath("$[1].shippingAddress", is("Podbudakovici 4")))

                .andExpect(jsonPath("$[2].id", is(taida.getId())))
                .andExpect(jsonPath("$[2].firstName", is("Taida")))
                .andExpect(jsonPath("$[2].lastName", is("Kadric")))
                .andExpect(jsonPath("$[2].username", is("tkadric28")))
                .andExpect(jsonPath("$[2].passwordHash", is("da09dasp")))
                .andExpect(jsonPath("$[2].email", is("tkadric1@etf.unsa.ba")))
                .andExpect(jsonPath("$[2].phoneNumber", is("061555666")))
                .andExpect(jsonPath("$[2].shippingAddress", is("Sulejmana Filipovica 10")));
    }

    @Test
    void shouldGetUserById() throws Exception {
        User taida = new User(
                "Taida",
                "Kadric",
                "tkadric28",
                "da09dasp",
                "tkadric1@etf.unsa.ba",
                "061555666",
                "Sulejmana Filipovica 10",
                null);
        given(userService.getUserById("id")).willReturn(taida);
        mockMvc.perform(get("/users/{userId}", "id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(taida.getId())))
                .andExpect(jsonPath("$.firstName", is("Taida")))
                .andExpect(jsonPath("$.lastName", is("Kadric")))
                .andExpect(jsonPath("$.username", is("tkadric28")))
                .andExpect(jsonPath("$.passwordHash", is("da09dasp")))
                .andExpect(jsonPath("$.email", is("tkadric1@etf.unsa.ba")))
                .andExpect(jsonPath("$.phoneNumber", is("061555666")))
                .andExpect(jsonPath("$.shippingAddress", is("Sulejmana Filipovica 10")));
    }

    @Test
    void shouldGetUsersByName() throws Exception {
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

        given(userService.getUsersByName("vic")).willReturn(Lists.newArrayList(faruk, kemal));
        mockMvc.perform(get("/users/name/{name}", "vic"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(faruk.getId())))
                .andExpect(jsonPath("$[0].firstName", is("Faruk")))
                .andExpect(jsonPath("$[0].lastName", is("Smajlovic")))
                .andExpect(jsonPath("$[0].username", is("fsmajlovic")))
                .andExpect(jsonPath("$[0].passwordHash", is("do3218uejd")))
                .andExpect(jsonPath("$[0].email", is("fsmajlovic2@etf.unsa.ba")))
                .andExpect(jsonPath("$[0].phoneNumber", is("061111222")))
                .andExpect(jsonPath("$[0].shippingAddress", is("Envera Sehovica 24")))

                .andExpect(jsonPath("$[1].id", is(kemal.getId())))
                .andExpect(jsonPath("$[1].firstName", is("Kemal")))
                .andExpect(jsonPath("$[1].lastName", is("Lazovic")))
                .andExpect(jsonPath("$[1].username", is("klazovic1")))
                .andExpect(jsonPath("$[1].passwordHash", is("jdoa9920")))
                .andExpect(jsonPath("$[1].email", is("klazovic1@etf.unsa.ba")))
                .andExpect(jsonPath("$[1].phoneNumber", is("061333444")))
                .andExpect(jsonPath("$[1].shippingAddress", is("Podbudakovici 4")));
    }

    @Test
    void shouldGetUsersSortedByLastName() throws Exception {
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
        doReturn(Lists.newArrayList(taida, kemal, faruk)).when(userService).sortByLastName();
        mockMvc.perform(get("/users/sort"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(taida.getId())))
                .andExpect(jsonPath("$[0].firstName", is("Taida")))
                .andExpect(jsonPath("$[0].lastName", is("Kadric")))
                .andExpect(jsonPath("$[0].username", is("tkadric28")))
                .andExpect(jsonPath("$[0].passwordHash", is("da09dasp")))
                .andExpect(jsonPath("$[0].email", is("tkadric1@etf.unsa.ba")))
                .andExpect(jsonPath("$[0].phoneNumber", is("061555666")))
                .andExpect(jsonPath("$[0].shippingAddress", is("Sulejmana Filipovica 10")))

                .andExpect(jsonPath("$[1].id", is(kemal.getId())))
                .andExpect(jsonPath("$[1].firstName", is("Kemal")))
                .andExpect(jsonPath("$[1].lastName", is("Lazovic")))
                .andExpect(jsonPath("$[1].username", is("klazovic1")))
                .andExpect(jsonPath("$[1].passwordHash", is("jdoa9920")))
                .andExpect(jsonPath("$[1].email", is("klazovic1@etf.unsa.ba")))
                .andExpect(jsonPath("$[1].phoneNumber", is("061333444")))
                .andExpect(jsonPath("$[1].shippingAddress", is("Podbudakovici 4")))

                .andExpect(jsonPath("$[2].id", is(faruk.getId())))
                .andExpect(jsonPath("$[2].firstName", is("Faruk")))
                .andExpect(jsonPath("$[2].lastName", is("Smajlovic")))
                .andExpect(jsonPath("$[2].username", is("fsmajlovic")))
                .andExpect(jsonPath("$[2].passwordHash", is("do3218uejd")))
                .andExpect(jsonPath("$[2].email", is("fsmajlovic2@etf.unsa.ba")))
                .andExpect(jsonPath("$[2].phoneNumber", is("061111222")))
                .andExpect(jsonPath("$[2].shippingAddress", is("Envera Sehovica 24")));
    }

    @Test
    public void userToBeDeletedDoesNotExistTest() throws Exception {
        given(userService.deleteUserById("id")).willReturn(false);
        mockMvc.perform(delete("/users/{userId}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("User Does Not Exist!")));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        given(userService.deleteUserById("id")).willReturn(true);
        mockMvc.perform(delete("/users/{userId}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User Successfully Deleted!")));
    }

    @Test
    public void shouldDeleteAllUsers() throws Exception {
        given(userService.deleteAllUsers()).willReturn(true);
        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Users Successfully Deleted!")));
    }
}