package org.locations.eventsphere.mappers.impl;

import DTOs.eventDTO;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class eventMapper implements Mapper<Event, eventDTO> {
    private ModelMapper mapper;
    public eventMapper(ModelMapper mapper) {
        this.mapper = mapper;
        this.mapper.addMappings(new PropertyMap<Event,eventDTO>() {
            @Override
            protected void configure() {
                skip(destination.getCATEGORY());
            }
        });
        this.mapper.addMappings(new PropertyMap<eventDTO,Event>() {
            @Override
            protected void configure() {
                skip(destination.getEVENTCATEGORY());
            }
        });
    }
    @Override
    public List<eventDTO> mapToList(List<Event> aList) {
        List<eventDTO> eventDTOS = new ArrayList<>();
        for (Event event : aList) {
            eventDTOS.add(mapper.map(event, eventDTO.class));
        }
        return eventDTOS;
    }
    @Override
    public List<Event> mapFromList(List<eventDTO> bList) {
        List<Event> events = new ArrayList<>();
        for (eventDTO eventDTO : bList) {
            events.add(mapper.map(eventDTO, Event.class));
        }
        return events;
    }
    @Override
    public eventDTO mapTo(Event event) {
        return mapper.map(event, eventDTO.class);
    }
    @Override
    public Event mapFrom(eventDTO eventDTO) {
        return mapper.map(eventDTO,Event.class);
    }
}
