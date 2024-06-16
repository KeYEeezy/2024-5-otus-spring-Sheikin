package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        printQuestions(csvQuestionDao.findAll());
    }

    private void printQuestions(List<Question> questions) {
        for (Question question : questions) {
            ioService.printFormattedLine("Question: %s", question.text());
            List<Answer> answers = question.answers();
            var answerNumber = 1;
            for (Answer answer : answers) {
                ioService.printFormattedLine("   %d. %s - %s", answerNumber, answer.text(), answer.isCorrect());
                answerNumber++;
            }
        }
    }
}