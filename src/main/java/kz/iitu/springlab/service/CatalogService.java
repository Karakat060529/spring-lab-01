package kz.iitu.springlab.service;

import kz.iitu.springlab.annotation.Audited;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class CatalogService {

    private final CatalogService self;

    public CatalogService(@Lazy CatalogService self) {
        this.self = self;
    }

    public String findById(long id) {
        sleep(50);
        return "Item no. " + id;
    }

    public List<String> findAll(int limit) {
        sleep(300);

        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "Item no. " + i)
                .toList();
    }

    @Audited
    public String remove(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid identifier: " + id
            );
        }

        return "Removed item no. " + id;
    }

    // BEFORE FIX:
    // this.findById(id) bypasses the Spring proxy
    public String internalCallDemo(long id) {
        return this.findById(id);
    }

    // AFTER FIX:
    // calls go through the Spring proxy
    public String removeTwice(long id) {
        String first = self.remove(id);
        String second = self.remove(id + 1);

        return first + " | " + second;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}