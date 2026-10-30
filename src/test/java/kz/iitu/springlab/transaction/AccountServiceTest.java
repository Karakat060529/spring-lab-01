
package kz.iitu.springlab.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountServiceTest {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Test
    void successfulTransfer() {
        service.transfer(1L, 2L, new BigDecimal("100.00"));

        assertEquals(0, new BigDecimal("900.00")
                .compareTo(repository.findById(1L).orElseThrow().getBalance()));

        assertEquals(0, new BigDecimal("600.00")
                .compareTo(repository.findById(2L).orElseThrow().getBalance()));
    }

    @Test
    void insufficientFunds() {
        assertThrows(IllegalStateException.class, () ->
                service.transfer(1L, 2L, new BigDecimal("2000.00")));

        assertEquals(0, new BigDecimal("1000.00")
                .compareTo(repository.findById(1L).orElseThrow().getBalance()));
    }

    @Test
    void rollbackOnFailure() {
        assertThrows(RuntimeException.class, () ->
                service.transferWithFailure(1L, 2L, new BigDecimal("100.00")));

        assertEquals(0, new BigDecimal("1000.00")
                .compareTo(repository.findById(1L).orElseThrow().getBalance()));
    }
}
