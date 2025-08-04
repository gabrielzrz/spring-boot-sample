package gabrielzrz.com.github.repository.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Zorzi
 * Interface para Repository Ports que suportam busca por texto
 */
public interface SearchableRepositoryPort<T, ID> extends BaseRepositoryPort<T, ID> {

    /**
     * Busca entidades por texto (implementação específica de cada repositório)
     */
    Page<T> search(String searchTerm, Pageable pageable);

    /**
     * Busca entidades por texto sem paginação
     */
    List<T> search(String searchTerm);

    /**
     * Busca entidades por múltiplos termos
     */
    Page<T> searchByTerms(List<String> terms, Pageable pageable);
}
