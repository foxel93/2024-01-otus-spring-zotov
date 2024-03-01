package ru.otus.hw.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.QuestionHelpers;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

@SpringBootTest
public class TestServiceImpTest {
    @MockBean
    LocalizedIOService ioServiceMock;

    @MockBean
    private QuestionDao questionDaoMock;

    @Autowired
    private TestService service;

    @BeforeEach
    void setUp() {
        Mockito.reset(ioServiceMock, questionDaoMock);
    }

    @Test
    void executeTest_question_callCount() {
        var student = new Student("some_name", "some_surname");

        service.executeTestFor(student);
        Mockito.verify(questionDaoMock, times(1)).findAll();
    }

    @Test
    void executeTest_question_correctResult() {
        var student = new Student("some_name", "some_surname");

        var questions = QuestionHelpers.generateQuestions(10, 2, 2);
        var correctedAnswerPositionByQuestionMap = QuestionHelpers.correctedAnswerPositionByQuestionMap(questions);
        var userAnswerPositionByQuestionMap = QuestionHelpers.randomAnswerPositionByQuestionMap(questions);
        var expectedCorrectedAnswers = correctedAnswers(questions, correctedAnswerPositionByQuestionMap, userAnswerPositionByQuestionMap);

        when(questionDaoMock.findAll()).thenReturn(questions);
        var stub = when(ioServiceMock.readIntForRangeLocalized(anyInt(), anyInt(), anyString(), Mockito.any(Object[].class)));
        for (var question : questions) {
            stub = stub.thenReturn(userAnswerPositionByQuestionMap.get(question));
        }

        var result = service.executeTestFor(student);
        assertEquals(expectedCorrectedAnswers, result.getRightAnswersCount());
    }

    private static long correctedAnswers(
        List<Question> questions,
        Map<Question, Integer> correctedAnswerPositionByQuestionMap,
        Map<Question, Integer> userAnswerPositionByQuestionMap
    ) {
        return questions.stream()
            .filter(question -> isCorrectedAnswer(
                question,
                correctedAnswerPositionByQuestionMap,
                userAnswerPositionByQuestionMap))
            .count();
    }

    private static boolean isCorrectedAnswer(
        Question question,
        Map<Question, Integer> correctedAnswerPositionByQuestionMap,
        Map<Question, Integer> userAnswerPositionByQuestionMap
    ) {
        var correctedPosition = correctedAnswerPositionByQuestionMap.get(question);
        var userPosition = userAnswerPositionByQuestionMap.get(question);
        return Objects.equals(correctedPosition, userPosition);
    }
}
