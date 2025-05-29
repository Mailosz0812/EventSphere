package org.locations.eventsphere.Repositories;

import org.locations.eventsphere.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface paymentRepository extends JpaRepository<Payment,Long> {
}
