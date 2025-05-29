package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.Pool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface poolRepository extends JpaRepository<Pool,Long> {
    List<Pool> findPoolsByEvent(Event event);
    Optional<Pool> findPoolByPoolID(Long poolID);
    void deletePoolByEventAndPoolID(Event event, Long poolID);

}
