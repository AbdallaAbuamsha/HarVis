package com.dataplume.HarVis.auth;

import com.dataplume.HarVis.auth.controllers.AuthController;
import com.dataplume.HarVis.auth.enums.ERole;
import com.dataplume.HarVis.auth.models.Role;
import com.dataplume.HarVis.auth.payload.request.SignupRequest;
import com.dataplume.HarVis.auth.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class AuthInitializer implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(AuthInitializer.class);


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthController authController;

    @Override
    public void run(String... args) {
        roleRepository.save(new Role(ERole.ROLE_ADMIN));
        roleRepository.save(new Role(ERole.ROLE_USER));

        authController.registerUser(new SignupRequest("admin1", "admin1@dp.com", "123456", new HashSet<>(){{add("admin");}}));
        authController.registerUser(new SignupRequest("admin2", "admin2@dp.com", "123456", new HashSet<>(){{add("admin");}}));
        authController.registerUser(new SignupRequest("user1", "user1@dp.com", "123456", new HashSet<>(){{add("user");}}));
        authController.registerUser(new SignupRequest("user2", "user2@dp.com", "123456", new HashSet<>(){{add("user");}}));
        authController.registerUser(new SignupRequest("user3", "user3@dp.com", "123456", new HashSet<>(){{add("user");}}));
        authController.registerUser(new SignupRequest("user4", "user4@dp.com", "123456", new HashSet<>(){{add("user");}}));
    }
}
