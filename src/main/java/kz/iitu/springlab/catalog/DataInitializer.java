
package kz.iitu.springlab.catalog;

import kz.iitu.springlab.web.Genre;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(SeedService seedService) {
        return args -> seedService.seed();
    }

    @org.springframework.stereotype.Service
    public static class SeedService {

        private final AuthorRepository authors;
        private final TagRepository tags;

        public SeedService(
                AuthorRepository authors,
                TagRepository tags) {
            this.authors = authors;
            this.tags = tags;
        }

        @Transactional
        public void seed() {
            if (authors.count() > 0) {
                return;
            }

            Author a1 = new Author("Joshua Bloch", "USA");
            Author a2 = new Author("Robert Martin", "USA");
            Author a3 = new Author("Craig Walls", "USA");

            Tag java = new Tag("Java");
            Tag spring = new Tag("Spring");
            Tag programming = new Tag("Programming");
            Tag bestseller = new Tag("Bestseller");

            tags.save(java);
            tags.save(spring);
            tags.save(programming);
            tags.save(bestseller);

            Book b1 = new Book("Effective Java", Genre.SCIENCE, 2018);
            b1.addTag(java);
            b1.addTag(programming);
            a1.addBook(b1);

            Book b2 = new Book("Java Basics", Genre.SCIENCE, 2020);
            b2.addTag(java);
            a1.addBook(b2);

            Book b3 = new Book("Clean Code", Genre.SCIENCE, 2008);
            b3.addTag(programming);
            b3.addTag(bestseller);
            a2.addBook(b3);

            Book b4 = new Book("Clean Architecture", Genre.SCIENCE, 2017);
            b4.addTag(programming);
            a2.addBook(b4);

            Book b5 = new Book("Refactoring Guide", Genre.SCIENCE, 2019);
            b5.addTag(programming);
            a2.addBook(b5);

            Book b6 = new Book("Spring in Action", Genre.SCIENCE, 2022);
            b6.addTag(spring);
            b6.addTag(java);
            a3.addBook(b6);

            Book b7 = new Book("Spring Boot Guide", Genre.SCIENCE, 2023);
            b7.addTag(spring);
            a3.addBook(b7);

            Book b8 = new Book("Cloud Applications", Genre.SCIENCE, 2024);
            b8.addTag(spring);
            b8.addTag(programming);
            a3.addBook(b8);

            authors.save(a1);
            authors.save(a2);
            authors.save(a3);
        }
    }
}

