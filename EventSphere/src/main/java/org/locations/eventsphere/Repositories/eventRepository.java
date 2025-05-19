package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.LoggedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface eventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findEventByNAME(String name);

    @Query("SELECT eo.event FROM EventOrganize eo WHERE eo.organizer = :organizer ")
    List<Event> findEventsByOrganizer(@Param("organizer") LoggedUser organizer);
    int countEventsBy();
    List<Event> findEventsByCreatedAtAfter(LocalDateTime time);
    List<Event> findEventsBy();
}
