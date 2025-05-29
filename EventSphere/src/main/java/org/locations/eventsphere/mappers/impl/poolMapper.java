package org.locations.eventsphere.mappers.impl;

import DTOs.poolDTO;
import org.locations.eventsphere.Entities.Pool;
import org.locations.eventsphere.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class poolMapper implements Mapper<Pool, poolDTO> {
    private final ModelMapper mapper;

    public poolMapper(ModelMapper mapper) {
        this.mapper = mapper;
        this.mapper.addMappings(new PropertyMap<poolDTO, Pool>() {
            @Override
            protected void configure() {
                skip(destination.getEvent());
                map(source.getPoolType(), destination.getPoolName());
            }
        });
        this.mapper.addMappings(new PropertyMap<Pool, poolDTO>() {
            @Override
            protected void configure() {
                map(source.getPoolName(),destination.getPoolType());
            }
        });
    }

    @Override
    public List<poolDTO> mapToList(List<Pool> pools) {
        List<poolDTO> poolDTOList = new ArrayList<>();
        for (Pool pool : pools) {
            poolDTOList.add(mapTo(pool));
        }
        return poolDTOList;
    }

    @Override
    public List<Pool> mapFromList(List<poolDTO> poolDTOS) {
        List<Pool> poolList = new ArrayList<>();
        for (poolDTO poolDTO : poolDTOS) {
            poolList.add(mapFrom(poolDTO));
        }
        return poolList;
    }

    @Override
    public poolDTO mapTo(Pool pool) {
        return mapper.map(pool, poolDTO.class);
    }

    @Override
    public Pool mapFrom(poolDTO poolDTO) {
        return mapper.map(poolDTO, Pool.class);
    }
}
