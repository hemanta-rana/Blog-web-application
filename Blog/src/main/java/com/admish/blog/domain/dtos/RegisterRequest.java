package com.admish.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder @AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "name is required  ")
    private String name ;

    @NotBlank(message = "email is required ")
    private String email;

    @NotBlank(message = "  password is required ")
    private String password;




}
