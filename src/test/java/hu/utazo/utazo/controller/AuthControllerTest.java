package hu.utazo.utazo.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Felhasználókat listázó admin oldal bejelentkezés nélkül")
    public void checkSiteSecurityTest() throws Exception {
        mockMvc.perform(get("/users/list/0")).andExpect(status().isFound());
    }

    @Test
    @DisplayName("Felhasználókat listázó admin oldal admin jogosultság nélküli felhasználóval")
    @WithMockUser(username = "testUser@test.com", authorities = {"USER"})
    public void checkSiteSecurityWithUserTest() throws Exception {
        mockMvc.perform(get("/users/list/0")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Felhasználókat listázó admin oldal adminnal")
    @WithMockUser(username = "testUser@test.com", authorities = {"ADMIN"})
    public void checkSiteSecurityWithAdminTest() throws Exception {
        mockMvc.perform(get("/users/list/0")).andExpect(status().isOk());
    }

}
