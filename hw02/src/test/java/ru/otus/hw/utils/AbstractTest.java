package ru.otus.hw.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Student;



@ExtendWith(MockitoExtension.class)
public class AbstractTest {

    protected static CsvQuestionDao csvQuestionDao;
    protected static Student testStudent;

    @BeforeAll
    static void init() {
        var properties = new AppProperties(1, "test-questions.csv");
        csvQuestionDao = new CsvQuestionDao(properties);
        testStudent = new Student("Nikita", "Sheikin");
    }
}
