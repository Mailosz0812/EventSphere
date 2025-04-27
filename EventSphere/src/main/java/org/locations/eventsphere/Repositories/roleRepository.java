package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface roleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findRoleByNAME(String NAME);
}
