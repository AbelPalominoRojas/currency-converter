package com.abelpalomino.currencyconverter.users.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    @NotBlank(message = "El campo email es requerido")
    @Email(message = "Ingrese un email valido")
    private String email;

    @NotBlank(message = "El campo password es requerido")
    private String password;
}
