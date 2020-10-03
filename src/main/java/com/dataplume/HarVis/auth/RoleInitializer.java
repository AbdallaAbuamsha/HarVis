package com.dataplume.HarVis.auth;

import com.dataplume.HarVis.auth.enums.ERole;
import com.dataplume.HarVis.auth.models.Role;
import com.dataplume.HarVis.auth.repository.RoleRepository;
import com.dataplume.HarVis.har.models.Campaign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(RoleInitializer.class);


    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role(ERole.ROLE_ADMIN));
        roleRepository.save(new Role(ERole.ROLE_USER));
        roleRepository.findAll().forEach(r -> logger.info(r.toString()));
    }
}
