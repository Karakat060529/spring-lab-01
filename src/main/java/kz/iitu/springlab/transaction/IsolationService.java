
package kz.iitu.springlab.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

@Service
public class IsolationService {

    private final AccountRepository repository;

    public IsolationService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BigDecimal readCommitted(Long accountId) {
        return repository.findById(accountId)
                .orElseThrow()
                .getBalance();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BigDecimal repeatableRead(Long accountId) {
        return repository.findById(accountId)
                .orElseThrow()
                .getBalance();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateBalance(Long accountId,
                              BigDecimal amount) {
        Account account = repository.findById(accountId)
                .orElseThrow();

        account.credit(amount);
        repository.saveAndFlush(account);
    }
}
