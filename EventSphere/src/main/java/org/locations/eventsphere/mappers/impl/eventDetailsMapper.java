package org.locations.eventsphere.mappers.impl;

import DTOs.eventDetailsDTO;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class eventDetailsMapper implements Mapper<Event, eventDetailsDTO> {

    private ModelMapper modelMapper;

    public eventDetailsMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<eventDetailsDTO> mapToList(List<Event> events) {
        List<eventDetailsDTO> eventDetailsDTOS = new ArrayList<>();
        for (Event event : events) {
            eventDetailsDTOS.add(mapTo(event));
        }
        return eventDetailsDTOS;
    }

    @Override
    public List<Event> mapFromList(List<eventDetailsDTO> eventDetailsDTOS) {
        List<Event> eventList = new ArrayList<>();
        for (eventDetailsDTO eventDetailsDTO : eventDetailsDTOS) {
            eventList.add(mapFrom(eventDetailsDTO));
        }
        return eventList;
    }

    @Override
    public eventDetailsDTO mapTo(Event event) {
        return modelMapper.map(event, eventDetailsDTO.class);
    }

    @Override
    public Event mapFrom(eventDetailsDTO eventDetailsDTO) {
        return modelMapper.map(eventDetailsDTO,Event.class);
    }
}
