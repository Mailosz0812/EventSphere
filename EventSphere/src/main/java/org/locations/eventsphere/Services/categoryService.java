package org.locations.eventsphere.Services;

import DTOs.categoryDTO;
import org.locations.eventsphere.Entities.EventCategory;
import org.locations.eventsphere.Repositories.eventCategoryRepository;
import org.locations.eventsphere.mappers.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class categoryService {
    private final eventCategoryRepository categoryRepo;
    private final Mapper<EventCategory,categoryDTO> categoryMapper;

    public categoryService(eventCategoryRepository categoryRepo, Mapper<EventCategory, categoryDTO> categoryMapper) {
        this.categoryRepo = categoryRepo;
        this.categoryMapper = categoryMapper;
    }

    public List<categoryDTO> getCategories(){
        List<EventCategory> categories = categoryRepo.findAll();
        return categoryMapper.mapToList(categories);
    }
}
