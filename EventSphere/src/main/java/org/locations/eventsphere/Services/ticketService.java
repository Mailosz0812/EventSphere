package org.locations.eventsphere.Services;

import DTOs.EventTicketsWrapper;
import DTOs.eventDetailsDTO;
import DTOs.ticketDetailsDTO;
import DTOs.ticketPaymentDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.locations.eventsphere.Entities.*;
import org.locations.eventsphere.Exceptions.*;
import org.locations.eventsphere.Repositories.*;
import org.locations.eventsphere.mappers.Mapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ticketService {
    private final ticketRepository ticketRepo;
    private final eventOrganizeRepository eventOrganizeRepo;
    private final eventRepository eventRepo;
    private final userRepository userRepo;
    private final poolRepository poolRepo;
    private final paymentRepository paymentRepo;
    private final Mapper<Event,eventDetailsDTO> eventDetailsMapper;
    private EntityManager entityManager;

    public ticketService(ticketRepository ticketRepo, eventOrganizeRepository eventOrganizeRepo,
                         eventRepository eventRepo, userRepository userRepo, poolRepository poolRepo,
                         paymentRepository paymentRepo, Mapper<Event, eventDetailsDTO> eventDetailsMapper,
                         EntityManager entityManager) {
        this.ticketRepo = ticketRepo;
        this.eventOrganizeRepo = eventOrganizeRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.poolRepo = poolRepo;
        this.paymentRepo = paymentRepo;
        this.eventDetailsMapper = eventDetailsMapper;
        this.entityManager = entityManager;
    }

    public void validateTicketPaymentData(ticketPaymentDTO dto) {
        Event event = eventRepo.findEventByName(dto.getEventName())
                .orElseThrow(() -> new NoSuchEventException("Event not found"));

        LoggedUser user = userRepo.findLoggedUserByMail(dto.getMail())
                .orElseThrow(() -> new NoSuchUserException("User not found"));

        try {
            Double.parseDouble(dto.getAmount());
        } catch (NumberFormatException e) {
            throw new EventSphereException("Invalid amount format");
        }

        if (dto.getCustomTicketInfo() == null || dto.getCustomTicketInfo().isBlank()) {
            throw new EventSphereException("Missing ticket info");
        }

        List<String> infoParts = List.of(dto.getCustomTicketInfo().split(";"));
        for (String info : infoParts) {
            String[] parts = info.split(":");
            if (parts.length != 2) {
                throw new EventSphereException("Invalid ticket info format");
            }

            long poolId;
            int quantity;
            try {
                poolId = Long.parseLong(parts[0]);
                quantity = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                throw new PoolException("Invalid pool ID or quantity format");
            }

            Pool pool = poolRepo.findPoolByPoolID(poolId)
                    .orElseThrow(() -> new NoSuchPoolException("Pool not found: " + poolId));

            if("EMPTY".equals(pool.getPoolStatus()) || pool.getTicketCount() == 0){
                throw new PoolException("Pool already sold out");
            }
            if (pool.getTicketCount() < quantity) {
                throw new PoolException("Not enough tickets available in pool ID: " + poolId);
            }
        }
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
            Pool pool = optPool.get();

            Integer ticketCount = pool.getTicketCount();
            pool.setTicketCount(ticketCount - quantity);
            poolRepo.save(pool);
            for(int i = 0; i < quantity; i++){
                Ticket ticket = new Ticket();
                ticket.setEvent(event);
                ticket.setUser(user);
                ticket.setPool(pool);
                ticket.setPayment(payment);
                ticket.setTicketStatus("ACTIVE");
                ticketsList.add(ticket);
            }

        }
        payment.setTicketsList(ticketsList);
        paymentRepo.save(payment);
        ticketRepo.saveAll(ticketsList);
        return ticketPaymentDTO;
    }
    public List<EventTicketsWrapper> getTicketsByMail(String mail,String sortStatus,String sortDate,String eventPattern) {
        List<Ticket> tickets = getTickets(mail,sortStatus,sortDate,eventPattern);
        Map<Pool, List<Ticket>> ticketsMap = new LinkedHashMap<>();
        List<EventTicketsWrapper> result = new ArrayList<>();

        for (Ticket ticket : tickets) {
            ticketsMap.computeIfAbsent(ticket.getPool(), p -> new ArrayList<>()).add(ticket);
        }

        for (Pool pool : ticketsMap.keySet()) {
            Ticket ticket = ticketsMap.get(pool).get(0);
            Event event = ticket.getEvent();
            eventDetailsDTO eventDetails = eventDetailsMapper.mapTo(event);

            ticketDetailsDTO dto = new ticketDetailsDTO();
            dto.setTicketType(pool.getPoolName());
            dto.setQuantity(ticketsMap.get(pool).size());
            dto.setTicketID(ticket.getTicketId());
            dto.setPoolPrice(String.valueOf(pool.getPrice()));
            dto.setPrice(String.valueOf(pool.getPrice() * dto.getQuantity()));
            dto.setPurchaseDate(ticket.getPayment().getPaymentTimestamp().toLocalDate());
            dto.setTicketStatus(ticket.getTicketStatus());
            EventTicketsWrapper wrapper = new EventTicketsWrapper();
            wrapper.setEventDetails(eventDetails);
            wrapper.setTickets(List.of(dto));
            result.add(wrapper);
        }

        return result;
    }
    private List<Ticket> getTickets(String mail,String statusSort,String dateSort,String eventPattern){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
        Root<Ticket> root = query.from(Ticket.class);
        Join<Ticket,Event> joinEvent = root.join("event");
        Join<Ticket,LoggedUser> joinUser = root.join("user");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(joinUser.get("mail"),mail));
        if ("active".equals(statusSort) || "used".equals(statusSort)) {
            predicates.add(cb.equal(cb.lower(root.get("ticketStatus")), statusSort));
        }
        if (eventPattern != null && !eventPattern.isEmpty()) {
            predicates.add(cb.like(cb.lower(joinEvent.get("name")), eventPattern.toLowerCase() + "%"));
        }
        query.select(root).where(predicates.toArray(new Predicate[0]));
        if("ASC".equalsIgnoreCase(dateSort)){
            query.orderBy(cb.asc(joinEvent.get("eventDate")));
        }else if("DESC".equalsIgnoreCase(dateSort)){
            query.orderBy(cb.desc(joinEvent.get("eventDate")));
        }

        return entityManager.createQuery(query).getResultList();
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
    public int countTicketsByUser(String mail){
        Integer count = ticketRepo.countTicketsByUser_Mail(mail);
        if(count == null){
            return 0;
        }
        return count;
    }
    public ticketDetailsDTO getTicketDetails(Long ticketId){
        Optional<Ticket> optTicket = ticketRepo.findTicketByTicketId(ticketId);
        if(optTicket.isEmpty()){
            throw new NoSuchPoolException("Ticket not found");
        }
        Ticket ticket = optTicket.get();
        ticketDetailsDTO detailsDTO = new ticketDetailsDTO();
        detailsDTO.setTicketID(ticket.getTicketId());
        detailsDTO.setPurchaseDate(ticket.getPayment().getPaymentTimestamp().toLocalDate());
        return detailsDTO;
    }

    private LoggedUser getLoggedUser(String mail) {
        Optional<LoggedUser> optOrganizer = userRepo.findLoggedUserByMail(mail);
        if(optOrganizer.isEmpty()){
            throw new NoSuchUserException("User not found");
        }
        return optOrganizer.get();
    }
    private Event getEvent(String eventName){
        Optional<Event> optEvent = eventRepo.findEventByName(eventName);
        if(optEvent.isEmpty()){
            throw new NoSuchEventException("Event not found");
        }
        return optEvent.get();
    }
}
