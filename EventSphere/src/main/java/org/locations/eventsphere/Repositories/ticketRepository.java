package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ticketRepository extends JpaRepository<Ticket,Long> {
    @Query("SELECT COUNT(t) FROM EventOrganize eo JOIN eo.event e JOIN Ticket t ON t.event = e WHERE eo.organizer = :organizer")
    Integer countTicketsByEventOrganizer(@Param("organizer")LoggedUser organizer);
}
