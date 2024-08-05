package ru.otus.hw.utils;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Student;

import java.util.Map;

@SpringBootTest
public class AbstractTest {

    protected static CsvQuestionDao csvQuestionDao;
    protected static Student testStudent;

    @BeforeAll
    static void init() {
        var properties = new AppProperties();
        properties.setRightAnswersCountToPass(1);
        properties.setFileNameByLocaleTag(Map.of("en-US", "test-questions.csv"));
        properties.setLocale("en-US");
        csvQuestionDao = new CsvQuestionDao(properties);
        testStudent = new Student("Nikita", "Sheikin");
    }
}
