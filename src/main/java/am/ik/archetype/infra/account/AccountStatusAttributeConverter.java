package am.ik.archetype.infra.account;

import am.ik.archetype.domain.model.AccountState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AccountStatusAttributeConverter implements AttributeConverter<AccountState, String> {
    @Override
    public String convertToDatabaseColumn(AccountState attribute) {
        return attribute.getCode();
    }

    @Override
    public AccountState convertToEntityAttribute(String dbData) {
        return AccountState.fromCode(dbData);
    }
}
