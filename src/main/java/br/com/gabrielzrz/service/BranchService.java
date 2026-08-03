package br.com.gabrielzrz.service;

import br.com.gabrielzrz.entity.Branch;
import br.com.gabrielzrz.repository.jpa.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zorzi
 */
@Service
public class BranchService {

    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public List<Branch> findAll() {
        return branchRepository.findAll();
    }
}
