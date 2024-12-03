package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.repositories.mongo.MongoBookRepository;

import javax.sql.DataSource;
import java.util.HashMap;

@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class JobConfig {
    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private static final int CHUNK_SIZE = 5;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final DataSource dataSource;

    private final MongoBookRepository mongoBookRepository;

    @Bean
    public Job importLibraryJob(Step createTempAuthorTable,
                                Step authorMigrationStep,
                                Step bookMigrationStep,
                                Step dropTempAuthorTable
    ) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(bookMigrationStep)
                .build();
    }

    @Bean
    public Step bookMigrationStep(RepositoryItemReader<MongoBook> reader,
                                  CompositeItemWriter<BookDto> writer, ItemProcessor<MongoBook, BookDto> processor) {
        return new StepBuilder("bookMigrationStep", jobRepository)
                .<MongoBook, BookDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoBook> bookReader() {
        return new RepositoryItemReaderBuilder<MongoBook>()
                .name("bookReader")
                .repository(mongoBookRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public ItemProcessor<MongoBook, BookDto> bookProcessor() {
        return (book) -> new BookDto(book.getId(), book.getTitle());
    }

    @Bean
    public CompositeItemWriter<BookDto> compositeBookWriter(JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter) {
        return new CompositeItemWriterBuilder<BookDto>()
                .delegates(bookJdbcBatchItemWriter)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<BookDto>()
                .itemPreparedStatementSetter((bookDto, statement) -> {
                    statement.setString(1, bookDto.getTitle());
                })
                .sql("INSERT INTO books(title, id) VALUES (?, nextval('book_id_seq'))")
                .dataSource(dataSource)
                .build();
    }
}
