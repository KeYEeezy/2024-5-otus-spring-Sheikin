package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.utils.AbstractTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvQuestionDaoTest extends AbstractTest {

    @Test
    @DisplayName("Положительный сценарий CsvQuestionDao с корректным путем к .csv")
    public void testFindAllCorrectFilePath() {
        List<Question> questions = csvQuestionDao.findAll();
        assertFalse(questions.isEmpty());
        assertEquals(3, questions.size());
        assertEquals("Question 1?", questions.get(0).text());
        assertEquals("Answer 1", questions.get(0).answers().get(0).text());
    }

    @Test
    @DisplayName("Негативный сценарий CsvQuestionDao с некорректным путем к .csv")
    public void testFindAllIncorrectFilePath() {
        var properties = new AppProperties();
        properties.setRightAnswersCountToPass(1);
        properties.setFileNameByLocaleTag(Map.of("en-US", "incorrect-questions.csv"));
        properties.setLocale("en-US");
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(properties);

        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);

    }
}