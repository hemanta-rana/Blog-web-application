package com.admish.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "  email is required ")
    private String email;
    @NotBlank(message = "  password is required ")
    private  String password;

}
