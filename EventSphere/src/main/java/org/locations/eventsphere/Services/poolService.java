package org.locations.eventsphere.Services;

import DTOs.poolDTO;
import DTOs.poolDetailsDTO;
import jakarta.transaction.Transactional;
import org.locations.eventsphere.Entities.Event;
import org.locations.eventsphere.Entities.Pool;
import org.locations.eventsphere.Exceptions.EventException;
import org.locations.eventsphere.Exceptions.NoSuchEventException;
import org.locations.eventsphere.Exceptions.NoSuchPoolException;
import org.locations.eventsphere.Exceptions.PoolException;
import org.locations.eventsphere.Repositories.eventRepository;
import org.locations.eventsphere.Repositories.poolRepository;
import org.locations.eventsphere.Repositories.ticketRepository;
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
    private final ticketRepository ticketRepo;

    public poolService(poolRepository poolRepo, eventRepository eventRepo, Mapper<Pool, poolDTO> poolMapper, ticketRepository ticketRepo) {
        this.poolRepo = poolRepo;
        this.eventRepo = eventRepo;
        this.poolMapper = poolMapper;
        this.ticketRepo = ticketRepo;
    }
    public poolDTO createPool(poolDTO poolDTO){
        Event event = getEvent(poolDTO.getEventName());
        if("DONE".equals(event.getEventStatus())){
            throw new EventException("Event is already performed");
        }
        Pool pool = poolMapper.mapFrom(poolDTO);
        pool.setPoolStatus("ACTIVE");
        pool.setEvent(event);
        return poolMapper.mapTo(poolRepo.save(pool));
    }
    public List<poolDetailsDTO> getPools(String eName){
        Event event = getEvent(eName);
        List<Pool> pools = poolRepo.findPoolsByEventAndPoolStatus(event,"ACTIVE");
        List<poolDetailsDTO> poolDetailsDTOS = new ArrayList<>();
        for (Pool pool : pools) {
            Long poolID = pool.getPoolID();
            Integer count = ticketRepo.countTicketsByPoolPoolID(poolID);
            int ticketCount = pool.getTicketCount() + count;
            poolDetailsDTOS.add(new poolDetailsDTO(
                    poolID,
                    pool.getPoolName(),
                    pool.getPrice(),
                    count,
                    ticketCount));
        }
        return poolDetailsDTOS;
    }
    @Transactional
    public void deletePool(Long poolID){
        Pool pool = getPool(poolID);
        Integer i = ticketRepo.countTicketsByPoolPoolID(poolID);
        if(i == null || i == 0) {
            poolRepo.deletePoolByPoolID(pool.getPoolID());
        }else{
            throw new PoolException("Pool already in use");
        }
    }
    @Transactional
    public void updatePool(poolDTO poolDTO){
        Pool pool = getPool(poolDTO.getPoolID());
        if("DONE".equals(pool.getEvent().getEventStatus())){
            throw new EventException("Event is already performed");
        }
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
        Optional<Event> optEvent = eventRepo.findEventByName(eventName);
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
