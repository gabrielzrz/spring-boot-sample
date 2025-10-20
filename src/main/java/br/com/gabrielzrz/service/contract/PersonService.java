package br.com.gabrielzrz.service.contract;

import br.com.gabrielzrz.dto.request.filters.PersonFilterRequest;
import br.com.gabrielzrz.dto.request.PersonRequestDTO;
import br.com.gabrielzrz.dto.response.ImportResultDTO;
import br.com.gabrielzrz.dto.response.PersonResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Zorzi
 */
public interface PersonService {

    PersonResponseDTO findById(UUID id);

    Page<PersonResponseDTO> findAll(PersonFilterRequest personFilterRequest, Pageable pageable);

    Page<PersonResponseDTO> findAll(Pageable pageable);

    PersonResponseDTO create(PersonRequestDTO personRequestDTO);

    void update(PersonRequestDTO personRequestDTO);

    void delete(UUID id);

    PersonResponseDTO disablePerson(UUID id);

    ImportResultDTO massCreation(MultipartFile file);
}
