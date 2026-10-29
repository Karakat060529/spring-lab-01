
package kz.iitu.springlab.catalog;

import kz.iitu.springlab.web.Genre;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    // GET - Get all books
    public List<Book> findAll(String author) {
        return repository.findAll();
    }

    // GET - Find book by ID
    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    // POST - Create a new book
    public Book create(Book book) {

        boolean duplicate = repository.findAll().stream()
                .anyMatch(existing ->
                        existing.getTitle()
                                .equalsIgnoreCase(book.getTitle())
                );

        if (duplicate) {
            throw new DuplicateTitleException(book.getTitle());
        }

        return repository.save(book);
    }

    // PUT - Update an existing book
    @Transactional
    public Optional<Book> replace(long id, Book book) {

        Optional<Book> existing = repository.findById(id);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Book oldBook = existing.get();

        oldBook.setTitle(book.getTitle());
        oldBook.setGenre(book.getGenre());
        oldBook.setYear(book.getYear());

        return Optional.of(repository.save(oldBook));
    }

    // DELETE - Delete book by ID
    @Transactional
    public boolean delete(long id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    // Task 3 - Bulk update using JPQL
    @Transactional
    public int archiveBooksBeforeYear(int year) {
        return repository.archiveBooksBeforeYear(year);
    }

    // Task 4 - Pagination and Sorting
    public Page<Book> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Slice<Book> findAllSliced(Pageable pageable) {
        return repository.findBooksAsSlice(pageable);
    }

    // Individual Assignment - Variant 8: Specification
    @Transactional(readOnly = true)
    public List<Book> searchBooks(
            String title,
            Genre genre,
            Integer year) {

        Specification<Book> specification =
                BookSpecification.search(title, genre, year);

        List<Book> books = repository.findAll(specification);

        // Initialize authors before returning results
        books.forEach(book -> {
            if (book.getAuthor() != null) {
                book.getAuthor().getName();
            }
        });

        return books;
    }
}
