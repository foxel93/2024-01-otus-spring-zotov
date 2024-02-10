package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
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
            var isAnswerValid = false; // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

//    @Override
//    public void executeTest() {
//        ioService.printLine("\nPlease answer the questions below\n");
//        questionDao.findAll().forEach(this::printQuestion);
//    }
//
//    private void printQuestion(Question question) {
//        ioService.printLine(question.text());
//        for (int i = 0; i < question.answers().size(); i++) {
//            ioService.printFormattedLine("\t[%d]: %s", i, question.answers().get(i).text());
//        }
//    }
}
