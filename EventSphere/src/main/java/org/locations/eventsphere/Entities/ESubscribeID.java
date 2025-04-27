package org.locations.eventsphere.Entities;

import lombok.Data;

@Data
class ESubscribeID {
    private Event event;
    private LoggedUser loggedUser;
}
