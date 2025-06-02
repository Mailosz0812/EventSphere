package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface userRepository extends JpaRepository<LoggedUser,Long> {
    Optional<LoggedUser> findLoggedUserByUsername(String USERNAME);
    @Query("SELECT lu FROM LoggedUser lu WHERE lu.password IS NOT NULL AND lu.role= :role")
    List<LoggedUser> findLoggedUsersByRole(@Param("role") Role role);
    Optional<LoggedUser> findLoggedUserByMail(String MAIL);
    List<LoggedUser> findLoggedUserByUsernameOrMail(String username, String mail);
    @Query("SELECT lu FROM LoggedUser lu WHERE lu.userTimestamp > :timestamp")
    List<LoggedUser> findLoggedUsersByUserTimestamp(@Param("timestamp") LocalDateTime timestamp);
    int countLoggedUserByRole(Role role);
}
