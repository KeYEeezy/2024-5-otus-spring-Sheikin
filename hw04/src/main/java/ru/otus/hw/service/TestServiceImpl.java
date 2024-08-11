package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private static final String ERROR_MESSAGE = "TestService.error.message.answer";

    private static final String PROMPT = "TestService.chose.answer";

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printFormattedLine(question.text());
            printAnswers(question.answers());
            var isAnswerValid = validateAnswer(question.answers()); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printAnswers(List<Answer> answers) {
        AtomicInteger index = new AtomicInteger(0);
        answers.forEach(answer -> {
            String text = answer.text();
            ioService.printFormattedLine("%d. %s", index.incrementAndGet(), text);
        });
    }

    private boolean validateAnswer(List<Answer> answers) {
        ioService.printLine("");
        var answerNumber = ioService.readIntForRangeWithPromptLocalized(1, answers.size(), PROMPT, ERROR_MESSAGE);
        return answers.get(answerNumber - 1).isCorrect();

    }
}