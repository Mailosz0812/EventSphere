package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.Location;
import org.locations.eventsphere.Entities.LoggedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface eventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findEventByNAME(String name);
    @Query("SELECT e FROM Event e JOIN e.organizers o WHERE o = ?1")
    List<Event> findEventsByOrganizer(LoggedUser organizer);
//    List<Event> findEventByLOCATION(String Location);
    @Query("SELECT e FROM  Ticket t JOIN t.event e WHERE t.user = ?1")
    List<Event> findEventsByUser(LoggedUser participant);


}
