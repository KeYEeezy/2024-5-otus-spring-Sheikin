package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.utils.AbstractTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class TestServiceImplTest extends AbstractTest {
    @Autowired
    private TestServiceImpl testService;

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Test
    void executePositiveTest() {
        when(questionDao.findAll()).thenReturn(csvQuestionDao.findAll());

        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), any(), any())).thenReturn(1);

        var testResult = testService.executeTestFor(testStudent);

        assertEquals(2, testResult.getRightAnswersCount());
    }

    @Test
    void executeNegativeTest() {
        when(questionDao.findAll()).thenReturn(csvQuestionDao.findAll());

        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), any(), any())).thenReturn(3);

        var testResult = testService.executeTestFor(testStudent);

        assertEquals(0, testResult.getRightAnswersCount());
    }
}