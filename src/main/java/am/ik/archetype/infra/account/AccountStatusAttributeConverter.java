package am.ik.archetype.infra.account;

import am.ik.archetype.domain.model.AccountStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AccountStatusAttributeConverter implements AttributeConverter<AccountStatus, String> {
    @Override
    public String convertToDatabaseColumn(AccountStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public AccountStatus convertToEntityAttribute(String dbData) {
        return AccountStatus.fromCode(dbData);
    }
}
