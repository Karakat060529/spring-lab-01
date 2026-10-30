
package kz.iitu.springlab.transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OptimisticLockingTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void detectsConcurrentModification() {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);

        Account stale = tx.execute(status ->
                entityManager.find(Account.class, 1L)
        );

        assertNotNull(stale);

        tx.executeWithoutResult(status -> {
            Account current = entityManager.find(Account.class, 1L);
            current.credit(new BigDecimal("100.00"));
        });

        assertThrows(OptimisticLockException.class, () ->
                tx.executeWithoutResult(status -> {
                    Account managed = entityManager.merge(stale);
                    entityManager.flush();
                })
        );
    }
}

