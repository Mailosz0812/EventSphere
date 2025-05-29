package org.locations.eventsphere.Services;

import DTOs.poolDTO;
import DTOs.poolDetailsDTO;
import jakarta.transaction.Transactional;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.Pool;
import org.locations.eventsphere.Exceptions.NoSuchEventException;
import org.locations.eventsphere.Exceptions.NoSuchPoolException;
import org.locations.eventsphere.Repositories.eventRepository;
import org.locations.eventsphere.Repositories.poolRepository;
import org.locations.eventsphere.mappers.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class poolService {
    private final poolRepository poolRepo;
    private final eventRepository eventRepo;
    private final Mapper<Pool,poolDTO> poolMapper;

    public poolService(poolRepository poolRepo, eventRepository eventRepo, Mapper<Pool, poolDTO> poolMapper) {
        this.poolRepo = poolRepo;
        this.eventRepo = eventRepo;
        this.poolMapper = poolMapper;
    }

    public poolDTO createPool(poolDTO poolDTO){
        Event event = getEvent(poolDTO.getEventName());
        Pool pool = poolMapper.mapFrom(poolDTO);
        pool.setEvent(event);
        return poolMapper.mapTo(poolRepo.save(pool));
    }
    public List<poolDetailsDTO> getPools(String eName){
        Event event = getEvent(eName);
        List<Pool> pools = poolRepo.findPoolsByEvent(event);
        List<poolDetailsDTO> poolDetailsDTOS = new ArrayList<>();
        for (Pool pool : pools) {
            System.out.println(pool.getPoolID());
            poolDetailsDTOS.add(new poolDetailsDTO(
                    pool.getPoolID(),
                    pool.getPoolName(),
                    pool.getPrice(),
                    0,
                    pool.getTicketCount()));
        }
        return poolDetailsDTOS;
    }

    @Transactional
    public void deletePool(String eventName,Long poolID){
        Event event = getEvent(eventName);
        Pool pool = getPool(poolID);
        poolRepo.deletePoolByEventAndPoolID(event,pool.getPoolID());
    }
    @Transactional
    public void updatePool(poolDTO poolDTO){
        Pool pool = getPool(poolDTO.getPoolID());
        pool.setPrice(poolDTO.getPrice());
        Integer ticketCount = pool.getTicketCount();
        ticketCount += poolDTO.getTicketCount();
        pool.setTicketCount(ticketCount);
        poolRepo.save(pool);
    }
    public poolDTO getPoolById(Long poolID){
        Pool pool = getPool(poolID);
        return poolMapper.mapTo(pool);
    }
    private Event getEvent(String eventName){
        Optional<Event> optEvent = eventRepo.findEventByNAME(eventName);
        if(optEvent.isEmpty()){
            throw new NoSuchEventException("Event not found");
        }
        return optEvent.get();
    }
    private Pool getPool(Long poolID){
        Optional<Pool> optPool = poolRepo.findPoolByPoolID(poolID);
        if(optPool.isEmpty()){
            throw new NoSuchPoolException("Pool not found");
        }
        return optPool.get();
    }
}
