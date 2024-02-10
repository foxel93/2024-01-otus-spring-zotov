package ru.otus.hw.dao;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import org.apache.commons.lang3.stream.IntStreams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.hw.Helpers;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

public class CsvQuestionDaoTest {
    private static Stream<Arguments> testParams() {
        return Stream.of(
            Arguments.of(generateQuestions(0, 1, 1)),
            Arguments.of(generateQuestions(1, 1, 1)),
            Arguments.of(generateQuestions(1, 2, 2)),
            Arguments.of(generateQuestions(2, 2, 2)),
            Arguments.of(generateQuestions(10, 1, 5))
        );
    }

    private File file;

    @BeforeEach
    void beforeEach() throws IOException {
        Helpers.tryDeleteFile(file);
        file = Helpers.createTempFileInResources("questions_", ".csv");
    }

    @AfterEach
    void afterEach() {
        Helpers.tryDeleteFile(file);
    }

    @ParameterizedTest(name = "{index} - expected questions: [{0}]")
    @MethodSource("testParams")
    @DisplayName("Find all questions")
    public void findAll_csvFile_questions(List<Question> questions) throws IOException {
        writeToFile(questions);
        var csv = new CsvQuestionDao(provider(file.getName()));

        var result = csv.findAll();

        Assertions.assertEquals(questions, result);
    }

    private TestFileNameProvider provider(String fileName) {
        return () -> fileName;
    }

    private void writeToFile(List<Question> questions) throws FileNotFoundException {
        try (var pw = new PrintWriter(file)) {
            questions.stream()
                .map(this::toCsvString)
                .forEach(pw::println);
        }
    }

    private String toCsvString(Question question) {
        var builder = new StringBuilder(question.text());
        var delimiter = ":";
        for (var answer : question.answers()) {
            builder
                .append(delimiter)
                .append(answer.text())
                .append("%")
                .append(answer.isCorrect());
            delimiter = "|";
        }
        return builder.toString();
    }

    private static List<Question> generateQuestions(int questionCount, int minAnswersCount, int maxAnswersCount) {
        return IntStreams.range(questionCount)
            .mapToObj(ign -> {
                var answersCount = ThreadLocalRandom.current().nextInt(minAnswersCount, maxAnswersCount + 1);
                return generateQuestion(answersCount);
            })
            .toList();
    }

    private static Question generateQuestion(int answersCount) {
        var answers = IntStreams.range(answersCount)
            .mapToObj(ign -> new Answer(random(10), ThreadLocalRandom.current().nextBoolean()))
            .toList();
        return new Question(random(10), answers);
    }
}
