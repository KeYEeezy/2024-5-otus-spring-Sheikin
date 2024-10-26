package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.security.LibraryUser;

import java.util.Optional;

public interface UserRepository extends MongoRepository<LibraryUser, String> {

    Optional<LibraryUser> findByUsername(String username);
}
