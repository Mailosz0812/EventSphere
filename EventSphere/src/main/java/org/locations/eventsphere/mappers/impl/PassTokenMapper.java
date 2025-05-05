package org.locations.eventsphere.mappers.impl;

import DTOs.PasswordTokenDTO;
import DTOs.userRegisterDTO;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.PasswordResetToken;
import org.locations.eventsphere.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PassTokenMapper implements Mapper<PasswordResetToken, PasswordTokenDTO> {
    private ModelMapper mapper;

    public PassTokenMapper(ModelMapper mapper) {
        this.mapper = mapper;
        this.mapper.addMappings(new PropertyMap<PasswordResetToken,PasswordTokenDTO>() {
            @Override
            protected void configure() {
                skip(destination.getMail());
            }
        });
    }

    @Override
    public List<PasswordTokenDTO> mapToList(List<PasswordResetToken> passwordResetTokens) {
        return List.of();
    }

    @Override
    public List<PasswordResetToken> mapFromList(List<PasswordTokenDTO> passwordTokenDTOS) {
        return List.of();
    }

    @Override
    public PasswordTokenDTO mapTo(PasswordResetToken passwordResetToken) {
        return mapper.map(passwordResetToken,PasswordTokenDTO.class);
    }

    @Override
    public PasswordResetToken mapFrom(PasswordTokenDTO passwordTokenDTO) {
        return mapper.map(passwordTokenDTO, PasswordResetToken.class);
    }
}
