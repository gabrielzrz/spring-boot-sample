package gabrielzrz.com.github.util;

import jakarta.validation.ConstraintViolation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Zorzi
 * Utilitário para parsing e formatação de mensagens de erro de exceções do JPA/Spring Data
 */
@Component
public class ExceptionMessageParser {

    /**
     * Converte DataIntegrityViolationException em mensagem amigável
     */
    public String parseDataIntegrityError(DataIntegrityViolationException e) {
        String message = e.getMostSpecificCause().getMessage().toLowerCase();
        if (message.contains("duplicate entry") || message.contains("unique constraint")) {
            return extractDuplicateKeyError(message);
        } else if (message.contains("cannot be null") || message.contains("not null")) {
            return extractNullConstraintError(message);
        } else if (message.contains("foreign key constraint")) {
            return extractForeignKeyError(message);
        } else if (message.contains("data too long")) {
            return extractDataTooLongError(message);
        } else if (message.contains("check constraint")) {
            return extractCheckConstraintError(message);
        } else {
            return "Violação de integridade dos dados: " + e.getMostSpecificCause().getMessage();
        }
    }

    /**
     * Converte ConstraintViolationException em mensagem amigável
     */
    public String parseValidationErrors(Set<ConstraintViolation<?>> violations) {
        if (violations.isEmpty()) {
            return "Erro de validação desconhecido";
        }
        if (violations.size() == 1) {
            ConstraintViolation<?> violation = violations.iterator().next();
            return formatSingleValidationError(violation);
        }
        return formatMultipleValidationErrors(violations);
    }

    /**
     * Extrai informação específica de erro de chave duplicada
     */
    private String extractDuplicateKeyError(String message) {
        if (message.contains("'uk_email'") || message.contains("email")) {
            return "Email já cadastrado no sistema";
        } else if (message.contains("'uk_cpf'") || message.contains("cpf")) {
            return "CPF já cadastrado no sistema";
        } else if (message.contains("'uk_phone'") || message.contains("phone")) {
            return "Telefone já cadastrado no sistema";
        } else {
            return "Registro duplicado - dados já existem no sistema";
        }
    }

    /**
     * Extrai informação específica de campo obrigatório
     */
    private String extractNullConstraintError(String message) {
        if (message.contains("name")) {
            return "Nome é obrigatório";
        } else if (message.contains("email")) {
            return "Email é obrigatório";
        } else if (message.contains("birth_day")) {
            return "Data de nascimento é obrigatória";
        } else {
            return "Campo obrigatório não informado";
        }
    }

    /**
     * Extrai informação específica de chave estrangeira
     */
    private String extractForeignKeyError(String message) {
        if (message.contains("department")) {
            return "Departamento informado não existe";
        } else if (message.contains("category")) {
            return "Categoria informada não existe";
        } else {
            return "Referência inválida - dados relacionados não encontrados";
        }
    }

    /**
     * Extrai informação específica de dados muito longos
     */
    private String extractDataTooLongError(String message) {
        if (message.contains("name")) {
            return "Nome muito longo (máximo permitido)";
        } else if (message.contains("address")) {
            return "Endereço muito longo (máximo permitido)";
        } else {
            return "Dados muito extensos para o campo";
        }
    }

    /**
     * Extrai informação específica de check constraint
     */
    private String extractCheckConstraintError(String message) {
        return "Valor inválido para o campo - não atende às regras de negócio";
    }

    /**
     * Formata erro de validação única
     */
    private String formatSingleValidationError(ConstraintViolation<?> violation) {
        String field = getFieldDisplayName(violation.getPropertyPath().toString());
        String message = violation.getMessage();
        return field + ": " + message;
    }

    /**
     * Formata múltiplos erros de validação
     */
    private String formatMultipleValidationErrors(Set<ConstraintViolation<?>> violations) {
        StringBuilder sb = new StringBuilder("Erros de validação: ");
        violations.forEach(violation -> {
            String field = getFieldDisplayName(violation.getPropertyPath().toString());
            sb.append(field)
                    .append(" (")
                    .append(violation.getMessage())
                    .append("), ");
        });

        // Remove última vírgula e espaço
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }

    /**
     * Converte nome técnico do campo para nome amigável
     */
    private String getFieldDisplayName(String fieldName) {
        switch (fieldName.toLowerCase()) {
            case "name": return "Nome";
            case "email": return "Email";
            case "birthday": return "Data de nascimento";
            case "address": return "Endereço";
            case "gender": return "Gênero";
            case "phone": return "Telefone";
            case "cpf": return "CPF";
            default: return fieldName;
        }
    }

    /**
     * Método genérico para qualquer Exception
     */
    public String parseGenericException(Exception e) {
        if (e instanceof DataIntegrityViolationException) {
            return parseDataIntegrityError((DataIntegrityViolationException) e);
        } else if (e instanceof jakarta.validation.ConstraintViolationException) {
            jakarta.validation.ConstraintViolationException cve = (jakarta.validation.ConstraintViolationException) e;
            return parseValidationErrors(cve.getConstraintViolations());
        } else {
            return "Erro inesperado: " + e.getMessage();
        }
    }
}
