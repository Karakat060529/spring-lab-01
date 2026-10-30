
package kz.iitu.springlab.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void logRequired(String message) {
        repository.save(new AuditLog(message));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logRequiresNew(String message) {
        repository.save(new AuditLog(message));
    }
}
