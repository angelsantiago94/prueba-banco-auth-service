package com.banco.auth.dto;

import com.banco.auth.validation.ValidPassword;
import com.banco.auth.validation.ValidUserRegistration;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ValidUserRegistration
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @ValidPassword
    private String password;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no debe exceder 50 caracteres")
    private String firstName;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no debe exceder 50 caracteres")
    private String lastName;
    
    @NotBlank(message = "El rol es obligatorio")
    private String role;
}
