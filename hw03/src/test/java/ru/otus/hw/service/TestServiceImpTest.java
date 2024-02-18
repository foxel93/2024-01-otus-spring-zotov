package ru.otus.hw.service;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
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
}
