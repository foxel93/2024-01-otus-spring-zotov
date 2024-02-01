package ru.otus.hw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

public class TestServiceImpTest {
    @Test
    void correctAnswerPosition_question_position() {
        var ioServiceMock = Mockito.mock(IOService.class);
        var questionDaoMock = Mockito.mock(QuestionDao.class);
        var service = new TestServiceImpl(ioServiceMock, questionDaoMock);

        var answersCount = 100;
        var expectedPos = ThreadLocalRandom.current().nextInt(0, answersCount);
        var answers = generateAnswers(answersCount, expectedPos);
        var question = new Question("some", answers);

        var actualPosition = service.correctAnswerPosition(question);

        Assertions.assertEquals(expectedPos, actualPosition);
    }

    private List<Answer> generateAnswers(int answersCount, int correctAnswerPosition) {
        var correctAnswer = new Answer("text", true);

        var answers = new ArrayList<Answer>(answersCount);
        for (int i = 1; i < answersCount; i++) {
            answers.add(new Answer("text" + i, false));
        }

        answers.add(correctAnswerPosition, correctAnswer);

        return answers;
    }
}
