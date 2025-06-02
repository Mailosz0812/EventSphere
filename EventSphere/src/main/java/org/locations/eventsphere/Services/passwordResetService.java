package org.locations.eventsphere.Services;

import DTOs.PasswordTokenDTO;
import DTOs.userRegisterDTO;
import org.locations.eventsphere.Entities.LoggedUser;
import org.locations.eventsphere.Entities.PasswordResetToken;
import org.locations.eventsphere.Exceptions.NoSuchTokenException;
import org.locations.eventsphere.Exceptions.NoSuchUserException;
import org.locations.eventsphere.Repositories.passwordResetRepository;
import org.locations.eventsphere.Repositories.userRepository;
import org.locations.eventsphere.mappers.Mapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class passwordResetService {
    private passwordResetRepository passResetRepo;
    private Mapper<PasswordResetToken, PasswordTokenDTO> mapper;
    private Mapper<LoggedUser,userRegisterDTO> userMapper;
    private userRepository userRepo;

    public passwordResetService(passwordResetRepository passResetRepo, Mapper<PasswordResetToken, PasswordTokenDTO> mapper, Mapper<LoggedUser, userRegisterDTO> userMapper, userRepository userRepo) {
        this.passResetRepo = passResetRepo;
        this.mapper = mapper;
        this.userMapper = userMapper;
        this.userRepo = userRepo;
    }
    public userRegisterDTO getUserByToken(String token){
        Optional<PasswordResetToken> passTokenOptional = passResetRepo.findPasswordResetTokenByTOKEN(token);
        if(passTokenOptional.isEmpty()){
            throw new NoSuchTokenException("Invalid token");
        }
        LoggedUser user = passTokenOptional.get().getUSERID();
        return userMapper.mapTo(user);
    }
    public PasswordTokenDTO getToken(String token){
        Optional<PasswordResetToken> passTokenOptional = passResetRepo.findPasswordResetTokenByTOKEN(token);
        if(passTokenOptional.isEmpty()){
            throw new NoSuchTokenException("Invalid token");
        }
        return mapper.mapTo(passTokenOptional.get());
    }
    public PasswordTokenDTO saveToken(PasswordTokenDTO passToken){
        Optional<LoggedUser> user = userRepo.findLoggedUserByMail(passToken.getMail());
        if(user.isEmpty()){
            throw new NoSuchUserException("Invalid email");
        }
        PasswordResetToken passTokenEntity = new PasswordResetToken();
        passTokenEntity.setUSERID(user.get());
        passTokenEntity.setTOKEN(passToken.getToken());
        passTokenEntity.setEXPIRE_DATE(passToken.getExpireDate());
        return mapper.mapTo(passResetRepo.save(passTokenEntity));
    }
    public void deleteToken(String token){
        Optional<PasswordResetToken> passTokenOptional = passResetRepo.findPasswordResetTokenByTOKEN(token);
        if(passTokenOptional.isEmpty()){
            throw new NoSuchTokenException("Invalid token");
        }
        passResetRepo.delete(passTokenOptional.get());
    }
}
