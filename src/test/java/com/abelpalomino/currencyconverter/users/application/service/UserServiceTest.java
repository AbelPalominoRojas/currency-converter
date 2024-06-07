package com.abelpalomino.currencyconverter.users.application.service;

import com.abelpalomino.currencyconverter.shared.application.dto.exception.DataNotFoundException;
import com.abelpalomino.currencyconverter.shared.application.dto.exception.UniqueConstraintException;
import com.abelpalomino.currencyconverter.shared.infrastructure.web.security.JwtHelper;
import com.abelpalomino.currencyconverter.users.application.dto.user.AuthDto;
import com.abelpalomino.currencyconverter.users.application.dto.user.UserBodyDto;
import com.abelpalomino.currencyconverter.users.application.mapper.UserDtoMapperImpl;
import com.abelpalomino.currencyconverter.users.application.service.impl.UserServiceImpl;
import com.abelpalomino.currencyconverter.users.domain.model.UserModel;
import com.abelpalomino.currencyconverter.users.domain.port.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserPort userPort;

    @Mock
    JwtHelper jwtHelper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    UserDtoMapperImpl userMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Captor
    ArgumentCaptor<UserBodyDto> userBodyCaptor;

    UserBodyDto userBody;
    Long id = 1L;

    UserModel userModel;

    @BeforeEach
    void setUp() {
        userBody = UserBodyDto.builder()
                .name("Abel")
                .lastName("Palomino")
                .email("abel@gmail.com")
                .password("Str123456")
                .build();

        userModel = UserModel.builder()
                .id(id)
                .name("Abel")
                .lastName("Palomino")
                .email("abel@gmail.com")
                .password("Str123456")
                .build();
    }

    @Test
    @DisplayName("Create should return UserDto when user does not exist")
    void createShouldReturnUserDtoWhenUserDoesNotExist() {
        // Given
        when(userPort.findByEmail(any(String.class)))
                .thenReturn(Mono.empty());

        when(userPort.save(any(UserModel.class)))
                .thenAnswer(invocation -> {
                    UserModel userModel = invocation.getArgument(0);
                    userModel.setId(id);
                    return Mono.just(userModel);
                });

        // When // Then
        StepVerifier.create(userService.create(userBody))
                .expectNextMatches(userDto -> {
                    assertEquals(userDto.getId(), id);
                    assertEquals(userDto.getName(), userBody.getName());
                    assertEquals(userDto.getLastName(), userBody.getLastName());
                    assertEquals(userDto.getEmail(), userBody.getEmail());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Create should return error when user already exists")
    void createShouldReturnErrorWhenUserAlreadyExists() {
        // Given
        when(userPort.findByEmail(anyString()))
                .thenReturn(Mono.just(userModel));

        when(userPort.save(any(UserModel.class)))
                .thenAnswer(invocation -> {
                    UserModel userModel = invocation.getArgument(0);
                    userModel.setId(id);
                    return Mono.just(userModel);
                });

        // When // Then
        StepVerifier.create(userService.create(userBody))
                .expectError(UniqueConstraintException.class)
                .verify();
    }

    @Test
    @DisplayName("Login should return UserSecurityDto when credentials are correct")
    void loginShouldReturnUserSecurityDtoWhenCredentialsAreCorrect() {
        // Given
        AuthDto authDto = AuthDto.builder()
                .email("abel@gmail.com")
                .password("Str123456")
                .build();


        when(userPort.findByEmail(anyString()))
                .thenReturn(Mono.just(userModel));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        // When // Then
        StepVerifier.create(userService.login(authDto))
                .expectNextMatches(userSecurityDto -> {
                    assertEquals(userSecurityDto.getEmail(), userBody.getEmail());
                    assertEquals(userSecurityDto.getName(), userBody.getName());
                    assertEquals(userSecurityDto.getLastName(), userBody.getLastName());

                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Login should return error when user not found")
    void loginShouldReturnErrorWhenUserNotFound() {
        // Given
        AuthDto authDto = AuthDto.builder()
                .email("abel@gmail.com")
                .password("Str123456")
                .build();

        when(userPort.findByEmail(authDto.getEmail())).thenReturn(Mono.empty());

        // When // Then
        StepVerifier.create(userService.login(authDto))
                .expectError(DataNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Login should return error when password is incorrect")
    void loginShouldReturnErrorWhenPasswordIsIncorrect() {
        // Given
        AuthDto authDto = AuthDto.builder()
                .email("abel@gmail.com")
                .password("Str123456")
                .build();

        when(userPort.findByEmail(authDto.getEmail())).thenReturn(Mono.just(userModel));
        when(passwordEncoder.matches(authDto.getPassword(), userModel.getPassword())).thenReturn(false);

        // When // Then
        StepVerifier.create(userService.login(authDto))
                .expectError(DataNotFoundException.class)
                .verify();

    }

}