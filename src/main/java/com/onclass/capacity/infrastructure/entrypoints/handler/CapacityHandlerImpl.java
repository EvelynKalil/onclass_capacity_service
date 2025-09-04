package com.onclass.capacity.infrastructure.entrypoints.handler;

import com.onclass.capacity.domain.api.CapacityServicePort;
import com.onclass.capacity.domain.enums.TechnicalMessage;
import com.onclass.capacity.domain.exceptions.BusinessException;
import com.onclass.capacity.infrastructure.entrypoints.dto.CapacityDTO;
import com.onclass.capacity.infrastructure.entrypoints.mapper.CapacityMapper;
import com.onclass.capacity.infrastructure.entrypoints.util.APIResponse;
import com.onclass.capacity.infrastructure.entrypoints.util.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CapacityHandlerImpl {

    private static final String X_MESSAGE_ID = "X_MESSAGE_ID";

    private final CapacityServicePort service;
    private final CapacityMapper mapper;

    public Mono<ServerResponse> create(ServerRequest request) {
        final String messageId = request.headers().firstHeader(X_MESSAGE_ID);
        log.info("[{}] POST /capacity - creating", messageId);

        return request.bodyToMono(CapacityDTO.class)
                .map(mapper::dtoToDomain)
                .flatMap(service::register)
                .map(mapper::toDto)
                .flatMap(dto -> {
                    APIResponse body = APIResponse.builder()
                            .code(String.valueOf(HttpStatus.CREATED.value()))
                            .message("Created")
                            .identifier(messageId)
                            .date(nowIso())
                            .data(dto)
                            .build();
                    return ServerResponse.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                })
                .onErrorResume(ex -> handleError(ex, messageId));
    }

    public Mono<ServerResponse> list(ServerRequest request) {
        final String messageId = request.headers().firstHeader(X_MESSAGE_ID);
        log.info("[{}] GET /capacities - listing", messageId);

        // Si en tu Technology devuelves ARRAY/FLUX directamente, deja así:
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        service.list().map(mapper::toDto),
                        CapacityDTO.class
                )
                .onErrorResume(ex -> handleError(ex, messageId));

        // Si en tu Technology envuelves también en APIResponse, avísame y te lo dejo igual que allá.
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        final String messageId = request.headers().firstHeader(X_MESSAGE_ID);
        final Long id = Long.valueOf(request.pathVariable("id"));
        log.info("[{}] GET /capacities/{} - fetching", messageId, id);

        return service.findById(id)
                .map(mapper::toDto)
                .flatMap(dto -> {
                    APIResponse body = APIResponse.builder()
                            .code(String.valueOf(HttpStatus.OK.value()))
                            .message("OK")
                            .identifier(messageId)
                            .date(nowIso())
                            .data(dto)
                            .build();
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                })
                .switchIfEmpty(notFound(messageId, "Capacity not found", "id"))
                .onErrorResume(ex -> handleError(ex, messageId));
    }

    /* ====================== helpers locales (sin “factory”) ====================== */

    private static String nowIso() {
        return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private Mono<ServerResponse> handleError(Throwable ex, String messageId) {
        if (ex instanceof BusinessException be) {
            TechnicalMessage tm = be.getTechnicalMessage();
            log.warn("[{}] BusinessException: {} - {}", messageId, tm.getCode(), tm.getMessage());
            APIResponse body = APIResponse.builder()
                    .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                    .message("Bad Request")
                    .identifier(messageId)
                    .date(nowIso())
                    .errors(List.of(new ErrorDTO(tm.getCode(), tm.getMessage(), tm.getParam())))
                    .build();
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body);
        }

        log.error("[{}] Unexpected error", messageId, ex);
        APIResponse body = APIResponse.builder()
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message("Internal Server Error")
                .identifier(messageId)
                .date(nowIso())
                .errors(List.of(new ErrorDTO("500", "Capacity error", null)))
                .build();
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body);
    }

    private Mono<ServerResponse> notFound(String messageId, String message, String field) {
        APIResponse body = APIResponse.builder()
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .message("Not Found")
                .identifier(messageId)
                .date(nowIso())
                .errors(List.of(new ErrorDTO("404", message, field)))
                .build();
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body);
    }
}
