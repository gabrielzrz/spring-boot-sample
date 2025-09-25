package br.com.gabrielzrz.service;

import br.com.gabrielzrz.service.contract.BranchService;
import br.com.gabrielzrz.constants.RepositoryAdapterConstants;
import br.com.gabrielzrz.model.Branch;
import br.com.gabrielzrz.repository.port.BranchRepositoryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zorzi
 */
@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepositoryPort branchRepositoryPort;

    public BranchServiceImpl(
            @Qualifier(RepositoryAdapterConstants.Jpa.BRANCH) BranchRepositoryPort branchRepositoryPort) {
        this.branchRepositoryPort = branchRepositoryPort;
    }

    @Override
    public List<Branch> findAll() {
        return branchRepositoryPort.findAll();
    }
}
