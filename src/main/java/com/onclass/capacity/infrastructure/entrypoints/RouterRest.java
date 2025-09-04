package com.onclass.capacity.infrastructure.entrypoints;

import com.onclass.capacity.infrastructure.entrypoints.handler.CapacityHandlerImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/capacity",   beanClass = CapacityHandlerImpl.class, beanMethod = "create", operation = @Operation(summary = "Create capacity")),
            @RouterOperation(path = "/capacities", beanClass = CapacityHandlerImpl.class, beanMethod = "list",   operation = @Operation(summary = "List capacities")),
            @RouterOperation(path = "/capacities/{id}", beanClass = CapacityHandlerImpl.class, beanMethod = "getById", operation = @Operation(summary = "Get capacity by id"))
    })
    public RouterFunction<?> routes(CapacityHandlerImpl handler) {
        return route(POST("/capacity"), handler::create)
                .andRoute(GET("/capacities"), handler::list)
                .andRoute(GET("/capacities/{id}"), handler::getById);
    }
}
