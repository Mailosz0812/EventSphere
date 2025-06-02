package org.locations.eventsphere.Configs;

import com.fasterxml.jackson.databind.ObjectMapper;
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
