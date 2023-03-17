package com.example.garage_f.controller;


import com.example.garage_f.model.Car;
import com.example.garage_f.model.Owner;
import com.example.garage_f.service.CarService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private OwnerService ownerService;

    @Test
    @WithMockUser(authorities = {"USER"})
    public void gettingCars_withAuthentication_shouldReturnPageOfCars() throws Exception {
        when(ownerService.findById(anyInt())).thenReturn(Owner.builder().build());

        mockMvc.perform(get("/cars/{owner_id}", anyInt()))
                .andExpect(status().isOk())
                .andExpect(view().name("cars/list"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("cars"));
    }

    @Test
    public void gettingCars_withoutAuthentication_shouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void gettingRegistrationFormTest() throws Exception {
        when(ownerService.findById(anyInt())).thenReturn(Owner.builder().build());

        mockMvc.perform(get("/cars/{owner_id}/registration", anyInt()))
                .andExpect(status().isOk())
                .andExpect(view().name("cars/new"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void savingCar_shouldRedirectToPageOfCars() throws Exception {
        when(carService.save(any())).thenReturn(Car.builder().build());

        mockMvc.perform(post("/cars")
                        .flashAttr("car", Car.builder().build())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/cars/**"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void gettingUpdateFormTest() throws Exception {
        when(carService.findById(anyInt())).thenReturn(Car.builder().build());
        when(ownerService.findById(anyInt())).thenReturn(Owner.builder().build());

        mockMvc.perform(get("/cars/{id}/updating", anyInt()))
                .andExpect(status().isOk())
                .andExpect(view().name("cars/update"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void deleting_shouldRedirectToPageOfCars() throws Exception {

        when(carService.findById(anyInt())).thenReturn(Car.builder().build());
        doNothing().when(carService).deleteById(anyInt());

        mockMvc.perform(delete("/cars/{id}", anyInt()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/cars/**"));
    }
}
