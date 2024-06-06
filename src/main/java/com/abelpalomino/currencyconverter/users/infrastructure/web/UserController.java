package com.abelpalomino.currencyconverter.users.infrastructure.web;

import com.abelpalomino.currencyconverter.shared.application.dto.exception.ArgumentNotValidError;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.GeneralError;
import com.abelpalomino.currencyconverter.shared.infrastructure.web.constant.StatusCode;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserBodyDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserDto;
import com.abelpalomino.currencyconverter.users.application.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    @SecurityRequirements(value = {})
    @ApiResponse(responseCode = StatusCode.CREATED, description = "User created")
    @ApiResponse(
            responseCode = StatusCode.BAD_REQUEST,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArgumentNotValidError.class)
            )
    )
    @ApiResponse(
            responseCode = StatusCode.CONFLICT,
            description = "Client validations failed",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GeneralError.class)
            )
    )
    @PostMapping
    public Mono<ResponseEntity<UserDto>> create(@Valid @RequestBody UserBodyDto userBody) {
        return userService.create(userBody)
                .map(user -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(user));
    }
}
