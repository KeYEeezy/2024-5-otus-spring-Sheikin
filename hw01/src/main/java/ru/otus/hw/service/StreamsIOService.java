package ru.otus.hw.service;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.io.PrintStream;
import java.util.List;

public class StreamsIOService implements IOService {
    private final PrintStream printStream;

    public StreamsIOService(PrintStream printStream) {

        this.printStream = printStream;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    public void printQuestions(List<Question> questions) {
        for (Question question : questions) {
            printFormattedLine("Question: %s", question.text());
            List<Answer> answers = question.answers();
            for (Answer answer : answers) {
                printFormattedLine(" - %s", answer.text() + " - " + answer.isCorrect());
            }
        }
    }
}