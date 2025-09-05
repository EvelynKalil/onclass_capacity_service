package com.onclass.capacity.infrastructure.entrypoints;

import com.onclass.capacity.infrastructure.entrypoints.dto.CapacityDTO;
import com.onclass.capacity.infrastructure.entrypoints.handler.CapacityHandlerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Tag(name = "Capacidades", description = "Operaciones sobre capacidades")
@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/capacity",
                    produces = {"application/json"},
                    beanClass = CapacityHandlerImpl.class,
                    beanMethod = "create",
                    operation = @Operation(
                            operationId = "createCapacity",
                            summary = "Registrar capacidad",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Datos de la capacidad a registrar",
                                    required = true,
                                    content = @io.swagger.v3.oas.annotations.media.Content(
                                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CapacityDTO.class)
                                    )
                            )
                    )
            ),
            @RouterOperation(
                    path = "/capacities",
                    produces = {"application/json"},
                    beanClass = CapacityHandlerImpl.class,
                    beanMethod = "list",
                    operation = @Operation(
                            operationId = "listCapacities",
                            summary = "Listar capacidades"
                    )
            ),
            @RouterOperation(
                    path = "/capacities/{id}",
                    produces = {"application/json"},
                    beanClass = CapacityHandlerImpl.class,
                    beanMethod = "getById",
                    operation = @Operation(
                            operationId = "getCapacityById",
                            summary = "Buscar capacidad por ID"
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(CapacityHandlerImpl handler) {
        return route(POST("/capacity"), handler::create)
                .andRoute(GET("/capacities"), handler::list)
                .andRoute(GET("/capacities/{id}"), handler::getById);
    }
}
