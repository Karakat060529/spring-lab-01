
package kz.iitu.springlab.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionPropagationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuditLogRepository auditRepository;

    @BeforeEach
    void clearAudit() {
        auditRepository.deleteAll();
    }

    @Test
    void requiredAuditRollsBack() {
        assertThrows(RuntimeException.class, () ->
                accountService.transferWithRequiredAudit());

        assertEquals(0, auditRepository.count());
    }

    @Test
    void requiresNewAuditSurvives() {
        assertThrows(RuntimeException.class, () ->
                accountService.transferWithRequiresNewAudit());

        assertEquals(1, auditRepository.count());
    }
}

