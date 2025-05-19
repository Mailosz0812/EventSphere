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
    Optional<LoggedUser> findLoggedUserByUSERNAME(String USERNAME);
    @Query("SELECT lu FROM LoggedUser lu WHERE lu.PASSWORD IS NOT NULL AND lu.ROLE= :role")
    List<LoggedUser> findLoggedUsersByROLE(@Param("role") Role role);
    Optional<LoggedUser> findLoggedUserByMAIL(String MAIL);
    List<LoggedUser> findLoggedUserByUSERNAMEOrMAIL(String username, String mail);
    @Query("SELECT lu FROM LoggedUser lu WHERE lu.userTimestamp < :timestamp")
    List<LoggedUser> findLoggedUsersByUSER_TIMESTAMP(@Param("timestamp") LocalDateTime timestamp);
    int countLoggedUserByROLE(Role role);
}
