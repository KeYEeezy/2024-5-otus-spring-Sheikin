package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object ...args);

    void printQuestions(List<Question> questions);
}