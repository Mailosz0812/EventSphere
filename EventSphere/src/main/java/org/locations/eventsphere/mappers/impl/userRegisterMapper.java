package org.locations.eventsphere.mappers.impl;
import DTOs.userRegisterDTO;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class userRegisterMapper implements Mapper<LoggedUser, userRegisterDTO> {
    private ModelMapper mapper;
    public userRegisterMapper(ModelMapper mapper) {
        this.mapper = mapper;
        this.mapper.addMappings(new PropertyMap<userRegisterDTO,LoggedUser>() {
            @Override
            protected void configure() {
                skip(destination.getRole());
            }
        });
        this.mapper.addMappings(new PropertyMap<LoggedUser,userRegisterDTO>() {
            @Override
            protected void configure() {
                skip(destination.getRole());
            }
        });
    }
    @Override
    public List<userRegisterDTO> mapToList(List<LoggedUser> aList) {
        List<userRegisterDTO> userRegisterDTOS = new ArrayList<>();
        for (LoggedUser loggedUser : aList) {
            userRegisterDTOS.add(mapper.map(loggedUser,userRegisterDTO.class));
        }
        return userRegisterDTOS;
    }
    @Override
    public List<LoggedUser> mapFromList(List<userRegisterDTO> bList) {
        List<LoggedUser> loggedUsers = new ArrayList<>();
        for (userRegisterDTO userRegisterDTO : bList) {
            loggedUsers.add(mapper.map(userRegisterDTO, LoggedUser.class));
        }
        return loggedUsers;
    }
    @Override
    public userRegisterDTO mapTo(LoggedUser loggedUser) {
        userRegisterDTO user =  mapper.map(loggedUser, userRegisterDTO.class);
        user.setRole(loggedUser.getRole().getNAME());
        return user;
    }
    @Override
    public LoggedUser mapFrom(userRegisterDTO userRegisterDTO) {
        return mapper.map(userRegisterDTO,LoggedUser.class);
    }
}
