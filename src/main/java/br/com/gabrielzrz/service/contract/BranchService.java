package br.com.gabrielzrz.service.contract;

import br.com.gabrielzrz.model.Branch;

import java.util.List;

/**
 * @author Zorzi
 */
public interface BranchService {

    List<Branch> findAll();
}
