package org.locations.eventsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EventSphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventSphereApplication.class, args);
    }

}
