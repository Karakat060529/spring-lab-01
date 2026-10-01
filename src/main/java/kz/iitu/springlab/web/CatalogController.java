package kz.iitu.springlab.web;

import kz.iitu.springlab.service.CatalogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/api/lab4/item/{id}")
    public String findById(@PathVariable long id) {
        return catalogService.findById(id);
    }

    @GetMapping("/api/lab4/items")
    public List<String> findAll(@RequestParam(defaultValue = "5") int limit) {
        return catalogService.findAll(limit);
    }

    @DeleteMapping("/api/lab4/item/{id}")
    public String remove(@PathVariable long id) {
        return catalogService.remove(id);
    }
    @GetMapping("/api/lab4/proxy")
    public String proxyInfo() {
        return catalogService.getClass().getName();
    }
    @GetMapping("/api/lab4/internal/{id}")
    public String internal(@PathVariable long id) {
        return catalogService.internalCallDemo(id);
    }
    @GetMapping("/api/lab4/remove-twice/{id}")
    public String removeTwice(@PathVariable long id) {
        return catalogService.removeTwice(id);
    }
}