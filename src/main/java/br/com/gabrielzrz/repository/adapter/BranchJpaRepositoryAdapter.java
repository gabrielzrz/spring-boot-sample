package br.com.gabrielzrz.repository.adapter;

import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.model.Branch;
import br.com.gabrielzrz.repository.jpa.BranchRepository;
import br.com.gabrielzrz.repository.port.BranchRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author Zorzi
 */
@Component(RepositoryAdapterConstants.Jpa.BRANCH)
public class BranchJpaRepositoryAdapter implements BranchRepositoryPort {

    private final BranchRepository branchRepository;

    public BranchJpaRepositoryAdapter(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public Branch findById(UUID uuid) {
        return branchRepository.findById(uuid).orElse(null);
    }

    @Override
    public Page<Branch> findAll(Pageable pageable) {
        return branchRepository.findAll(pageable);
    }

    public List<Branch> findAll() {
        return branchRepository.findAll();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return branchRepository.existsById(uuid);
    }

    @Override
    public Branch save(Branch entity) {
        return branchRepository.save(entity);
    }

    @Override
    public List<Branch> saveAll(List<Branch> entities) {
        return branchRepository.saveAll(entities);
    }

    @Override
    public void delete(Branch entity) {
        branchRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID uuid) {
        branchRepository.deleteById(uuid);
    }
}
