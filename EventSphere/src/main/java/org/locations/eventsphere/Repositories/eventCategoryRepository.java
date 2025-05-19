package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface eventCategoryRepository extends JpaRepository<EventCategory,Long> {

    Optional<EventCategory> findEventCategoryByNAME(String name);
    List<EventCategory> findAll();
}
