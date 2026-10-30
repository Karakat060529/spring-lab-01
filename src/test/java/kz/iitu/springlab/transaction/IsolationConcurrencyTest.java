
package kz.iitu.springlab.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IsolationConcurrencyTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private BigDecimal readBalance() {
        return jdbc.queryForObject(
                "SELECT balance FROM accounts WHERE id = 1",
                BigDecimal.class
        );
    }

    private BigDecimal[] compareIsolation(int isolation) throws Exception {
        TransactionTemplate transaction =
                new TransactionTemplate(transactionManager);
        transaction.setIsolationLevel(isolation);

        CountDownLatch firstRead = new CountDownLatch(1);
        CountDownLatch updated = new CountDownLatch(1);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<BigDecimal[]> result = executor.submit(() ->
                    transaction.execute(status -> {
                        BigDecimal before = readBalance();
                        firstRead.countDown();

                        try {
                            if (!updated.await(10, TimeUnit.SECONDS)) {
                                throw new IllegalStateException("Update timeout");
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException(e);
                        }

                        BigDecimal after = readBalance();
                        return new BigDecimal[]{before, after};
                    })
            );

            assertTrue(firstRead.await(10, TimeUnit.SECONDS));

            jdbc.update(
                    "UPDATE accounts SET balance = balance + 100 WHERE id = 1"
            );
            updated.countDown();

            return result.get(10, TimeUnit.SECONDS);
        } finally {
            updated.countDown();
            executor.shutdownNow();
        }
    }

    @Test
    void readCommittedSeesUpdatedValue() throws Exception {
        BigDecimal[] values = compareIsolation(
                TransactionDefinition.ISOLATION_READ_COMMITTED
        );

        assertEquals(0,
                values[1].compareTo(values[0].add(new BigDecimal("100"))));
    }

    @Test
    void repeatableReadKeepsOriginalValue() throws Exception {
        BigDecimal[] values = compareIsolation(
                TransactionDefinition.ISOLATION_REPEATABLE_READ
        );

        assertEquals(0, values[0].compareTo(values[1]));
    }
}
