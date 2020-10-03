package com.dataplume.HarVis.auth.controllers;

import com.dataplume.HarVis.auth.models.Role;
import com.dataplume.HarVis.auth.security.services.RoleService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<Role>> getAll()
    {
        return ResponseEntity.ok(roleService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable long id) throws NotFoundException {
        return ResponseEntity.ok(roleService.getById(id));
    }
}
