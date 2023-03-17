package com.example.garage_f.service;

import com.example.garage_f.model.Owner;
import com.example.garage_f.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class OwnerService {


    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Cacheable(value = "allOwners")
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Cacheable(value = "owner", key = "#id")
    public Owner findById(int id) {
        return ownerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @Caching(evict = {
            @CacheEvict(value = "allOwners", allEntries = true),
            @CacheEvict(value = "owner", key = "#owner.id")})
    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Caching(evict = {
            @CacheEvict(value = "allOwners", allEntries = true),
            @CacheEvict(value = "owner", key = "#id")})
    public void deleteById(int id) {
        ownerRepository.deleteById(id);
    }

}
