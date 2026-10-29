
package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/legacy/api/books")
public class BookRestController {

    private final BookService service;

    public BookRestController(BookService service) {
        this.service = service;
    }

    // Lab 6 - Variant 8: Accept-Language
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> list(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "100") int limit,
            @RequestHeader(
                    value = "Accept-Language",
                    defaultValue = "en"
            ) String language) {

        List<Book> books = service.findAll(author);

        if (limit < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Map<String, Object>> result = new ArrayList<>();

        boolean russian = language.toLowerCase()
                .startsWith("ru");

        for (Book book : books.stream().limit(limit).toList()) {

            Map<String, Object> item = new LinkedHashMap<>();

            if (russian) {
                item.put("идентификатор", book.id());
                item.put("название", book.title());
                item.put("автор", book.author());
                item.put("год", book.year());
            } else {
                item.put("id", book.id());
                item.put("title", book.title());
                item.put("author", book.author());
                item.put("year", book.year());
            }

            result.add(item);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(
            @PathVariable Long id) {

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> create(
            @RequestBody Book book) {

        Book saved = service.create(book);

        return ResponseEntity
                .created(URI.create(
                        "/legacy/api/books/" + saved.id()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(
            @PathVariable Long id,
            @RequestBody Book book) {

        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Book updated = service.create(
                new Book(
                        id,
                        book.title(),
                        book.author(),
                        book.year()
                )
        );

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
