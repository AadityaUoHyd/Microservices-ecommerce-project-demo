package org.aadi.ecommerce.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
public class SessionController {

    @GetMapping("/")
    public Mono<String> home(WebSession webSession) {
        return Mono.just(webSession.getId());
    }
}
