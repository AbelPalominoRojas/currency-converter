package com.abelpalomino.currencyconverter.users.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBodyDto {
    @NotBlank(message = "El campo name es requerido")
    @Size(min = 3, max = 50, message = "El campo name debe tener entre 3 y 50 caracteres")
    private String name;

    @Size(max = 50, message = "El campo lastName debe tener como maximo 50 caracteres")
    private String lastName;

    @NotBlank(message = "El campo email es requerido")
    @Email(message = "El campo email debe ser un email valido")
    private String email;

    @NotBlank(message = "El campo password es requerido")
    @Size(min = 8, max = 50, message = "El campo password debe tener entre 8 y 50 caracteres")
    private String password;
}
