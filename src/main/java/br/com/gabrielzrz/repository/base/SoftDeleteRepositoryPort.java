package br.com.gabrielzrz.repository.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author Zorzi
 * Interface para Repository Ports que suportam Soft Delete
 * Adiciona métodos específicos para entidades que podem ser desabilitadas
 */
public interface SoftDeleteRepositoryPort<T, ID> extends BaseRepositoryPort<T, ID> {

    /**
     * Busca apenas entidades ativas
     */
    Page<T> findAllActive(Pageable pageable);

    /**
     * Busca apenas entidades inativas
     */
    Page<T> findAllInactive(Pageable pageable);

    /**
     * Busca entidade ativa por ID
     */
    Optional<T> findActiveById(ID id);

    /**
     * Desabilita entidade (soft delete)
     */
    void disable(ID id);

    /**
     * Habilita entidade
     */
    void enable(ID id);

    /**
     * Verifica se entidade está ativa
     */
    boolean isActive(ID id);

    /**
     * Conta apenas entidades ativas
     */
    long countActive();
}
