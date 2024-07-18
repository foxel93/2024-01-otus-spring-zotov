package ru.otus.hw.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.hw.models.Role;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute.roleName();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return Role.of(dbData);
    }
}
