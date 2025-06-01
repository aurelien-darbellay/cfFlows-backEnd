package s05t02.interactiveCV.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserDetailsDto {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
}
