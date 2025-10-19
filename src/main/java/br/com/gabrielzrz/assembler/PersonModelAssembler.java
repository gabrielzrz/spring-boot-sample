package br.com.gabrielzrz.assembler;

import br.com.gabrielzrz.controllers.PersonController;
import br.com.gabrielzrz.dto.PersonDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Zorzi
 */
@Component
public class PersonModelAssembler implements RepresentationModelAssembler<PersonDTO, EntityModel<PersonDTO>> {

    @Override
    public EntityModel<PersonDTO> toModel(PersonDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PersonController.class).findById(entity.getId()))
                        .withSelfRel()
                        .withType("GET"),
                linkTo(methodOn(PersonController.class).findAll(null, null, "ASC"))
                        .withRel("findAll")
                        .withType("GET"),
                linkTo(methodOn(PersonController.class).create(entity))
                        .withRel("create")
                        .withType("POST"),
                linkTo(methodOn(PersonController.class).update(entity))
                        .withRel("update")
                        .withType("PUT"),
                linkTo(methodOn(PersonController.class).delete(entity.getId()))
                        .withRel("delete")
                        .withType("DELETE")
        );
    }
}
