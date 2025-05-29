package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.EventOrganize;
import org.locations.eventsphere.Entities.LoggedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface eventOrganizeRepository extends JpaRepository<EventOrganize, Long> {

}
