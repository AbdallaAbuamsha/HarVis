package com.dataplume.HarVis.auth.security.services;

import com.dataplume.HarVis.auth.models.Role;
import com.dataplume.HarVis.auth.repository.RoleRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role getById(Long id) throws NotFoundException {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent())
            return role.get();
        throw new NotFoundException(("role with id "+id+" not found"));
    }
}
