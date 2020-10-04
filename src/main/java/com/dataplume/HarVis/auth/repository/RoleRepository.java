package com.dataplume.HarVis.auth.repository;


import com.dataplume.HarVis.auth.enums.ERole;
import com.dataplume.HarVis.auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);

	String GET_USER_ROLES = "SELECT * FROM user_roles;";
	@Query(value = GET_USER_ROLES, nativeQuery = true)
	List<Object> getAllUsersRoles();
}
