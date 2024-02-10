package ru.otus.hw.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            printQuestion(question);

            var expectedAnswerId = expectedAnswerId(question);
            var actualAnswerId = actualAnswerId(question);

            var isAnswerValid = expectedAnswerId == actualAnswerId;
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        for (int i = 0; i < question.answers().size(); i++) {
            ioService.printFormattedLine("\t[%d]: %s", i, question.answers().get(i).text());
        }
    }

    private int expectedAnswerId(Question question) {
        for (int i = 0; i < question.answers().size(); i++) {
            if (question.answers().get(i).isCorrect()) {
                return i;
            }
        }
        return -1;
    }

    private int actualAnswerId(Question question) {
        return ioService.readIntForRange(0, question.answers().size() - 1, "Invalid answer");
    }
}
