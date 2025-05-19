package org.locations.eventsphere.mappers.impl;

import DTOs.categoryDTO;
import org.locations.eventsphere.Entities.EventCategory;
import org.locations.eventsphere.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class categoryMapper implements Mapper<EventCategory, categoryDTO> {
    private ModelMapper mapper;

    public categoryMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<categoryDTO> mapToList(List<EventCategory> eventCategories) {
        List<categoryDTO> categoryDTOS = new ArrayList<>();
        for (EventCategory category : eventCategories) {
            categoryDTOS.add(mapTo(category));
        }
        return categoryDTOS;
    }

    @Override
    public List<EventCategory> mapFromList(List<categoryDTO> categoryDTOS) {
        List<EventCategory> categories = new ArrayList<>();
        for (categoryDTO category : categoryDTOS) {
            categories.add(mapFrom(category));
        }
        return categories;
    }

    @Override
    public categoryDTO mapTo(EventCategory eventCategory) {
        return mapper.map(eventCategory, categoryDTO.class);
    }

    @Override
    public EventCategory mapFrom(categoryDTO categoryDTO) {
        return mapper.map(categoryDTO,EventCategory.class);
    }
}
