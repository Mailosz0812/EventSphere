package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ticketRepository extends JpaRepository<Ticket,Long> {
    @Query("SELECT COUNT(t) FROM EventOrganize eo JOIN eo.event e JOIN Ticket t ON t.event = e WHERE eo.organizer = :organizer")
    Integer countTicketsByEventOrganizer(@Param("organizer")LoggedUser organizer);
    Optional<Ticket> findTicketByTicketId(Long ticketId);
    @Query("SELECT t FROM Ticket t JOIN t.event e WHERE e.eventDate < :now AND t.ticketStatus != :status")
    List<Ticket> findAllByEventDateBeforeAndStatusNot(@Param("now") LocalDate now, @Param("status") String status);
    Integer countTicketsByUser_Mail(String mail);
}
