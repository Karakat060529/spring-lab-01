
package kz.iitu.springlab.catalog;

import org.springframework.data.jpa.domain.Specification;
import kz.iitu.springlab.web.Genre;

public class BookSpecification {

    public static Specification<Book> search(
            String title,
            Genre genre,
            Integer year) {

        return (root, query, cb) -> {

            var conditions = new java.util.ArrayList<
                    jakarta.persistence.criteria.Predicate>();

            if (title != null && !title.isBlank()) {
                conditions.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"
                        )
                );
            }

            if (genre != null) {
                conditions.add(
                        cb.equal(root.get("genre"), genre)
                );
            }

            if (year != null) {
                conditions.add(
                        cb.equal(root.get("year"), year)
                );
            }

            return cb.and(conditions.toArray(
                    new jakarta.persistence.criteria.Predicate[0]
            ));
        };
    }
}

