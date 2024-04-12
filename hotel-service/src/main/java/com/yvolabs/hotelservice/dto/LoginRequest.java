package com.yvolabs.hotelservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Yvonne N
 */
@Data
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
