package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.Role;
import com.unsa.etf.Identity.Service.Service.RoleService;
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

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @MockBean
    private BodyValidator bodyValidator;


    @Test
    void shouldGetNoRoles() throws Exception {
        doReturn(Lists.newArrayList()).when(roleService).getRoles();
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldGetRoles() throws Exception {
        Role role1 = new Role("P1");
        Role role2 = new Role("P2");
        Role role3 = new Role("P3");
        doReturn(Lists.newArrayList(role1, role2, role3)).when(roleService).getRoles();
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(role1.getId())))
                .andExpect(jsonPath("$[0].name", is("P1")))
                .andExpect(jsonPath("$[1].id", is(role2.getId())))
                .andExpect(jsonPath("$[1].name", is("P2")))
                .andExpect(jsonPath("$[2].id", is(role3.getId())))
                .andExpect(jsonPath("$[2].name", is("P3")));
    }

    @Test
    void shouldGetRoleById() throws Exception {
        Role role = new Role("R");
        given(roleService.getRoleById("id")).willReturn(role);
        mockMvc.perform(get("/roles/{roleId}", "id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(role.getId())))
                .andExpect(jsonPath("$.name", is("R")));
    }

    @Test
    public void roleToBeDeletedDoesNotExistTest() throws Exception {
        given(roleService.deleteRole("id")).willReturn(false);
        mockMvc.perform(delete("/roles/{roleId}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Role Does Not Exist!")));
    }

    @Test
    public void shouldDeleteRole() throws Exception {
        given(roleService.deleteRole("id")).willReturn(true);
        mockMvc.perform(delete("/roles/{roleId}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Role Successfully Deleted!")));
    }

    @Test
    public void shouldDeleteAllRoles() throws Exception {
        given(roleService.deleteAll()).willReturn(true);
        mockMvc.perform(delete("/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Roles Successfully Deleted!")));
    }
}