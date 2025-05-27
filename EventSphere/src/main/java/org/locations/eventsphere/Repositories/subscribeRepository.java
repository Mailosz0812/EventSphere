package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.EventSubscribe;
import org.locations.eventsphere.Entities.LoggedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface subscribeRepository extends JpaRepository<EventSubscribe,Long> {

    Optional<EventSubscribe> findEventSubscribeByEventAndLoggedUser(Event event, LoggedUser user);
    boolean existsEventSubscribeByEventAndLoggedUser(Event event, LoggedUser user);
    int countEventSubscribeByLoggedUser_MAIL(String mail);
    List<EventSubscribe> findEventSubscribeBySubscribedAtAfterAndLoggedUser_MAIL(LocalDateTime time, String mail);
    @Query("SELECT es.event FROM EventSubscribe es WHERE es.event.EVENTDATE BETWEEN :min AND :max AND es.loggedUser.MAIL = :mail")
    List<Event> findEventSubscribesByEvent_EVENTDATEBetween(@Param("min") LocalDate min, @Param("max") LocalDate max, @Param("mail") String mail);

}
