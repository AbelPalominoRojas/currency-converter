package com.abelpalomino.currencyconverter.users.infrastructure.web;

import com.abelpalomino.currencyconverter.shared.application.dto.exception.ArgumentNotValidError;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.GeneralError;
import com.abelpalomino.currencyconverter.shared.infrastructure.web.constant.StatusCode;
import com.abelpalomino.currencyconverter.users.application.dto.user.AuthDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserSecurityDto;
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

@SecurityRequirements(value = {})
@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    @ApiResponse(responseCode = StatusCode.OK)
    @ApiResponse(
            responseCode = StatusCode.NOT_FOUND,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GeneralError.class)
            )
    )
    @ApiResponse(
            responseCode = StatusCode.BAD_REQUEST,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArgumentNotValidError.class)
            )
    )
    @PostMapping
    public Mono<ResponseEntity<UserSecurityDto>> auth(@Valid @RequestBody AuthDto authDto){

        return userService.login(authDto)
                .map(user -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(user)
                );
    }
}
