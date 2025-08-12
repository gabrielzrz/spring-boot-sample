package gabrielzrz.com.github.Service;

import gabrielzrz.com.github.Service.contract.BranchService;
import gabrielzrz.com.github.constants.RepositoryAdapterConstants;
import gabrielzrz.com.github.model.Branch;
import gabrielzrz.com.github.repository.port.BranchRepositoryPort;
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
