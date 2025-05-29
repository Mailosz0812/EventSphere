package org.locations.eventsphere.Services;

import DTOs.ticketPaymentDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.locations.eventsphere.Entities.*;
import org.locations.eventsphere.Exceptions.EventSphereException;
import org.locations.eventsphere.Exceptions.NoSuchEventException;
import org.locations.eventsphere.Exceptions.NoSuchPoolException;
import org.locations.eventsphere.Exceptions.NoSuchUserException;
import org.locations.eventsphere.Repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ticketService {
    private final ticketRepository ticketRepo;
    private final eventOrganizeRepository eventOrganizeRepo;
    private final eventRepository eventRepo;
    private final userRepository userRepo;
    private final poolRepository poolRepo;
    private final paymentRepository paymentRepo;

    public ticketService(ticketRepository ticketRepo, eventOrganizeRepository eventOrganizeRepo,
                         eventRepository eventRepo,
                         userRepository userRepo,
                         poolRepository poolRepo,
                         paymentRepository paymentRepo) {
        this.ticketRepo = ticketRepo;
        this.eventOrganizeRepo = eventOrganizeRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.poolRepo = poolRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public ticketPaymentDTO createTicketPayment(ticketPaymentDTO ticketPaymentDTO){
        Event event = getEvent(ticketPaymentDTO.getEventName());
        LoggedUser user = getLoggedUser(ticketPaymentDTO.getMail());
        List<String> info = List.of(ticketPaymentDTO.getCustomTicketInfo().split(";"));
        List<Ticket> ticketsList = new ArrayList<>();
        Payment payment = new Payment();
        payment.setPaymentMethod(ticketPaymentDTO.getMethod());
        try {
            payment.setAmount(Double.parseDouble(ticketPaymentDTO.getAmount()));
        }catch (NumberFormatException e){
            throw new EventSphereException("Something went wrong");
        }
        for (String s : info) {
            System.out.println(s);
            List<String> ticketInfo = List.of(s.split(":"));
            Long poolID = null;
            int quantity = 0;
            try{
                poolID = Long.parseLong(ticketInfo.get(0));
                quantity = Integer.parseInt(ticketInfo.get(1));
            }catch(NumberFormatException e){
                throw new EventSphereException("Something went wrong");
            }
            Optional<Pool> optPool = poolRepo.findPoolByPoolID(poolID);
            if(optPool.isEmpty()){
                throw new NoSuchPoolException("Pool not found");
            }

            for(int i = 0; i < quantity; i++){
                System.out.println(i);
                Pool pool = optPool.get();
                Integer ticketCount = pool.getTicketCount();
                pool.setTicketCount(--ticketCount);
                Ticket ticket = new Ticket();
                ticket.setEvent(event);
                ticket.setUser(user);
                ticket.setPool(pool);
                ticket.setPayment(payment);
                ticketsList.add(ticket);
            }
        }
        payment.setTicketsList(ticketsList);
        paymentRepo.save(payment);
        ticketRepo.saveAll(ticketsList);
        return ticketPaymentDTO;
    }

    public int countSoldTicketsByOrganizer(String mail){
        LoggedUser organizer = getLoggedUser(mail);
        Integer ticketCount = ticketRepo.countTicketsByEventOrganizer(organizer);
        if(ticketCount == null){
            return 0;
        }
        return ticketCount;
    }
    public void countTicketsByOrganizer(String mail){
        LoggedUser organizer = getLoggedUser(mail);
//        Integer ticketCount = eventOrganizeRepo.sumEventTicketsByOrganizerMail(organizer);
//        if(ticketCount == null){
//            return 0;
//        }
//        return ticketCount;
    }

    private LoggedUser getLoggedUser(String mail) {
        Optional<LoggedUser> optOrganizer = userRepo.findLoggedUserByMAIL(mail);
        if(optOrganizer.isEmpty()){
            throw new NoSuchUserException("User not found");
        }
        return optOrganizer.get();
    }
    private Event getEvent(String eventName){
        Optional<Event> optEvent = eventRepo.findEventByNAME(eventName);
        if(optEvent.isEmpty()){
            throw new NoSuchEventException("Event not found");
        }
        return optEvent.get();
    }
}
