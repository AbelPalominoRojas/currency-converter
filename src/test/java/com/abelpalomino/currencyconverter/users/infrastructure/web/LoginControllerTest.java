package com.abelpalomino.currencyconverter.users.infrastructure.web;

import com.abelpalomino.currencyconverter.users.application.dto.user.AuthDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserSecurityDto;
import com.abelpalomino.currencyconverter.users.application.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class LoginControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Auth should return ok when credentials are correct")
    void authShouldReturnOkWhenCredentialsAreCorrect() {
        // Given
        Long id = 1L;
        AuthDto authDto = AuthDto.builder()
                .email("abel@gmail.com")
                .password("Str123456")
                .build();

        UserSecurityDto userSecurityDto = UserSecurityDto.builder()
                .id(id)
                .name("Abel")
                .lastName("Palomino")
                .email("abel@gmail.com")
                .accessToken("accessToken")
                .build();

        // When // Then
        when(userService.login(any(AuthDto.class)))
                .thenReturn(Mono.just(userSecurityDto));

        client.post()
                .uri("/login")
                .bodyValue(authDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.name").isEqualTo(userSecurityDto.getName())
                .jsonPath("$.lastName").isEqualTo(userSecurityDto.getLastName())
                .jsonPath("$.email").isEqualTo(userSecurityDto.getEmail())
                .jsonPath("$.accessToken").isEqualTo(userSecurityDto.getAccessToken());

    }

    @Test
    @DisplayName("Auth should return bad request when email is incorrect")
    void authShouldReturnBadRequestWhenPasswordIsIncorrect() {
        AuthDto authDto = new AuthDto();

        client.post()
                .uri("/login")
                .bodyValue(authDto)
                .exchange()
                .expectStatus().isBadRequest();
    }
}