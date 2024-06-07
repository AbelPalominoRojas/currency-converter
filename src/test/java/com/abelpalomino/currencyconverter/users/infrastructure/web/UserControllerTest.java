package com.abelpalomino.currencyconverter.users.infrastructure.web;

import com.abelpalomino.currencyconverter.users.application.dto.user.UserBodyDto;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.entity.UserEntity;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    UserRepository userRepository;

    @Captor
    ArgumentCaptor<UserBodyDto> userBodyCaptor;


    @Test
    @DisplayName("Create should return created when user does not exist")
    void createShouldReturnCreatedWhenUserDoesNotExist() {
        // Given
        Long id = 1L;
        UserBodyDto userBody = UserBodyDto.builder()
                .name("Abel")
                .lastName("Palomino")
                .email("abel@gmail.com")
                .password("Str123456")
                .build();

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity userEntity = invocation.getArgument(0);
                    userEntity.setId(id);
                    return Mono.just(userEntity);
                });

        // When // Then
        client.post()
                .uri("/users")
                .bodyValue(userBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.name").isEqualTo(userBody.getName())
                .jsonPath("$.lastName").isEqualTo(userBody.getLastName())
                .jsonPath("$.email").isEqualTo(userBody.getEmail())
                .jsonPath("$.password").doesNotExist();
    }

    @Test
    @DisplayName("Create should return conflict when user exists")
    void createShouldReturnBadRequestWhenNotSendRequiredFields() {
        // Given
        UserBodyDto userBody = UserBodyDto.builder()
                .build();

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(UserEntity.builder().build()));

        // When // Then
        client.post()
                .uri("/users")
                .bodyValue(userBody)
                .exchange()
                .expectStatus().isBadRequest();
    }

}