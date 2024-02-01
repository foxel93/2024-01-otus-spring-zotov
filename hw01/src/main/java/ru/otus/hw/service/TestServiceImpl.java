package ru.otus.hw.service;

import com.google.common.annotations.VisibleForTesting;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        var scanner = new Scanner(System.in);

        ioService.printLine("\nPlease answer the questions below\n");

        var result = new TreeMap<String, Boolean>();
        questionDao.findAll().forEach(question -> {
            printQuestion(question);

            ioService.printLine("Input answer number: ");
            var userAnswerPos = scanner.next().trim();
            var expectedPos = Integer.toString(correctAnswerPosition(question));

            result.put(question.text(), expectedPos.equals(userAnswerPos));
        });
        printResult(result);
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        for (int i = 0; i < question.answers().size(); i++) {
            ioService.printFormattedLine("\t[%d]: %s", i, question.answers().get(i).text());
        }
    }

    private void printResult(Map<String, Boolean> result) {
        ioService.printLine("Result:\n");

        int score = 0;
        for (var entry : result.entrySet()) {
            var answer = entry.getKey();
            var status = entry.getValue();

            ioService.printLine("- Answer: " + answer);
            ioService.printLine("  Status: " + (status ? "correct" : "failed"));

            score += (status ? 1 : 0);
        }
        ioService.printFormattedLine("\nScore: %d/%d", score,result.size());
    }

    @VisibleForTesting
    protected int correctAnswerPosition(Question question) {
        for (int i = 0; i < question.answers().size(); i++) {
            if (question.answers().get(i).isCorrect()) {
                return i;
            }
        }
        return -1;
    }
}
