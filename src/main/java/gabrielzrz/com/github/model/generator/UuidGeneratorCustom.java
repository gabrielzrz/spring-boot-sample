package gabrielzrz.com.github.model.generator;

import gabrielzrz.com.github.model.base.BaseEntityId;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class UuidGeneratorCustom implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        if (object instanceof BaseEntityId entity) {
            UUID id = entity.getId();
            if (nonNull(id) && StringUtils.isNotBlank(id.toString())) {
                return id;
            }
        }
        return UUID.randomUUID(); // Type 4 UUID
    }

    @Override
    public boolean allowAssignedIdentifiers() {
        return true;
    }
}
