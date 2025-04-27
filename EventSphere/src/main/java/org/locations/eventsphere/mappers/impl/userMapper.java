package org.locations.eventsphere.mappers.impl;


import DTOs.userDTO;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class userMapper implements Mapper<LoggedUser, userDTO> {

    private ModelMapper modelMapper;

    public userMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }
    @Override
    public List<userDTO> mapToList(List<LoggedUser> aList) {
        List<userDTO> userDTOS = new ArrayList<>();
        for (LoggedUser loggedUser : aList) {
            userDTOS.add(modelMapper.map(loggedUser,userDTO.class));
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
        return modelMapper.map(loggedUser, userDTO.class);
    }

    @Override
    public LoggedUser mapFrom(userDTO userDTO) {
        return modelMapper.map(userDTO, LoggedUser.class);
    }
}
