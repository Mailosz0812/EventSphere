package org.locations.eventsphere.Services;
import DTOs.eventDTO;
import DTOs.userRegisterDTO;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.EventCategory;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.Role;
import org.locations.eventsphere.Exceptions.EventAlreadyExistsException;
import org.locations.eventsphere.Exceptions.NoSuchCategoryException;
import org.locations.eventsphere.Exceptions.NoSuchEventException;
import org.locations.eventsphere.Exceptions.NoSuchUserException;
import org.locations.eventsphere.Repositories.eventCategoryRepository;
import org.locations.eventsphere.Repositories.eventRepository;
import org.locations.eventsphere.Repositories.roleRepository;
import org.locations.eventsphere.Repositories.userRepository;
import org.locations.eventsphere.mappers.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class eventService {
    private eventRepository eventRepo;
    private userRepository userRepo;
    private roleRepository roleRepo;
    private Mapper<Event, eventDTO> eventMapper;
    private eventCategoryRepository categoryRepo;
    public eventService(eventRepository eventRepo, userRepository userRepo, roleRepository roleRepo,
                        Mapper<Event, eventDTO> eventMapper, eventCategoryRepository categoryRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.eventMapper = eventMapper;
        this.categoryRepo = categoryRepo;
    }
    public eventDTO organizeEvent(eventDTO eventDTO){
        Optional<Event> eventOptional = eventRepo.findEventByNAME(eventDTO.getNAME());
        if(eventOptional.isPresent()){
            throw new EventAlreadyExistsException("Event named: "+eventDTO.getNAME()+" already exists");
        }
        EventCategory category = checkCategory(eventDTO);
        Event newEvent = eventMapper.mapFrom(eventDTO);
        newEvent.setEVENTCATEGORY(category);
        eventRepo.save(newEvent);
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
        updateEvent.setEVENTCATEGORY(checkCategory(eventDTO));
        updateEvent.setDESCRIPTION(eventDTO.getDESCRIPTION());
        updateEvent.setEVENTDATE(eventDTO.getEVENTDATE());
        updateEvent.setLOCATION(eventDTO.getLOCATION());
        updateEvent.setNAME(eventDTO.getNAME());
        updateEvent.setTICKETCOUNT(eventDTO.getTICKETCOUNT());
        return eventMapper.mapTo(eventRepo.save(updateEvent));
    }

    private Event checkEvent(eventDTO eventDTO) {
        Optional<Event> eventOptional = eventRepo.findEventByNAME(eventDTO.getNAME());
        if(eventOptional.isEmpty()){
            throw new NoSuchEventException("No such event named: "+ eventDTO.getNAME());
        }
        return eventOptional.get();
    }

    public List<Event> findEventsByUser(String username){
        Optional<LoggedUser> loggedUserOptional = userRepo.findLoggedUserByUSERNAME(username);
        if(loggedUserOptional.isEmpty()){
            throw new NoSuchUserException("No user named: "+username);
        }
        return eventRepo.findEventsByUser(loggedUserOptional.get());
    }

    public List<Event> findEventsByOrganizer(String username){
        Role role = roleRepo.findRoleByNAME("ORGANIZER").get();
        Optional<LoggedUser> organizerOptional = userRepo.findLoggedUserByUSERNAMEAndROLE(username,role);
        if(organizerOptional.isEmpty()){
            throw new NoSuchUserException("No organizer named: "+username);
        }
        return eventRepo.findEventsByOrganizer(organizerOptional.get());
    }

}