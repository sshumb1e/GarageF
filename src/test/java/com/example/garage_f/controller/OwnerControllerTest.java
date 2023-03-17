package com.example.garage_f.controller;


import com.example.garage_f.model.Owner;
import com.example.garage_f.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Test
    @WithMockUser(authorities = {"USER"})
    public void gettingOwners_withoutAuthentication_shouldReturnPageOfOwners() throws Exception {
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/list"))
                .andExpect(model().attributeExists("owners"));
    }

    @Test
    public void gettingOwners_withoutAuthentication_shouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void gettingRegistrationFormTest() throws Exception {
        mockMvc.perform(get("/owners/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/new"))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void savingOwner_shouldRedirectToPageOfOwners() throws Exception {
        when(ownerService.save(any())).thenReturn(Owner.builder().build());

        mockMvc.perform(post("/owners")
                        .flashAttr("owner", Owner.builder().build())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owners"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void gettingUpdateFormTest() throws Exception {
        when(ownerService.findById(anyInt())).thenReturn(Owner.builder().build());

        mockMvc.perform(get("/owners/{id}/updating", anyInt()))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/update"))
                .andExpect(model().attributeExists("owner"));
    }


    @Test
    @WithMockUser(authorities = {"USER"})
    public void deleting_shouldRedirectToPageOfOwners() throws Exception {
        doNothing().when(ownerService).deleteById(anyInt());

        mockMvc.perform(delete("/owners/{id}", anyInt()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owners"));
    }
}
