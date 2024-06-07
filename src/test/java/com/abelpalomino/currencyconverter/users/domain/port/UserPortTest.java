package com.abelpalomino.currencyconverter.users.domain.port;

import com.abelpalomino.currencyconverter.users.domain.model.UserModel;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.UserPortImpl;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.entity.UserEntity;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.mapper.UserMapperImpl;
import com.abelpalomino.currencyconverter.users.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPortTest {

    @Mock
    UserRepository userRepository;

    @Spy
    UserMapperImpl userMapper;

    @InjectMocks
    UserPortImpl userPort;

    @Captor
    ArgumentCaptor<UserModel> userModelCaptor;

    @Test
    @DisplayName("Save should return saved UserModel")
    void saveShouldReturnSavedUserModel() {
        // Given
        Long id = 1L;
        UserModel userModel = UserModel.builder()
                .id(id)
                .name("Abel")
                .lastName("Palomino")
                .email("abel@gmail.com")
                .password("Str123456")
                .build();

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity userEntity = invocation.getArgument(0);
                    userEntity.setId(id);
                    return Mono.just(userEntity);
                });

        // When // Then
        StepVerifier.create(userPort.save(userModel))
                .expectNextMatches(savedUserModel -> {
                    assertEquals(savedUserModel.getId(), id);
                    assertEquals(savedUserModel.getName(), userModel.getName());
                    assertEquals(savedUserModel.getLastName(), userModel.getLastName());
                    assertEquals(savedUserModel.getEmail(), userModel.getEmail());
                    assertEquals(savedUserModel.getPassword(), userModel.getPassword());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    @DisplayName("findByEmail should return UserModel")
    void findByEmailShouldReturnUserModel() {
        // Given
        Long id = 1L;
        String email = "abel@gmail.com";

        UserEntity userEntity = UserEntity.builder()
                .id(id)
                .name("Abel")
                .lastName("Palomino")
                .email(email)
                .password("Str123456")
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Mono.just(userEntity));

        // When // Then
        StepVerifier.create(userPort.findByEmail(email))
                .expectNextMatches(userModel -> {
                    assertEquals(userModel.getId(), id);
                    assertEquals(userModel.getName(), userEntity.getName());
                    assertEquals(userModel.getLastName(), userEntity.getLastName());
                    assertEquals(userModel.getEmail(), userEntity.getEmail());
                    assertEquals(userModel.getPassword(), userEntity.getPassword());
                    return true;
                })
                .verifyComplete();

    }

}