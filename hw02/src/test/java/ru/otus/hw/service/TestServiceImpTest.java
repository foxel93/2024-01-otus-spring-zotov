package ru.otus.hw.service;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;

public class TestServiceImpTest {
    @Test
    void executeTest_question_callCount() {
        var ioServiceMock = Mockito.mock(IOService.class);
        var questionDaoMock = Mockito.mock(QuestionDao.class);
        var service = new TestServiceImpl(ioServiceMock, questionDaoMock);

        service.executeTest();
        Mockito.verify(questionDaoMock, times(1)).findAll();
    }
}
