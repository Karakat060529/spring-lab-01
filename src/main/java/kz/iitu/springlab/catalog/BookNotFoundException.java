
package kz.iitu.springlab.catalog;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(long id) {
        super("No book with id " + id);
    }
}

