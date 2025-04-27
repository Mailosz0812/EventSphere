package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface userRepository extends JpaRepository<LoggedUser,Long> {
    Optional<LoggedUser> findLoggedUserByUSERNAME(String USERNAME);
    List<LoggedUser> findLoggedUsersByROLE(Role role);

    @Query("SELECT o FROM Event ev JOIN ev.organizers o WHERE ev = ?1")
    List<LoggedUser> findOrganizerByEvent(Event e);

    @Query("SELECT o FROM Ticket t JOIN t.user o WHERE t.event = ?1")
    List<LoggedUser> findUsersByEvent(Event e);

    Optional<LoggedUser> findLoggedUserByMAIL(String MAIL);
    Optional<LoggedUser> findLoggedUserByUSERNAMEAndROLE(String username,Role role);
    Optional<LoggedUser> findLoggedUserByUSERNAMEOrMAIL(String username, String mail);
}
