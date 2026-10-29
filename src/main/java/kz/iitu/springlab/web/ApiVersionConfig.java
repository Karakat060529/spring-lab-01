
package kz.iitu.springlab.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(
            ApiVersionConfigurer configurer) {

        configurer.useRequestHeader("API-Version");
        configurer.setDefaultVersion("1");
    }
}
