package kz.iitu.springlab.web;

import jakarta.validation.constraints.NotBlank;

public class Publisher {

    @NotBlank(message = "{publisher.name.required}")
    private String name;

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
