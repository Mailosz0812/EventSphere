package org.locations.eventsphere.Repositories;

import DTOs.dailySalesDetails;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ticketRepository extends JpaRepository<Ticket,Long> {
    @Query("SELECT COUNT(t) FROM EventOrganize eo JOIN eo.event e JOIN Ticket t ON t.event = e WHERE eo.organizer = :organizer")
    Integer countTicketsByEventOrganizer(@Param("organizer")LoggedUser organizer);
    Optional<Ticket> findTicketByTicketId(Long ticketId);
    @Query("SELECT t FROM Ticket t JOIN t.event e WHERE e.eventDate < :now AND t.ticketStatus = :status")
    List<Ticket> findAllByEventDateBeforeAndStatus(@Param("now") LocalDate now, @Param("status") String status);
    Integer countTicketsByUser_Mail(String mail);
    Integer countTicketsByPoolPoolID(Long poolID);
    @Query("SELECT COUNT(t) FROM Ticket t JOIN t.event e JOIN e.organizes eo WHERE eo.organizer.mail = :mail")
    Long countSoldTicketsByOrganizerMail(@Param("mail") String mail);
    Integer countTicketsByEvent(Event event);
    @Query("""
    SELECT NEW DTOs.dailySalesDetails(
        to_char( t.payment.paymentTimestamp,'YYYY-MM-DD'),
        COUNT(t)
    )
    FROM Ticket t
    WHERE t.event = :event
    GROUP BY to_char( t.payment.paymentTimestamp,'YYYY-MM-DD')
    ORDER BY to_char(t.payment.paymentTimestamp, 'YYYY-MM-DD')
""")
    List<dailySalesDetails> getTicketStatsByEvent(Event event);
}
