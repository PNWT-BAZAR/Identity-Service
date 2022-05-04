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
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.objectsList", hasSize(0)));
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
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.objectsList", hasSize(3)))
                .andExpect(jsonPath("$.objectsList[0].firstName", is("Faruk")))
                .andExpect(jsonPath("$.objectsList[0].lastName", is("Smajlovic")))
                .andExpect(jsonPath("$.objectsList[0].username", is("fsmajlovic")))
                .andExpect(jsonPath("$.objectsList[0].passwordHash", is("do3218uejd")))
                .andExpect(jsonPath("$.objectsList[0].email", is("fsmajlovic2@etf.unsa.ba")))
                .andExpect(jsonPath("$.objectsList[0].phoneNumber", is("061111222")))
                .andExpect(jsonPath("$.objectsList[0].shippingAddress", is("Envera Sehovica 24")))

                .andExpect(jsonPath("$.objectsList[1].firstName", is("Kemal")))
                .andExpect(jsonPath("$.objectsList[1].lastName", is("Lazovic")))
                .andExpect(jsonPath("$.objectsList[1].username", is("klazovic1")))
                .andExpect(jsonPath("$.objectsList[1].passwordHash", is("jdoa9920")))
                .andExpect(jsonPath("$.objectsList[1].email", is("klazovic1@etf.unsa.ba")))
                .andExpect(jsonPath("$.objectsList[1].phoneNumber", is("061333444")))
                .andExpect(jsonPath("$.objectsList[1].shippingAddress", is("Podbudakovici 4")))

                .andExpect(jsonPath("$.objectsList[2].firstName", is("Taida")))
                .andExpect(jsonPath("$.objectsList[2].lastName", is("Kadric")))
                .andExpect(jsonPath("$.objectsList[2].username", is("tkadric28")))
                .andExpect(jsonPath("$.objectsList[2].passwordHash", is("da09dasp")))
                .andExpect(jsonPath("$.objectsList[2].email", is("tkadric1@etf.unsa.ba")))
                .andExpect(jsonPath("$.objectsList[2].phoneNumber", is("061555666")))
                .andExpect(jsonPath("$.objectsList[2].shippingAddress", is("Sulejmana Filipovica 10")));
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
                .andExpect(jsonPath("$.object.firstName", is("Taida")))
                .andExpect(jsonPath("$.object.lastName", is("Kadric")))
                .andExpect(jsonPath("$.object.username", is("tkadric28")))
                .andExpect(jsonPath("$.object.passwordHash", is("da09dasp")))
                .andExpect(jsonPath("$.object.email", is("tkadric1@etf.unsa.ba")))
                .andExpect(jsonPath("$.object.phoneNumber", is("061555666")))
                .andExpect(jsonPath("$.object.shippingAddress", is("Sulejmana Filipovica 10")));
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
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.objectsList", hasSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.objectsList[0].id", is(faruk.getId())))
                .andExpect(jsonPath("$.objectsList[0].firstName", is("Faruk")))
                .andExpect(jsonPath("$.objectsList[0].lastName", is("Smajlovic")))
                .andExpect(jsonPath("$.objectsList[0].username", is("fsmajlovic")))
                .andExpect(jsonPath("$.objectsList[0].passwordHash", is("do3218uejd")))
                .andExpect(jsonPath("$.objectsList[0].email", is("fsmajlovic2@etf.unsa.ba")))
                .andExpect(jsonPath("$.objectsList[0].phoneNumber", is("061111222")))
                .andExpect(jsonPath("$.objectsList[0].shippingAddress", is("Envera Sehovica 24")))

                .andExpect(jsonPath("$.objectsList[1].firstName", is("Kemal")))
                .andExpect(jsonPath("$.objectsList[1].lastName", is("Lazovic")))
                .andExpect(jsonPath("$.objectsList[1].username", is("klazovic1")))
                .andExpect(jsonPath("$.objectsList[1].passwordHash", is("jdoa9920")))
                .andExpect(jsonPath("$.objectsList[1].email", is("klazovic1@etf.unsa.ba")))
                .andExpect(jsonPath("$.objectsList[1].phoneNumber", is("061333444")))
                .andExpect(jsonPath("$.objectsList[1].shippingAddress", is("Podbudakovici 4")));
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
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.objectsList", hasSize(3)))

                .andExpect(jsonPath("$.objectsList[0].firstName", is("Taida")))
                .andExpect(jsonPath("$.objectsList[0].lastName", is("Kadric")))
                .andExpect(jsonPath("$.objectsList[0].username", is("tkadric28")))
                .andExpect(jsonPath("$.objectsList[0].passwordHash", is("da09dasp")))
                .andExpect(jsonPath("$.objectsList[0].email", is("tkadric1@etf.unsa.ba")))
                .andExpect(jsonPath("$.objectsList[0].phoneNumber", is("061555666")))
                .andExpect(jsonPath("$.objectsList[0].shippingAddress", is("Sulejmana Filipovica 10")))

                .andExpect(jsonPath("$.objectsList[1].firstName", is("Kemal")))
                .andExpect(jsonPath("$.objectsList[1].lastName", is("Lazovic")))
                .andExpect(jsonPath("$.objectsList[1].username", is("klazovic1")))
                .andExpect(jsonPath("$.objectsList[1].passwordHash", is("jdoa9920")))
                .andExpect(jsonPath("$.objectsList[1].email", is("klazovic1@etf.unsa.ba")))
                .andExpect(jsonPath("$.objectsList[1].phoneNumber", is("061333444")))
                .andExpect(jsonPath("$.objectsList[1].shippingAddress", is("Podbudakovici 4")))

                .andExpect(jsonPath("$.objectsList[2].firstName", is("Faruk")))
                .andExpect(jsonPath("$.objectsList[2].lastName", is("Smajlovic")))
                .andExpect(jsonPath("$.objectsList[2].username", is("fsmajlovic")))
                .andExpect(jsonPath("$.objectsList[2].passwordHash", is("do3218uejd")))
                .andExpect(jsonPath("$.objectsList[2].email", is("fsmajlovic2@etf.unsa.ba")))
                .andExpect(jsonPath("$.objectsList[2].phoneNumber", is("061111222")))
                .andExpect(jsonPath("$.objectsList[2].shippingAddress", is("Envera Sehovica 24")));
    }

    @Test
    public void userToBeDeletedDoesNotExistTest() throws Exception {
        given(userService.deleteUserById("id")).willReturn(false);
        mockMvc.perform(delete("/users/{userId}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode", is(409)))
                .andExpect(jsonPath("$.error.message", is("User Does Not Exist!")))
                .andExpect(jsonPath("$.message", is("Error has occurred!")));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        given(userService.deleteUserById("id")).willReturn(true);
        mockMvc.perform(delete("/users/{userId}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User Successfully Deleted!")));
    }

    @Test
    public void shouldDeleteAllUsers() throws Exception {
        given(userService.deleteAllUsers()).willReturn(true);
        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Users Successfully Deleted!")));
    }
}