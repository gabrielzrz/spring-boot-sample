package br.com.gabrielzrz.assembler;

import br.com.gabrielzrz.controllers.PersonController;
import br.com.gabrielzrz.dto.response.PersonResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Zorzi
 */
@Component
public class PersonModelAssembler implements RepresentationModelAssembler<PersonResponseDTO, EntityModel<PersonResponseDTO>> {

    @Override
    public EntityModel<PersonResponseDTO> toModel(PersonResponseDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PersonController.class).findById(entity.getId()))
                        .withSelfRel()
                        .withType("GET"),
                linkTo(methodOn(PersonController.class).findAll(null, null, "ASC"))
                        .withRel("findAll")
                        .withType("GET"),
//                linkTo(methodOn(PersonController.class).create(entity))
//                        .withRel("create")
//                        .withType("POST"),
//                linkTo(methodOn(PersonController.class).update(entity))
//                        .withRel("update")
//                        .withType("PUT"),
                linkTo(methodOn(PersonController.class).delete(entity.getId()))
                        .withRel("delete")
                        .withType("DELETE")
        );
    }
}
