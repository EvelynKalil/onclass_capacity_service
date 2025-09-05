package com.onclass.capacity.infrastructure.adapters.technologyadapter;

import com.onclass.capacity.domain.spi.TechnologyGatewayPort;
import com.onclass.capacity.infrastructure.entrypoints.dto.TechnologyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TechnologyWebClientAdapter implements TechnologyGatewayPort {

    private final WebClient webClient;

    @Autowired
    public TechnologyWebClientAdapter(WebClient.Builder builder,
                                      @Value("${app.technology.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public Flux<Long> findExistingIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/technologies/{id}").build(id))
                        .retrieve()
                        .bodyToMono(TechnologyDTO.class)
                        .map(TechnologyDTO::getId)
                        .onErrorResume(ex -> Mono.empty()));
    }
}
