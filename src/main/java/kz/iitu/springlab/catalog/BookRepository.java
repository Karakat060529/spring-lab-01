
package kz.iitu.springlab.catalog;

import kz.iitu.springlab.web.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface BookRepository extends
        JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    @EntityGraph(attributePaths = "author")
    Page<Book> findAll(Pageable pageable);

    List<Book> findByGenre(Genre genre);

    List<Book> findByYearGreaterThan(int year);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByArchivedFalse();

    List<Book> findByAuthorNameIgnoreCase(String name);

    // JPQL: Find books by author's country
    @Query("""
            SELECT b
            FROM Book b
            JOIN b.author a
            WHERE LOWER(a.country) = LOWER(:country)
            """)
    List<Book> findBooksByAuthorCountry(
            @Param("country") String country
    );

    // JPQL: Archive books published before a given year
    @Modifying
    @Query("""
            UPDATE Book b
            SET b.archived = true
            WHERE b.year < :year
            """)
    int archiveBooksBeforeYear(
            @Param("year") int year
    );

    // Task 3 - Fetch books together with their authors

    @Query("SELECT b FROM Book b JOIN FETCH b.author")
    Slice<Book> findBooksAsSlice(Pageable pageable);


}
