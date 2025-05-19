package org.locations.eventspheremvc.Exceptions;

import DTOs.messageDTO;
import lombok.Getter;

@Getter
public class EmailException extends EventSphereMVCException{
    private messageDTO target;
    public EmailException(String message,messageDTO target) {
        super(message);
        this.target = target;
    }
}
