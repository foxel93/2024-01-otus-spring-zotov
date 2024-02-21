package ru.otus.hw.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.QuestionHelpers;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

public class TestServiceImpTest {
    @Test
    void executeTest_question_callCount() {
        var ioServiceMock = Mockito.mock(LocalizedIOService.class);
        var questionDaoMock = Mockito.mock(QuestionDao.class);
        var service = new TestServiceImpl(ioServiceMock, questionDaoMock);
        var student = new Student("some_name", "some_surname");

        service.executeTestFor(student);
        Mockito.verify(questionDaoMock, times(1)).findAll();
    }

    @Test
    void executeTest_question_correctResult() {
        var ioServiceMock = Mockito.mock(LocalizedIOService.class);
        var questionDaoMock = Mockito.mock(QuestionDao.class);

        var service = new TestServiceImpl(ioServiceMock, questionDaoMock);
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
