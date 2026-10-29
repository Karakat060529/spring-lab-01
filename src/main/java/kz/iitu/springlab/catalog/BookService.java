
package kz.iitu.springlab.catalog;

import org.springframework.stereotype.Service;

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
        List<Book> books = repository.findAll();

        if (author == null || author.isBlank()) {
            return books;
        }

        return books.stream()
                .filter(book -> book.author()
                        .toLowerCase()
                        .contains(author.toLowerCase()))
                .toList();
    }

    // GET - Find book by ID
    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    // POST - Create a new book

    public Book create(Book book) {

        boolean duplicate = repository.findAll().stream()
                .anyMatch(existing ->
                        existing.title().equalsIgnoreCase(book.title())
                                && existing.author().equalsIgnoreCase(book.author())
                );

        if (duplicate) {
            throw new DuplicateTitleException(book.title());
        }

        return repository.save(book);
    }


    // PUT - Update an existing book
    public Optional<Book> replace(long id, Book book) {

        Optional<Book> existing = repository.findById(id);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Book oldBook = existing.get();

        Book updated = new Book(
                id,
                book.title(),
                book.author(),
                book.year(),
                book.genre(),
                oldBook.createdAt()
        );

        return Optional.of(repository.save(updated));
    }

    // DELETE - Delete book by ID
    public boolean delete(long id) {
        return repository.deleteById(id);
    }
}
