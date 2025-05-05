package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface passwordResetRepository extends JpaRepository<PasswordResetToken,Long> {
    Optional<PasswordResetToken> findPasswordResetTokenByTOKEN(String token);
}
