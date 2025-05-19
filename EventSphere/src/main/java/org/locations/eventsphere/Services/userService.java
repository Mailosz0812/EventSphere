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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
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
        List<LoggedUser> optionalLoggedUser = userRepo.findLoggedUserByUSERNAMEOrMAIL(userDTO.getUSERNAME(),userDTO.getMAIL());
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
            throw new NoSuchRoleException("Role not found");
        }
        return role.get();
    }

    public userDTO updateUser(userDTO userDTO){
        Optional<LoggedUser> loggedUserOptional = userRepo.findLoggedUserByMAIL(userDTO.getMAIL());
        if(loggedUserOptional.isEmpty()){
            throw new NoSuchUserException("User not found");
        }
        LoggedUser updateUser = loggedUserOptional.get();
        updateUser.setNAME(userDTO.getNAME());
        updateUser.setSURNAME(userDTO.getSURNAME());
        updateUser.setDESCRIPTION(userDTO.getDESCRIPTION());
        return userMapper.mapTo(userRepo.save(updateUser));
    }
    public userRegisterDTO updateUserRegister(userRegisterDTO userDTO){
        Optional<LoggedUser> loggedUserOptional = userRepo.findLoggedUserByMAIL(userDTO.getMAIL());
        if(loggedUserOptional.isEmpty()){
            throw new NoSuchUserException("User not found");
        }
        LoggedUser updateUser = loggedUserOptional.get();
        updateUser.setNAME(userDTO.getNAME());
        updateUser.setSURNAME(userDTO.getSURNAME());
        updateUser.setPASSWORD(userDTO.getPASSWORD());
        updateUser.setMAIL(userDTO.getMAIL());
        updateUser.setUSERNAME(userDTO.getUSERNAME());
        updateUser.setNON_LOCKED(userDTO.isNON_LOCKED());
        return userRegisterMapper.mapTo(userRepo.save(updateUser));
    }

    public userRegisterDTO getUserByMail(String mail){
        Optional<LoggedUser> optionalLoggedUser = userRepo.findLoggedUserByMAIL(mail);
        if(optionalLoggedUser.isEmpty()){
            throw new NoSuchUserException("User not found");
        }
        return userRegisterMapper.mapTo(optionalLoggedUser.get());
    }
    public userDTO getUserByUsername(String username){
        Optional<LoggedUser> optionalLoggedUser = userRepo.findLoggedUserByUSERNAME(username);
        if(optionalLoggedUser.isEmpty()){
            throw new NoSuchUserException("User not found");
        }
        return userMapper.mapTo(optionalLoggedUser.get());
    }
    public userDTO setBlockUser(String mail){
        Optional<LoggedUser> optionalLoggedUser = userRepo.findLoggedUserByMAIL(mail);
        if(optionalLoggedUser.isEmpty()){
            throw new NoSuchUserException("User not found");
        }
        LoggedUser loggedUser = optionalLoggedUser.get();
        boolean isLocked = loggedUser.isNON_LOCKED();
        loggedUser.setNON_LOCKED(!isLocked);
        return userMapper.mapTo(userRepo.save(loggedUser));
    }
    public List<userDTO> usersRegistered(){
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime firstDay = time.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        System.out.println(firstDay);
        List<LoggedUser> users = userRepo.findLoggedUsersByUSER_TIMESTAMP(firstDay);
        return userMapper.mapToList(users);
    }
    public String usersCount(String role){
        Role eRole = checkRole(role);
        return String.valueOf(userRepo.countLoggedUserByROLE(eRole));
    }
}
