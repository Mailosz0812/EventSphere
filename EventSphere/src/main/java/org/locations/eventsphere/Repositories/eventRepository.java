package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.EventCategory;
import org.locations.eventsphere.Entities.LoggedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface eventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findEventByName(String name);
    Optional<Event> findEventByNameAndEventStatus(String name,String status);
    @Query("SELECT eo.event FROM EventOrganize eo WHERE eo.organizer = :organizer AND eo.event.eventStatus= :status")
    List<Event> findEventsByOrganizerAndEventStatus(@Param("organizer") LoggedUser organizer,@Param("status") String eventStatus);
    int countEventsByEventStatus(String status);
    List<Event> findEventsByCreatedAtAfter(LocalDateTime time);
    List<Event> findEventsByEventCategoryAndEventStatus(EventCategory category,String eventStatus);
    List<Event> findEventsByEventStatus(String eventStatus);
    List<Event> findEventsByNameContainsIgnoreCaseAndEventStatus(String name,String status);
    List<Event> findAllByEventDateBefore(LocalDate time);


}
