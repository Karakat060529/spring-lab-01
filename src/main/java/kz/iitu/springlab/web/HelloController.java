package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(
                owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors()
        );
    }

    public record Greeting(
            String message,
            String owner,
            LocalDateTime timestamp
    ) { }

    public record Info(
            String owner,
            String javaVersion,
            int cpuCores
    ) { }
    @GetMapping("/fibonacci")
    public Object fibonacci(@RequestParam(defaultValue = "10") int n) {
        if (n < 1 || n > 50) {
            return "n must be between 1 and 50";
        }

        long[] numbers = new long[n];

        if (n >= 1) {
            numbers[0] = 0;
        }

        if (n >= 2) {
            numbers[1] = 1;
        }

        for (int i = 2; i < n; i++) {
            numbers[i] = numbers[i - 1] + numbers[i - 2];
        }

        return numbers;
    }
}
