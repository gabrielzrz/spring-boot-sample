package br.com.gabrielzrz.dto.request.filters;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Zorzi
 */
public record PersonFilterRequest(
        String name,

        String gender,

        Boolean isEnabled,

        LocalDate birthDateFrom,

        LocalDate birthDateTo
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 5996217390849456034L;
}
