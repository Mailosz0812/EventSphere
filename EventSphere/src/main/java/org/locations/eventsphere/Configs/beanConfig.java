package org.locations.eventsphere.Configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class beanConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
