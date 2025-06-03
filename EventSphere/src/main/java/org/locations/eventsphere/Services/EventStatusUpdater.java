package org.locations.eventsphere.Services;

import jakarta.transaction.Transactional;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Repositories.eventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventStatusUpdater {
    private final eventRepository eventRepo;

    public EventStatusUpdater(eventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }
    @Transactional
    @Scheduled(fixedRate = 3600000)
    public void updateTicketStatus(){
        System.out.println("Event Scheduler");
        LocalDate now = LocalDate.now();
        List<Event> events = eventRepo.findAllByEventDateBefore(now);
        for (Event event : events) {
            event.setEventStatus("DONE");
        }
        eventRepo.saveAll(events);
    }
}
