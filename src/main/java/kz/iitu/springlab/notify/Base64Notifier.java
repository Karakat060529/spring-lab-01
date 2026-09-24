package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component("base64")
@Order(3)
public class Base64Notifier implements Notifier {

    private static final Logger log =
            LoggerFactory.getLogger(Base64Notifier.class);

    @PostConstruct
    public void init() {
        log.info("BASE64 notifier initialized");
    }

    @Override
    public String send(String message) {
        return Base64.getEncoder()
                .encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String channel() {
        return "base64";
    }
}