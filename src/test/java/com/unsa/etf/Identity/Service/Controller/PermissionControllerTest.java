package com.unsa.etf.Identity.Service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unsa.etf.Identity.Service.Model.Permission;
import com.unsa.etf.Identity.Service.Service.PermissionService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PermissionController.class)
class PermissionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PermissionService permissionService;

    @MockBean
    private BodyValidator bodyValidator;

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void shouldGetNoPermissions() throws Exception {
        doReturn(Lists.newArrayList()).when(permissionService).getPermissions();
        mockMvc.perform(get("/permissions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldGetPermissions() throws Exception {
        Permission permission1 = new Permission("P1");
        Permission permission2 = new Permission("P2");
        Permission permission3 = new Permission("P3");
        doReturn(Lists.newArrayList(permission1, permission2, permission3)).when(permissionService).getPermissions();
        mockMvc.perform(get("/permissions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(permission1.getId())))
                .andExpect(jsonPath("$[0].name", is("P1")))
                .andExpect(jsonPath("$[1].id", is(permission2.getId())))
                .andExpect(jsonPath("$[1].name", is("P2")))
                .andExpect(jsonPath("$[2].id", is(permission3.getId())))
                .andExpect(jsonPath("$[2].name", is("P3")));
    }

    @Test
    void shouldGetPermissionById() throws Exception {
        Permission permission = new Permission("P");
        given(permissionService.getPermissionById("id")).willReturn(permission);
        mockMvc.perform(get("/permissions/{permissionId}", "id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(permission.getId())))
                .andExpect(jsonPath("$.name", is("P")));
    }

    @Test
    void shouldGetPermissionByName() throws Exception {
        Permission permission = new Permission("Perm");
        Permission permission1 = new Permission("Permisija");
        given(permissionService.getPermissionWithName("Per")).willReturn(Lists.newArrayList(permission, permission1));
        mockMvc.perform(get("/permissions/name/{name}", "Per"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(permission.getId())))
                .andExpect(jsonPath("$[0].name", is("Perm")))
                .andExpect(jsonPath("$[1].id", is(permission1.getId())))
                .andExpect(jsonPath("$[1].name", is("Permisija")));
    }

//    @Test
//    void shouldCreateNewPermission() throws Exception {
//        Permission permission = new Permission("Permission name");
//
//        given(permissionService.addNewPermission(permission)).willReturn(permission);
//        mockMvc.perform(post("/permissions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(permission)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$.id", is(permission.getId())))
//                .andExpect(jsonPath("$.name", is("Permission name")));
//    }

    @Test
    public void permissionToBeDeletedDoesNotExistTest() throws Exception {
        given(permissionService.deletePermission("id")).willReturn(false);
        mockMvc.perform(delete("/permissions/{permissionId}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Permission Does Not Exist!")));
    }

    @Test
    public void shouldDeletePermission() throws Exception {
        given(permissionService.deletePermission("id")).willReturn(true);
        mockMvc.perform(delete("/permissions/{permissionId}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Permission Successfully Deleted!")));
    }

    @Test
    public void shouldDeleteAllPermissions() throws Exception {
        given(permissionService.deleteAll()).willReturn(true);
        mockMvc.perform(delete("/permissions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Permissions Successfully Deleted!")));
    }
}