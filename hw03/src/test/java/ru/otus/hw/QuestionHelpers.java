package ru.otus.hw;

import static org.apache.commons.lang3.RandomStringUtils.random;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.stream.IntStreams;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

public class QuestionHelpers {
    public static List<Question> generateQuestions(int questionCount, int minAnswersCount, int maxAnswersCount) {
        return IntStreams.range(questionCount)
            .mapToObj(ign -> {
                var answersCount = ThreadLocalRandom.current().nextInt(minAnswersCount, maxAnswersCount + 1);
                return generateQuestion(answersCount);
            })
            .toList();
    }

    public static Question generateQuestion(int answersCount) {
        var correctedPosition = ThreadLocalRandom.current().nextInt(0, answersCount);
        var answers = IntStreams.range(answersCount)
            .mapToObj(position -> new Answer(random(10), correctedPosition == position ))
            .toList();
        return new Question(random(10), answers);
    }

    public static Map<Question, Integer> correctedAnswerPositionByQuestionMap(List<Question> questions) {
        var map = ImmutableMap.<Question, Integer>builderWithExpectedSize(questions.size());
        for(var question : questions) {
            var correctedPosition = correctedPosition(question);
            map.put(question, correctedPosition);
        }
        return map.build();
    }

    public static int correctedPosition(Question question) {
        for (int i = 0; i < question.answers().size(); i++) {
            if (question.answers().get(i).isCorrect()) {
                return i;
            }
        }
        return -1;
    }

    public static Map<Question, Integer> randomAnswerPositionByQuestionMap(List<Question> questions) {
        var map = ImmutableMap.<Question, Integer>builderWithExpectedSize(questions.size());
        for(var question : questions) {
            var randomPosition = ThreadLocalRandom.current().nextInt(0, question.answers().size());
            map.put(question, randomPosition);
        }
        return map.build();
    }
}
