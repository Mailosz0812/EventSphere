package org.locations.eventsphere.Repositories;


import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface imageRepository extends JpaRepository<EventImage, Long> {
    Optional<EventImage> findEventImageByEvent(Event event);
}
