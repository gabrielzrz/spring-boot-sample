package br.com.gabrielzrz.model.generator;

import br.com.gabrielzrz.model.base.BaseEntityId;
import com.github.f4b6a3.uuid.UuidCreator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

import static java.util.Objects.nonNull;

/**
 * @author Zorzi
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9562.html">RFC 9562 (UUID v7)</a>
 */
public class UuidGeneratorCustom implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        if (object instanceof BaseEntityId entity) {
            UUID id = entity.getId();
            if (nonNull(id) && StringUtils.isNotBlank(id.toString())) {
                return id;
            }
        }
        return UuidCreator.getTimeOrderedEpoch(); // Type 7 UUID
    }

    @Override
    public boolean allowAssignedIdentifiers() {
        return true;
    }
}
