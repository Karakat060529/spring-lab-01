
package kz.iitu.springlab;

import jakarta.persistence.EntityManagerFactory;
import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookRepository;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@SpringBootTest
public class NPlusOneTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void compareQueries() {

        SessionFactory sessionFactory =
                entityManagerFactory.unwrap(SessionFactory.class);

        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);

        // WITHOUT JOIN FETCH
        entityManager.flush();
        entityManager.clear();
        statistics.clear();

        List<Book> books = repository.findAll();

        for (Book book : books) {
            if (book.getAuthor() != null) {
                book.getAuthor().getName();
            }
        }

        long withoutFetch = statistics.getPrepareStatementCount();

        System.out.println("WITHOUT JOIN FETCH: " + withoutFetch);

        // WITH JOIN FETCH
        statistics.clear();

        List<Book> booksWithAuthors =
                repository.findAllWithAuthors();

        for (Book book : booksWithAuthors) {
            if (book.getAuthor() != null) {
                book.getAuthor().getName();
            }
        }

        long withFetch = statistics.getPrepareStatementCount();

        System.out.println("WITH JOIN FETCH: " + withFetch);
    }

}
