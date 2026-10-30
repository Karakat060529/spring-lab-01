
package kz.iitu.springlab.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(statements = {
        "UPDATE accounts SET balance = 1000.00 WHERE id = 1"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class IsolationTest {

    @Autowired
    private IsolationService isolationService;

    @Test
    void readCommittedReturnsBalance() {
        BigDecimal balance = isolationService.readCommitted(1L);

        assertEquals(0,
                new BigDecimal("1000.00").compareTo(balance));
    }

    @Test
    void repeatableReadReturnsBalance() {
        BigDecimal balance = isolationService.repeatableRead(1L);

        assertEquals(0,
                new BigDecimal("1000.00").compareTo(balance));
    }
}
