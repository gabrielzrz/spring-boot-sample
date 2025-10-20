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

    @Override
    public Branch save(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return branchRepository.existsById(uuid);
    }

    @Override
    public List<Branch> findAll() {
        return branchRepository.findAll();
    }
}
