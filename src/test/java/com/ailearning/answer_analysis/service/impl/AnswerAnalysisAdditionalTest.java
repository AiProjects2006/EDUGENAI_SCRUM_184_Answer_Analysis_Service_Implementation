
package com.ailearning.answer_analysis.service.impl;

import com.ailearning.answer_analysis.dto.request.AnswerAnalysisRequest;
import com.ailearning.answer_analysis.dto.request.QuestionAnswerRequest;
import com.ailearning.answer_analysis.dto.response.AnswerAnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnswerAnalysisAdditionalTest {

    private AnswerAnalysisServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AnswerAnalysisServiceImpl();
    }

    private AnswerAnalysisRequest request(
            QuestionAnswerRequest... questions) {

        AnswerAnalysisRequest request = new AnswerAnalysisRequest();
        request.setActivityId(1L);
        request.setAttemptId(10L);
        request.setStudentId(100L);
        request.setQuestions(List.of(questions));
        return request;
    }

    private QuestionAnswerRequest mcq(
            int number, String studentAnswer, String correctAnswer,
            double marks) {

        QuestionAnswerRequest question = new QuestionAnswerRequest();
        question.setQuestionNumber(number);
        question.setQuestionType("MCQ");
        question.setStudentAnswer(studentAnswer);
        question.setCorrectAnswer(correctAnswer);
        question.setMaxMarks(marks);
        return question;
    }

    @Test
    void shouldCalculateMixedAnswersCorrectly() {
        AnswerAnalysisResponse result = service.analyzeAnswers(request(
                mcq(1, "A", "A", 2.0),
                mcq(2, "B", "A", 2.0),
                mcq(3, " ", "B", 2.0)
        ));

        assertEquals(3, result.getTotalQuestions());
        assertEquals(1, result.getCorrectAnswers());
        assertEquals(1, result.getIncorrectAnswers());
        assertEquals(1, result.getUnansweredQuestions());
        assertEquals(2, result.getAnsweredQuestions());
        assertEquals(6.0, result.getTotalPossibleMarks(), 0.001);
        assertEquals(2.0, result.getMarksEarned(), 0.001);
        assertEquals(33.33, result.getScorePercentage(), 0.001);
        assertEquals(50.0, result.getAccuracyPercentage(), 0.001);
        assertEquals(3, result.getQuestionResults().size());
    }

    @Test
    void shouldHandleAllQuestionsUnanswered() {
        AnswerAnalysisResponse result = service.analyzeAnswers(request(
                mcq(1, null, "A", 1.0),
                mcq(2, "  ", "B", 1.0)
        ));

        assertEquals(0, result.getAnsweredQuestions());
        assertEquals(2, result.getUnansweredQuestions());
        assertEquals(0.0, result.getAccuracyPercentage(), 0.001);
        assertEquals(0.0, result.getScorePercentage(), 0.001);
        assertTrue(result.getPerformanceSummary().contains("No questions"));
    }

    @Test
    void shouldHandleZeroMaximumMarks() {
        AnswerAnalysisResponse result = service.analyzeAnswers(request(
                mcq(1, "A", "A", 0.0)
        ));

        assertEquals(0.0, result.getTotalPossibleMarks(), 0.001);
        assertEquals(0.0, result.getScorePercentage(), 0.001);
        assertEquals(100.0, result.getAccuracyPercentage(), 0.001);
    }

    @Test
    void shouldEvaluateMatchingAnswers() {
        QuestionAnswerRequest question = new QuestionAnswerRequest();
        question.setQuestionNumber(1);
        question.setQuestionType("MATCHING");
        question.setMaxMarks(2.0);
        question.setCorrectMatchingAnswers(
                Map.of("Apple", "Fruit", "Carrot", "Vegetable"));
        question.setStudentMatchingAnswers(
                Map.of("apple", " fruit ", "CARROT", "VEGETABLE"));

        AnswerAnalysisResponse result =
                service.analyzeAnswers(request(question));

        assertEquals(1, result.getCorrectAnswers());
        assertEquals(2.0, result.getMarksEarned(), 0.001);
        assertEquals(
                Map.of("apple", " fruit ", "CARROT", "VEGETABLE"),
                result.getQuestionResults()
                        .get(0).getStudentMatchingAnswers());
    }

    @Test
    void shouldRejectDuplicateQuestionNumbers() {
        AnswerAnalysisRequest request = request(
                mcq(1, "A", "A", 1.0),
                mcq(1, "B", "B", 1.0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.analyzeAnswers(request));
    }

    @Test
    void shouldRejectUnsupportedQuestionTypes() {
        QuestionAnswerRequest question = mcq(1, "A", "A", 1.0);
        question.setQuestionType("UNKNOWN_TYPE");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.analyzeAnswers(request(question))
        );
    }

    @Test
    void shouldRejectMissingCorrectAnswer() {
        QuestionAnswerRequest question = mcq(1, "A", null, 1.0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.analyzeAnswers(request(question)));
    }

    @Test
    void shouldRejectInvalidMaximumMarks() {
        QuestionAnswerRequest question = mcq(1, "A", "A", -1.0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.analyzeAnswers(request(question)));
    }

    @Test
    void shouldRejectNullRequest() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.analyzeAnswers(null));
    }
}
