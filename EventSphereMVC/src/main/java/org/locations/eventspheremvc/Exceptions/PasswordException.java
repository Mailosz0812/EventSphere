package org.locations.eventspheremvc.Exceptions;

import DTOs.userRegisterDTO;
import lombok.Getter;

@Getter
public class PasswordException extends EventSphereMVCException {
  private userRegisterDTO target;
  public PasswordException(String message,userRegisterDTO target) {
    super(message);
    this.target = target;
  }
}
