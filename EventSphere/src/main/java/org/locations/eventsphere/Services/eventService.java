package org.locations.eventsphere.Services;
import DTOs.eventDTO;
import DTOs.preCreatedUserDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class eventService {
    private eventRepository eventRepo;
    private userRepository userRepo;
    private Mapper<Event, eventDTO> eventMapper;
    private eventCategoryRepository categoryRepo;
    private eventOrganizeRepository eOrganizeRepository;

    public eventService(eventRepository eventRepo, userRepository userRepo, Mapper<Event, eventDTO> eventMapper, eventCategoryRepository categoryRepo, eventOrganizeRepository eOrganizeRepository) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.eventMapper = eventMapper;
        this.categoryRepo = categoryRepo;
        this.eOrganizeRepository = eOrganizeRepository;
    }

    public eventDTO organizeEvent(eventDTO eventDTO){
        Optional<Event> eventOptional = eventRepo.findEventByNameAndEventStatus(eventDTO.getNAME(),"ACTIVE");
        if(eventOptional.isPresent()){
            throw new EventAlreadyExistsException("Event named: "+eventDTO.getNAME()+" already exists");
        }
        Optional<LoggedUser> organizerOptional = userRepo.findLoggedUserByMail(eventDTO.getOrganizerMail());
        if(organizerOptional.isEmpty()){
            throw new NoSuchUserException("Organizer not found");
        }
        EventCategory category = checkCategory(eventDTO);
        Event newEvent = eventMapper.mapFrom(eventDTO);
        newEvent.setEventStatus("ACTIVE");
        newEvent.setEventCategory(category);
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
            throw new NoSuchCategoryException("Category not found");
        }
        return categoryOptional.get();
    }

    public eventDTO updateEvent(eventDTO eventDTO){
        Event updateEvent = checkEvent(eventDTO.getNAME());
        updateEvent.setDescription(eventDTO.getDESCRIPTION());
        updateEvent.setEventDate(eventDTO.getEVENTDATE());
        updateEvent.setLocation(eventDTO.getLOCATION());
        updateEvent.setName(eventDTO.getNAME());
        return eventMapper.mapTo(eventRepo.save(updateEvent));
    }

    public List<eventDTO> getEventsByOrganizer(String mail){
        Optional<LoggedUser> optionalUser = userRepo.findLoggedUserByMail(mail);
        if(optionalUser.isEmpty()){
            throw new NoSuchUserException("Something went wrong");
        }
        List<Event> eventsList = eventRepo.findEventsByOrganizerAndEventStatus(optionalUser.get(),"ACTIVE");
        return eventMapper.mapToList(eventsList);
    }
    public eventDTO eventDetails(String name){
        Optional<Event> eventOptional = eventRepo.findEventByName(name);
        if(eventOptional.isEmpty()){
            throw new NoSuchEventException("Event not found");
        }
        return eventMapper.mapTo(eventOptional.get());
    }
    public String countEvents(){
        return String.valueOf(eventRepo.countEventsByEventStatus("ACTIVE"));
    }
    public List<eventDTO> getRecentEvents(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDay = now.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        List<Event> events = eventRepo.findEventsByCreatedAtAfter(firstDay);
        return eventMapper.mapToList(events);
    }
    public List<eventDTO> getEventsByCategory(String category){
        Optional<EventCategory> optECategory = categoryRepo.findEventCategoryByNAME(category);
        if(optECategory.isEmpty()){
            return eventMapper.mapToList(eventRepo.findEventsByEventStatus("ACTIVE"));
        }
        List<Event> events = eventRepo.findEventsByEventCategoryAndEventStatus(optECategory.get(),"ACTIVE");
        return eventMapper.mapToList(events);
    }
    public List<eventDTO> getEventsByName(String name){
        return eventMapper.mapToList(eventRepo.findEventsByNameContainsIgnoreCaseAndEventStatus(name,"ACTIVE"));
    }
    public preCreatedUserDTO getOrganizerByEvent(String eventName){
        checkEvent(eventName);
        Optional<LoggedUser> organizer = userRepo.findOrganizerByEvent(eventName);
        return getPreCreatedUserDTO(organizer);
    }

    private preCreatedUserDTO getPreCreatedUserDTO(Optional<LoggedUser> organizer) {
        preCreatedUserDTO organizerDTO = new preCreatedUserDTO();
        if(organizer.isPresent()){
            LoggedUser user = organizer.get();
            System.out.println(user.getMail() + " " + user.getUsername());
            organizerDTO.setMail(user.getMail());
            organizerDTO.setUsername(user.getUsername());
        }
        return organizerDTO;
    }

    private Event checkEvent(String eventName) {
        Optional<Event> eventOptional = eventRepo.findEventByNameAndEventStatus(eventName,"ACTIVE");
        if (eventOptional.isEmpty()) {
            throw new NoSuchEventException("Event not found");
        }
        return eventOptional.get();
    }


}