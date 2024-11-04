package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.hw.models.security.User;
import ru.otus.hw.models.security.Role;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDetailsMapper {

    default UserDetails toSecurityUser(User source) {
        var roles = source.getRoles()
                .stream()
                .map(Role::getName)
                .toArray(String[]::new);
        return org.springframework.security.core.userdetails.User.builder()
                .username(source.getUsername())
                .password(source.getPassword())
                .roles(roles)
                .build();
    }


}
