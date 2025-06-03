package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface poolRepository extends JpaRepository<Pool,Long> {
    List<Pool> findPoolsByEventAndPoolStatus(Event event,String poolStatus);
    Optional<Pool> findPoolByPoolID(Long poolID);
    void deletePoolByPoolID(Long poolID);
    @Query("SELECT SUM(p.ticketCount) FROM Pool p JOIN p.event e JOIN e.organizes eo WHERE eo.organizer.mail = :mail")
    Long sumTicketCountByOrganizerMail(@Param("mail") String mail);

}
