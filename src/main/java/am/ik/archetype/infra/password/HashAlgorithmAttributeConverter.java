package am.ik.archetype.infra.password;

import am.ik.archetype.domain.model.Password;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class HashAlgorithmAttributeConverter implements AttributeConverter<Password.HashAlgorithm, String> {
    @Override
    public String convertToDatabaseColumn(Password.HashAlgorithm algorithm) {
        return algorithm.getCode();
    }

    @Override
    public Password.HashAlgorithm convertToEntityAttribute(String s) {
        return Password.HashAlgorithm.fromCode(s);
    }
}
