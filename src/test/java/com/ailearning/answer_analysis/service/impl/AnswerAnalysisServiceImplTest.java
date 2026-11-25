
package com.ailearning.answer_analysis.service.impl;

import com.ailearning.answer_analysis.dto.request.AnswerAnalysisRequest;
import com.ailearning.answer_analysis.dto.request.QuestionAnswerRequest;
import com.ailearning.answer_analysis.dto.response.AnswerAnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnswerAnalysisServiceImplTest {

    private AnswerAnalysisServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AnswerAnalysisServiceImpl();
    }

    private AnswerAnalysisRequest createRequest(
            QuestionAnswerRequest... questions) {

        AnswerAnalysisRequest request =
                new AnswerAnalysisRequest();

        request.setActivityId(1L);
        request.setAttemptId(101L);
        request.setStudentId(5L);
        request.setQuestions(List.of(questions));

        return request;
    }

    private QuestionAnswerRequest createQuestion(
            int number,
            String type,
            double marks,
            String studentAnswer,
            String correctAnswer) {

        QuestionAnswerRequest question =
                new QuestionAnswerRequest();

        question.setQuestionNumber(number);
        question.setQuestionType(type);
        question.setMaxMarks(marks);
        question.setStudentAnswer(studentAnswer);
        question.setCorrectAnswer(correctAnswer);

        return question;
    }

    @Test
    void shouldAwardFullMarksForCorrectMCQ() {
        AnswerAnalysisRequest request = createRequest(
                createQuestion(1, "MCQ", 2, "A", "A")
        );

        AnswerAnalysisResponse response =
                service.analyzeAnswers(request);

        assertEquals(1, response.getCorrectAnswers());
        assertEquals(0, response.getIncorrectAnswers());
        assertEquals(2.0, response.getMarksEarned());
        assertEquals(100.0, response.getScorePercentage());
    }

    @Test
    void shouldMarkIncorrectTrueFalseAnswer() {
        AnswerAnalysisRequest request = createRequest(
                createQuestion(1, "TRUE_FALSE", 1,
                        "False", "True")
        );

        AnswerAnalysisResponse response =
                service.analyzeAnswers(request);

        assertEquals(0, response.getCorrectAnswers());
        assertEquals(1, response.getIncorrectAnswers());
        assertEquals(0.0, response.getMarksEarned());
        assertEquals(0.0, response.getScorePercentage());
    }

    @Test
    void shouldCountUnansweredQuestion() {
        AnswerAnalysisRequest request = createRequest(
                createQuestion(1, "MCQ", 2, "", "B")
        );

        AnswerAnalysisResponse response =
                service.analyzeAnswers(request);

        assertEquals(1, response.getUnansweredQuestions());
        assertEquals(0, response.getCorrectAnswers());
        assertEquals(0, response.getIncorrectAnswers());
        assertEquals(0.0, response.getMarksEarned());
    }

    @Test
    void shouldAwardFullMarksForCorrectMatching() {
        QuestionAnswerRequest question =
                createQuestion(1, "MATCHING", 4, "", "");

        question.setStudentMatchingAnswers(Map.of(
                "Java", "Programming Language",
                "MySQL", "Database"
        ));

        question.setCorrectMatchingAnswers(Map.of(
                "Java", "Programming Language",
                "MySQL", "Database"
        ));

        AnswerAnalysisResponse response =
                service.analyzeAnswers(createRequest(question));

        assertEquals(1, response.getCorrectAnswers());
        assertEquals(4.0, response.getMarksEarned());
        assertEquals(100.0, response.getScorePercentage());
    }

    @Test
    void shouldCalculateTotalScoreAndPercentage() {
        AnswerAnalysisRequest request = createRequest(
                createQuestion(1, "MCQ", 2, "A", "A"),
                createQuestion(2, "TRUE_FALSE", 1,
                        "False", "True"),
                createQuestion(3, "MCQ", 2, "", "B")
        );

        AnswerAnalysisResponse response =
                service.analyzeAnswers(request);

        assertEquals(3, response.getTotalQuestions());
        assertEquals(1, response.getCorrectAnswers());
        assertEquals(1, response.getIncorrectAnswers());
        assertEquals(1, response.getUnansweredQuestions());
        assertEquals(5.0, response.getTotalPossibleMarks());
        assertEquals(2.0, response.getMarksEarned());
        assertEquals(40.0, response.getScorePercentage());
    }

    @Test
    void shouldRejectDuplicateQuestionNumbers() {
        AnswerAnalysisRequest request = createRequest(
                createQuestion(1, "MCQ", 2, "A", "A"),
                createQuestion(1, "MCQ", 2, "B", "B")
        );

        assertThrows(IllegalArgumentException.class,
                () -> service.analyzeAnswers(request));
    }

    @Test
    void shouldRejectUnsupportedQuestionType() {
        AnswerAnalysisRequest request = createRequest(
                createQuestion(
                        1, "UNKNOWN_TYPE", 5, "My answer", "Expected"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.analyzeAnswers(request)
        );
    }


    @Test
    void shouldRejectBlankCorrectMatchingPair() {
        QuestionAnswerRequest question =
                createQuestion(1, "MATCHING", 4, "", "");

        question.setStudentMatchingAnswers(
                Map.of("Java", "Programming Language"));

        question.setCorrectMatchingAnswers(
                Map.of("Java", " "));

        AnswerAnalysisRequest request = createRequest(question);

        assertThrows(IllegalArgumentException.class,
                () -> service.analyzeAnswers(request));
    }

}
