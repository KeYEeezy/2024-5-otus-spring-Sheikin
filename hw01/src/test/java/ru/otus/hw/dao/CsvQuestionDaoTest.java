package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.utils.AbstractTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CsvQuestionDaoTest extends AbstractTest {


    @Test
    @DisplayName("Положительный сценарий CsvQuestionDao с корректным путем к .csv")
    public void testFindAllCorrectFilePath() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(new AppProperties("test-questions.csv"));
        List<Question> questions = csvQuestionDao.findAll();

        assertFalse(questions.isEmpty());
        assertEquals(3, questions.size());
        assertEquals("Вопрос 1?", questions.get(0).text());
        assertEquals("Ответ 1", questions.get(0).answers().get(0).text());
    }

    @Test
    @DisplayName("Негативный сценарий CsvQuestionDao с некорректным путем к .csv")
    public void testFindAllIncorrectFilePath() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(new AppProperties("incorrect-path.csv"));

        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);

    }
}