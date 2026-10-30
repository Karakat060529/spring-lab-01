
package kz.iitu.springlab.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final AuditService auditService;

    public AccountService(AccountRepository repository,
                          AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId).orElseThrow();
        Account to = repository.findById(toId).orElseThrow();

        from.debit(amount);
        to.credit(amount);

        repository.save(from);
        repository.save(to);
    }

    @Transactional
    public void transferWithFailure(Long fromId, Long toId,
                                    BigDecimal amount) {
        Account from = repository.findById(fromId).orElseThrow();
        Account to = repository.findById(toId).orElseThrow();

        from.debit(amount);
        repository.saveAndFlush(from);

        throw new RuntimeException("Simulated transaction failure");
    }

    @Transactional
    public void transferWithRequiredAudit() {
        auditService.logRequired("REQUIRED audit entry");

        throw new RuntimeException("Simulated failure");
    }

    @Transactional
    public void transferWithRequiresNewAudit() {
        auditService.logRequiresNew("REQUIRES_NEW audit entry");

        throw new RuntimeException("Simulated failure");
    }
}
