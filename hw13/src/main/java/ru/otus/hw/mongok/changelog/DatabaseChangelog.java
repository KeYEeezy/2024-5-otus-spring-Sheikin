package ru.otus.hw.mongok.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.repositories.mongo.MongoBookRepository;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "drop", author = "nsheykin", runAlways = true)
    public void drop(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "init", author = "nsheykin")
    public void init(MongoBookRepository bookRepository) {
        MongoBook book1 = bookRepository.save(new MongoBook("1", "Crime and Punishment"));
        MongoBook book2 = bookRepository.save(new MongoBook("2", "A Wizard of Earthsea"));
        MongoBook book3 = bookRepository.save(new MongoBook("3", "Labyrinth of Reflections"));
    }
}
