package org.locations.eventsphere.Services;

import DTOs.imageEventDTO;
import jakarta.transaction.Transactional;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.EventImage;
import org.locations.eventsphere.Exceptions.EventImageAlreadyExists;
import org.locations.eventsphere.Exceptions.NoSuchEventException;
import org.locations.eventsphere.Exceptions.eventImageProcessingException;
import org.locations.eventsphere.Repositories.eventRepository;
import org.locations.eventsphere.Repositories.imageRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class imageService {
    private final imageRepository imageRepo;
    private final eventRepository eventRepo;

    public imageService(imageRepository imageRepo, eventRepository eventRepo) {
        this.imageRepo = imageRepo;
        this.eventRepo = eventRepo;
    }
    public imageEventDTO addImage(imageEventDTO iEventDTO) {
        Event event = getEvent(iEventDTO.getEName());
        Optional<EventImage> optImage = imageRepo.findEventImageByEvent(event);
        if(optImage.isPresent()){
            throw new EventImageAlreadyExists("Image already added");
        }
        EventImage iEvent = new EventImage();
        iEvent.setEvent(event);
        iEvent.setAltText(iEventDTO.getAltText());
        iEvent.setBlob(iEventDTO.getImageBytes());
        event.setEventImage(iEvent);
        imageRepo.save(iEvent);
        eventRepo.save(event);
        return iEventDTO;
    }
    public imageEventDTO getImageByEvent(String eName){
        Event event = getEvent(eName);
        Optional<EventImage> optImage = imageRepo.findEventImageByEvent(event);
        if(optImage.isEmpty()){
            throw new eventImageProcessingException("Image not found");
        }
        EventImage iEvent = optImage.get();
        imageEventDTO iEventDTO = new imageEventDTO();
        iEventDTO.setAltText(iEvent.getAltText());
        iEventDTO.setEName(iEventDTO.getEName());
        iEventDTO.setImageBytes(iEvent.getBlob());
        return iEventDTO;
    }
    private Event getEvent(String eName) {
        Optional<Event> optEvent = eventRepo.findEventByNAME(eName);
        if(optEvent.isEmpty()){
            throw new NoSuchEventException("Event not found");
        }
        return optEvent.get();
    }
    @Transactional
    public void deleteImage(String eName){
        Event event = getEvent(eName);
        Optional<EventImage> optImage = imageRepo.findEventImageByEvent(event);
        if(optImage.isEmpty()){
            throw new eventImageProcessingException("Image not found");
        }
        event.setEventImage(null);
        imageRepo.delete(optImage.get());
    }

}
