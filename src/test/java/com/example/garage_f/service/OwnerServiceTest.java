package com.example.garage_f.service;


import com.example.garage_f.model.Owner;
import com.example.garage_f.repository.OwnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {


    @Mock
    private OwnerRepository ownerRepository;
    private OwnerService ownerService;
    private Owner testOwner;

    @BeforeEach
    public void init() {
        ownerService = new OwnerService(ownerRepository);
        testOwner = Owner.builder().name("Test owner").build();
    }

    @Test
    public void getAllTest() {

        List<Owner> expectedOwners = List.of(
                Owner.builder().name("Owner 1").build(),
                Owner.builder().name("Owner 2").build(),
                Owner.builder().name("Owner 3").build()
        );

        Mockito.when(ownerRepository.findAll()).thenReturn(expectedOwners);

        List<Owner> owners = ownerService.findAll();
        Assertions.assertEquals(owners, expectedOwners);
    }

    @Test
    public void findByIdTest() {

        Mockito.when(ownerRepository.findById(anyInt())).thenReturn(Optional.of(testOwner));
        Owner owner = ownerService.findById(anyInt());
        Assertions.assertEquals(owner, testOwner);
    }

    @Test
    public void findByIdNotFoundTest(){

        Mockito.when(ownerRepository.findById(anyInt())).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> ownerService.findById(anyInt()));
        Assertions.assertEquals(NOT_FOUND, responseStatusException.getStatusCode());
    }

    @Test
    public void saveTest() {

        Mockito.when(ownerRepository.save(any())).thenReturn(testOwner);
        Owner expectedOwner = ownerService.save(Owner.builder().build());
        Assertions.assertEquals(expectedOwner, testOwner);
    }

    @Test
    public void deleteTest() {

        doNothing().when(ownerRepository).deleteById(anyInt());
        ownerService.deleteById(anyInt());
        verify(ownerRepository, times(1)).deleteById(anyInt());
    }
}
