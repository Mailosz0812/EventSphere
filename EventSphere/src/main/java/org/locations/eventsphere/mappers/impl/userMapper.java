package org.locations.eventsphere.mappers.impl;


import DTOs.userDTO;
import DTOs.userRegisterDTO;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class userMapper implements Mapper<LoggedUser, userDTO> {

    private ModelMapper modelMapper;

    public userMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.modelMapper.addMappings(new PropertyMap<userDTO,LoggedUser>() {
            @Override
            protected void configure() {
                skip(destination.getROLE());
            }
        });
        this.modelMapper.addMappings(new PropertyMap<LoggedUser,userDTO>() {
            @Override
            protected void configure() {
                skip(destination.getROLE());
            }
        });
    }
    @Override
    public List<userDTO> mapToList(List<LoggedUser> aList) {
        List<userDTO> userDTOS = new ArrayList<>();
        for (LoggedUser loggedUser : aList) {
            userDTOS.add(mapTo(loggedUser));
        }
        return userDTOS;
    }

    @Override
    public List<LoggedUser> mapFromList(List<userDTO> bList) {
        List<LoggedUser> loggedUsers = new ArrayList<>();
        for (userDTO userDTO : bList) {
            loggedUsers.add(modelMapper.map(userDTO, LoggedUser.class));
        }
        return loggedUsers;
    }

    @Override
    public userDTO mapTo(LoggedUser loggedUser) {
        userDTO user = modelMapper.map(loggedUser, userDTO.class);
        user.setROLE(loggedUser.getROLE().getNAME());
        return user;
    }

    @Override
    public LoggedUser mapFrom(userDTO userDTO) {
        return modelMapper.map(userDTO, LoggedUser.class);
    }
}
