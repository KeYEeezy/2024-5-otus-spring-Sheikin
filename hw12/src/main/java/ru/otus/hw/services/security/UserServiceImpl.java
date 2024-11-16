package ru.otus.hw.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.mappers.UserDetailsMapper;
import ru.otus.hw.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository jpaUserRepository;

    private final UserDetailsMapper userDetailsMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user =  jpaUserRepository.findByUsername(username)
                .map(userDetailsMapper::toSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with name %s not found".formatted(username)));
        return user;
    }
}
