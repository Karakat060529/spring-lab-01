
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

        if (limit < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<Book> books = service.findAll(author);

        List<Map<String, Object>> result = new ArrayList<>();

        boolean russian = language.toLowerCase()
                .startsWith("ru");

        for (Book book : books.stream().limit(limit).toList()) {

            Map<String, Object> item = new LinkedHashMap<>();

            if (russian) {
                item.put("идентификатор", book.getId());
                item.put("название", book.getTitle());
                item.put("автор", null);
                item.put("год", book.getYear());
            } else {
                item.put("id", book.getId());
                item.put("title", book.getTitle());
                item.put("author", null);
                item.put("year", book.getYear());
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
                        "/legacy/api/books/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(
            @PathVariable Long id,
            @RequestBody Book book) {

        return service.replace(id, book)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
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
