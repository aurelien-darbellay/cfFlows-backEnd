package s05t02.interactiveCV.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationRequestDto {
    @NotBlank(message = "Username is required")
    private final String username;
    @NotBlank(message = "Password is required")
    private final String password;
    private String firstname;
    private String lastname;
}
