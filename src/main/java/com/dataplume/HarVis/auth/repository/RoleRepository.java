package com.dataplume.HarVis.auth.repository;


import com.dataplume.HarVis.auth.enums.ERole;
import com.dataplume.HarVis.auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
