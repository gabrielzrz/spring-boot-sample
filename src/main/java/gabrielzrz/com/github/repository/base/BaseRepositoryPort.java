package gabrielzrz.com.github.repository.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Zorzi
 * Interface base para todos os Repository Ports
 * Define operações CRUD comuns para todas as entidades
 */
public interface BaseRepositoryPort<T, ID> {

    T findById(ID id);

    Page<T> findAll(Pageable pageable);

    List<T> findAll();

    boolean existsById(ID id);

    T save(T entity);

    List<T> saveAll(List<T> entities);

    void delete(T entity);

    void deleteById(ID id);
}
