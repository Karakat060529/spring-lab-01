
package kz.iitu.springlab.web;

import jakarta.validation.Valid;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import kz.iitu.springlab.catalog.BookNotFoundException;

import kz.iitu.springlab.web.dto.BookMapper;
import kz.iitu.springlab.web.dto.BookRequest;
import kz.iitu.springlab.web.dto.BookResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Tag(
        name = "Books",
        description = "The catalogue of books"
)
@RestController
@RequestMapping("/api/books")
public class BookApiController {

    private final BookService service;
    private final BookMapper mapper;

    public BookApiController(
            BookService service,
            BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // GET - All books
    @Operation(summary = "Get all books")
    @ApiResponse(
            responseCode = "200",
            description = "List of books returned successfully"
    )
    @GetMapping
    public List<BookResponse> list(
            @RequestParam(required = false) String author) {

        return mapper.toResponseList(
                service.findAll(author)
        );
    }

    // GET - API Version 1
    @Operation(
            summary = "Find a book by its identifier (Version 1)",
            description = "Returns the standard book response"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The book was found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No book with this id",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ProblemDetail.class
                            )
                    )
            )
    })
    @GetMapping(path = "/{id}", version = "1")
    public BookResponse findV1(
            @Parameter(
                    description = "Identifier of the book",
                    example = "42"
            )
            @PathVariable long id) {

        Book book = service.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(id));

        return mapper.toResponse(book);
    }

    // GET - API Version 2
    @Operation(
            summary = "Find a book by its identifier (Version 2)",
            description = "Returns extended book information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Extended book information returned"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found"
            )
    })
    @GetMapping(path = "/{id}", version = "2")
    public Map<String, Object> findV2(
            @Parameter(
                    description = "Identifier of the book",
                    example = "42"
            )
            @PathVariable long id) {

        Book book = service.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(id));

        return Map.of(
                "apiVersion", "2",
                "book", mapper.toResponse(book),
                "message", "Extended book information"
        );
    }

    // POST - Create book
    @Operation(summary = "Create a new book")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Book created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Duplicate book"
            )
    })
    @PostMapping
    public ResponseEntity<BookResponse> create(
            @Valid @RequestBody BookRequest request) {

        Book saved = service.create(
                mapper.toDomain(request)
        );

        return ResponseEntity
                .created(URI.create(
                        "/api/books/" + saved.id()))
                .body(mapper.toResponse(saved));
    }

    // PUT - Update book
    @Operation(summary = "Update an existing book")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Book updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found"
            )
    })
    @PutMapping("/{id}")
    public BookResponse update(
            @PathVariable long id,
            @Valid @RequestBody BookRequest request) {

        Book updated = service.replace(
                id,
                mapper.toDomain(request)
        ).orElseThrow(() ->
                new BookNotFoundException(id));

        return mapper.toResponse(updated);
    }

    // DELETE - Delete book
    @Operation(summary = "Delete a book by its identifier")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Book deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable long id) {

        boolean deleted = service.delete(id);

        if (!deleted) {
            throw new BookNotFoundException(id);
        }

        return ResponseEntity.noContent().build();
    }
}
