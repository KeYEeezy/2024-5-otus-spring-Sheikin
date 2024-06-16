package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(getResource(fileNameProvider.getTestFileName()))) {
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build();

            List<QuestionDto> questionDtos = csvToBean.parse();
            for (QuestionDto questionDto : questionDtos) {
                questions.add(questionDto.toDomainObject());
            }
        } catch (QuestionReadException | IOException e) {
            throw new QuestionReadException("Error reading questions from CSV file", e);
        }
        return questions;
    }

    private InputStreamReader getResource(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        InputStream inputStream = classPathResource.getInputStream();
        Objects.requireNonNull(inputStream, "Input stream is null for file: " + fileName);
        return new InputStreamReader(inputStream);
    }
}