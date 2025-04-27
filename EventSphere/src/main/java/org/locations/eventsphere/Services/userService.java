package org.locations.eventsphere.Services;


import DTOs.userDTO;
import DTOs.userRegisterDTO;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.Role;
import org.locations.eventsphere.Exceptions.NoSuchEventException;
import org.locations.eventsphere.Exceptions.NoSuchRoleException;
import org.locations.eventsphere.Exceptions.NoSuchUserException;
import org.locations.eventsphere.Exceptions.UserAlreadyExistsException;
import org.locations.eventsphere.Repositories.roleRepository;
import org.locations.eventsphere.Repositories.userRepository;
import org.locations.eventsphere.Repositories.eventRepository;
import org.locations.eventsphere.mappers.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class userService {
    private userRepository userRepo;
    private roleRepository roleRepo;
    private eventRepository eventRepo;
    private Mapper<LoggedUser, userDTO> userMapper;
    private Mapper<LoggedUser, userRegisterDTO> userRegisterMapper;
    @Autowired
    public userService(userRepository userRepo, roleRepository roleRepo, Mapper<LoggedUser,userDTO> userMapper, eventRepository eventRepository, Mapper<LoggedUser,userRegisterDTO> userRegisterMapper){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.userMapper = userMapper;
        this.eventRepo = eventRepository;
        this.userRegisterMapper = userRegisterMapper;
    }
    public boolean createUser(userRegisterDTO userDTO){
        Optional<LoggedUser> optionalLoggedUser = userRepo.findLoggedUserByUSERNAMEOrMAIL(userDTO.getUSERNAME(),userDTO.getMAIL());
        if(optionalLoggedUser.isEmpty()){
            LoggedUser newUser = userRegisterMapper.mapFrom(userDTO);
            Role role = checkRole(userDTO.getROLE());
            newUser.setROLE(role);
            userRepo.save(newUser);
            return true;
        }
        throw new UserAlreadyExistsException("User with this username or mail already exists");
    }
    public List<userDTO> getUsersByRole(String roleString){
        Role role = checkRole(roleString);
        List<LoggedUser> loggedUsers = userRepo.findLoggedUsersByROLE(role);
        return userMapper.mapToList(loggedUsers);
    }
    private Role checkRole(String roleString) {
        Optional<Role> role = roleRepo.findRoleByNAME(roleString);
        if(role.isEmpty()){
            throw new NoSuchRoleException("No such role as:" + roleString);
        }
        return role.get();
    }
    public List<userDTO> findOrganizersByEvent(String eventName){
        Event event = getEvent(eventName);
        List<LoggedUser> organizers = userRepo.findOrganizerByEvent(event);
        return userMapper.mapToList(organizers);
    }

    private Event getEvent(String eventName) {
        Optional<Event> eventOptional = eventRepo.findEventByNAME(eventName);
        if(eventOptional.isEmpty()){
            throw new NoSuchEventException("No such event: "+ eventName);
        }
        return eventOptional.get();
    }
    public List<userDTO> findUsersByEvent(String eventName){
        Event event = getEvent(eventName);
        List<LoggedUser> userList = userRepo.findUsersByEvent(event);
        return userMapper.mapToList(userList);
    }
    public userDTO updateUser(userDTO userDTO){
        Optional<LoggedUser> loggedUserOptional = userRepo.findLoggedUserByMAIL(userDTO.getMAIL());
        if(loggedUserOptional.isEmpty()){
            throw new NoSuchUserException("No such user with mail: "+userDTO.getMAIL());
        }
        LoggedUser updateUser = loggedUserOptional.get();
        updateUser.setNAME(userDTO.getNAME());
        updateUser.setSURNAME(userDTO.getSURNAME());
        updateUser.setDESCRIPTION(userDTO.getDESCRIPTION());
        return userMapper.mapTo(userRepo.save(updateUser));
    }
}
