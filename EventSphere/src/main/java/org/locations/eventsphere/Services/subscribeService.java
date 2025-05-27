package org.locations.eventsphere.Services;

import DTOs.eventDTO;
import DTOs.subscribeDTO;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.EventSubscribe;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Exceptions.NoSuchEventException;
import org.locations.eventsphere.Exceptions.NoSuchUserException;
import org.locations.eventsphere.Exceptions.subscribeProcessingException;
import org.locations.eventsphere.Repositories.eventRepository;
import org.locations.eventsphere.Repositories.subscribeRepository;
import org.locations.eventsphere.Repositories.userRepository;
import org.locations.eventsphere.mappers.Mapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class subscribeService {
    private final subscribeRepository subscribeRepo;
    private final userRepository userRepo;
    private final eventRepository eventRepo;
    private final Mapper<Event,eventDTO> eventMapper;

    public subscribeService(subscribeRepository subscribeRepo, userRepository userRepo, eventRepository eventRepo, Mapper<Event, eventDTO> eventMapper) {
        this.subscribeRepo = subscribeRepo;
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
        this.eventMapper = eventMapper;
    }

    public void subscribeEvent(subscribeDTO subDTO){
        Optional<Event> optEvent = getEvent(subDTO.getEventName());
        Optional<LoggedUser> optUser = getLoggedUser(subDTO.getUserMail());
        EventSubscribe subEvent = new EventSubscribe();
        subEvent.setEvent(optEvent.get());
        subEvent.setLoggedUser(optUser.get());
        subscribeRepo.save(subEvent);
    }

    public void unsubscribeEvent(subscribeDTO subDTO){
        Optional<Event> optEvent = getEvent(subDTO.getEventName());
        Optional<LoggedUser> optUser = getLoggedUser(subDTO.getUserMail());
        Optional<EventSubscribe> optSubscribe = subscribeRepo.findEventSubscribeByEventAndLoggedUser(optEvent.get(),optUser.get());
        if(optSubscribe.isEmpty()){
            throw new subscribeProcessingException("Subscription not found");
        }
        subscribeRepo.delete(optSubscribe.get());
    }
    public boolean getSubscribeState(String name,String mail){
        Optional<Event> optEvent = getEvent(name);
        Optional<LoggedUser> optUser = getLoggedUser(mail);
        return subscribeRepo.existsEventSubscribeByEventAndLoggedUser(optEvent.get(),optUser.get());

    }
    public int countSubscribeEvent(String mail){
        return subscribeRepo.countEventSubscribeByLoggedUser_MAIL(mail);
    }
    public List<eventDTO> getRecentlySubscribed(String mail){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDay = now.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        List<EventSubscribe> recentlySub = subscribeRepo.findEventSubscribeBySubscribedAtAfterAndLoggedUser_MAIL(firstDay,mail);
        List<Event> events = new ArrayList<>();
        for (EventSubscribe eventSubscribe : recentlySub) {
            events.add(eventSubscribe.getEvent());
        }
        return eventMapper.mapToList(events);
    }
    public List<eventDTO> getIncomingEvents(String mail){
        LocalDate now = LocalDate.now();
        LocalDate max = now.plusWeeks(2);
        List<Event> incomingEvents = subscribeRepo.findEventSubscribesByEvent_EVENTDATEBetween(now,max,mail);
        return eventMapper.mapToList(incomingEvents);
    }
    private Optional<Event> getEvent(String name) {
        Optional<Event> optEvent = eventRepo.findEventByNAME(name);
        if(optEvent.isEmpty()){
            throw new NoSuchEventException("Event not found");
        }
        return optEvent;
    }

    private Optional<LoggedUser> getLoggedUser(String mail) {
        Optional<LoggedUser> optUser = userRepo.findLoggedUserByMAIL(mail);
        if(optUser.isEmpty()){
            throw new NoSuchUserException("User not found");
        }
        return optUser;
    }


}
