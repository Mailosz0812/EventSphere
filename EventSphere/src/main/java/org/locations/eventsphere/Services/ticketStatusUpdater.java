package org.locations.eventsphere.Services;

import jakarta.transaction.Transactional;
import org.locations.eventsphere.Entities.Ticket;
import org.locations.eventsphere.Repositories.ticketRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

public class ticketStatusUpdater {
    private final ticketRepository ticketRepo;

    public ticketStatusUpdater(ticketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @Transactional
    @Scheduled(fixedRate = 3600000)
    public void updateTicketStatus(){
        LocalDate now = LocalDate.now();
        List<Ticket> ticketList = ticketRepo.findAllByEventDateBeforeAndStatusNot(now,"ACTIVE");
        for (Ticket ticket : ticketList) {
            ticket.setTicketStatus("USED");
        }
        ticketRepo.saveAll(ticketList);
    }
}
