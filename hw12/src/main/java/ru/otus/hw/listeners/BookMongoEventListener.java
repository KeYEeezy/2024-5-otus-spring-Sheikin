package ru.otus.hw.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Component
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {
    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> deleteEvent) {
        super.onBeforeDelete(deleteEvent);
        String bookId = String.valueOf(deleteEvent.getSource().get("_id"));
        commentRepository.deleteAllByBookId(bookId);
    }
}
