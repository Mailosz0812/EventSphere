package org.locations.eventsphere.Services;
import DTOs.eventDTO;
import DTOs.userRegisterDTO;
import org.locations.eventsphere.Entities.*;
import org.locations.eventsphere.Exceptions.EventAlreadyExistsException;
import org.locations.eventsphere.Exceptions.NoSuchCategoryException;
import org.locations.eventsphere.Exceptions.NoSuchEventException;
import org.locations.eventsphere.Exceptions.NoSuchUserException;
import org.locations.eventsphere.Repositories.*;
import org.locations.eventsphere.mappers.Mapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class eventService {
    private eventRepository eventRepo;
    private userRepository userRepo;
    private roleRepository roleRepo;
    private Mapper<Event, eventDTO> eventMapper;
    private eventCategoryRepository categoryRepo;
    private eventOrganizeRepository eOrganizeRepository;

    public eventService(eventRepository eventRepo, userRepository userRepo, roleRepository roleRepo, Mapper<Event, eventDTO> eventMapper, eventCategoryRepository categoryRepo, eventOrganizeRepository eOrganizeRepository) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.eventMapper = eventMapper;
        this.categoryRepo = categoryRepo;
        this.eOrganizeRepository = eOrganizeRepository;
    }

    public eventDTO organizeEvent(eventDTO eventDTO){
        Optional<Event> eventOptional = eventRepo.findEventByNAME(eventDTO.getNAME());
        if(eventOptional.isPresent()){
            throw new EventAlreadyExistsException("Event named: "+eventDTO.getNAME()+" already exists");
        }
        Optional<LoggedUser> organizerOptional = userRepo.findLoggedUserByMAIL(eventDTO.getOrganizerMail());
        if(organizerOptional.isEmpty()){
            throw new NoSuchUserException("Something went wrong :(");
        }
        EventCategory category = checkCategory(eventDTO);
        Event newEvent = eventMapper.mapFrom(eventDTO);
        newEvent.setEVENTCATEGORY(category);
        eventRepo.save(newEvent);
        EventOrganize eventOrganize = new EventOrganize();
        eventOrganize.setEvent(newEvent);
        eventOrganize.setOrganizer(organizerOptional.get());
        eOrganizeRepository.save(eventOrganize);
        return eventDTO;
    }

    private EventCategory checkCategory(eventDTO eventDTO) {
        Optional<EventCategory> categoryOptional = categoryRepo.findEventCategoryByNAME(eventDTO.getCATEGORY());
        if(categoryOptional.isEmpty()){
            throw new NoSuchCategoryException("No category named: "+ eventDTO.getCATEGORY());
        }
        return categoryOptional.get();
    }

    public eventDTO updateEvent(eventDTO eventDTO){
        Event updateEvent = checkEvent(eventDTO);
        updateEvent.setDESCRIPTION(eventDTO.getDESCRIPTION());
        updateEvent.setEVENTDATE(eventDTO.getEVENTDATE());
        updateEvent.setLOCATION(eventDTO.getLOCATION());
        updateEvent.setNAME(eventDTO.getNAME());
        return eventMapper.mapTo(eventRepo.save(updateEvent));
    }

    public List<eventDTO> getEventsByOrganizer(String mail){
        Optional<LoggedUser> optionalUser = userRepo.findLoggedUserByMAIL(mail);
        if(optionalUser.isEmpty()){
            throw new NoSuchUserException("Something went wrong");
        }
        List<Event> eventsList = eventRepo.findEventsByOrganizer(optionalUser.get());
        return eventMapper.mapToList(eventsList);
    }
    public eventDTO eventDetails(String name){
        Optional<Event> eventOptional = eventRepo.findEventByNAME(name);
        if(eventOptional.isEmpty()){
            throw new NoSuchEventException("Invalid event name");
        }
        return eventMapper.mapTo(eventOptional.get());
    }
    public String countEvents(){
        return String.valueOf(eventRepo.countEventsBy());
    }
    public List<eventDTO> getRecentEvents(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDay = now.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        List<Event> events = eventRepo.findEventsByCreatedAtAfter(firstDay);
        return eventMapper.mapToList(events);
    }
    public List<eventDTO> getEvents(){
        List<Event> events = eventRepo.findEventsBy();
        return eventMapper.mapToList(events);
    }
    private Event checkEvent(eventDTO eventDTO) {
        Optional<Event> eventOptional = eventRepo.findEventByNAME(eventDTO.getNAME());
        if (eventOptional.isEmpty()) {
            throw new NoSuchEventException("Invalid event name");
        }
        return eventOptional.get();
    }


}