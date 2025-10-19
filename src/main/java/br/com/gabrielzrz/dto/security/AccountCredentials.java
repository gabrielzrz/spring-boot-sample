package br.com.gabrielzrz.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Zorzi
 */
public record AccountCredentials(

        @NotBlank(message = "Username is required")
        @Size(max = 255, message = "Username must not exceed 255 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(max = 255, message = "Password must not exceed 255 characters")
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 2777912995572096209L;
}
