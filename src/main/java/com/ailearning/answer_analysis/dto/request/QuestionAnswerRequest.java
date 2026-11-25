package com.ailearning.answer_analysis.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.Map;

public class QuestionAnswerRequest {

    @NotNull(message = "Question number is required")
    @Positive(message = "Question number must be positive")
    private Integer questionNumber;

    @NotBlank(message = "Question type is required")
    private String questionType;

    @NotNull(message = "Maximum marks are required")
    @PositiveOrZero(message = "Maximum marks cannot be negative")
    private Double maxMarks;

    // MCQ, TRUE_FALSE, FILL_BLANK, SHORT_ANSWER
    private String studentAnswer;

    // Correct answer from a trusted server-side source
    private String correctAnswer;

    // MATCHING
    private Map<String, String> studentMatchingAnswers;
    private Map<String, String> correctMatchingAnswers;

    // FILL_BLANK, SHORT_ANSWER
    private List<String> acceptedAnswers;

    // SORTING
    private List<String> studentSequence;
    private List<String> correctSequence;

    // DRAG_DROP: item ID -> drop-zone ID
    private Map<String, String> studentPlacements;
    private Map<String, String> correctPlacements;

    // HOTSPOT
    private String studentHotspotId;
    private String correctHotspotId;

    // ESSAY, PROBLEM_SOLVING, APPLICATION_BASED
    // These evaluation fields must be populated by a trusted evaluator.
    private Double rubricMarks;
    private String evaluationFeedback;
    private boolean manualReviewRequired;

    public QuestionAnswerRequest() {
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Double getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(Double maxMarks) {
        this.maxMarks = maxMarks;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Map<String, String> getStudentMatchingAnswers() {
        return studentMatchingAnswers;
    }

    public void setStudentMatchingAnswers(
            Map<String, String> studentMatchingAnswers) {
        this.studentMatchingAnswers = studentMatchingAnswers;
    }

    public Map<String, String> getCorrectMatchingAnswers() {
        return correctMatchingAnswers;
    }

    public void setCorrectMatchingAnswers(
            Map<String, String> correctMatchingAnswers) {
        this.correctMatchingAnswers = correctMatchingAnswers;
    }

    public List<String> getAcceptedAnswers() {
        return acceptedAnswers;
    }

    public void setAcceptedAnswers(List<String> acceptedAnswers) {
        this.acceptedAnswers = acceptedAnswers;
    }

    public List<String> getStudentSequence() {
        return studentSequence;
    }

    public void setStudentSequence(List<String> studentSequence) {
        this.studentSequence = studentSequence;
    }

    public List<String> getCorrectSequence() {
        return correctSequence;
    }

    public void setCorrectSequence(List<String> correctSequence) {
        this.correctSequence = correctSequence;
    }

    public Map<String, String> getStudentPlacements() {
        return studentPlacements;
    }

    public void setStudentPlacements(
            Map<String, String> studentPlacements) {
        this.studentPlacements = studentPlacements;
    }

    public Map<String, String> getCorrectPlacements() {
        return correctPlacements;
    }

    public void setCorrectPlacements(
            Map<String, String> correctPlacements) {
        this.correctPlacements = correctPlacements;
    }

    public String getStudentHotspotId() {
        return studentHotspotId;
    }

    public void setStudentHotspotId(String studentHotspotId) {
        this.studentHotspotId = studentHotspotId;
    }

    public String getCorrectHotspotId() {
        return correctHotspotId;
    }

    public void setCorrectHotspotId(String correctHotspotId) {
        this.correctHotspotId = correctHotspotId;
    }

    public Double getRubricMarks() {
        return rubricMarks;
    }

    public void setRubricMarks(Double rubricMarks) {
        this.rubricMarks = rubricMarks;
    }

    public String getEvaluationFeedback() {
        return evaluationFeedback;
    }

    public void setEvaluationFeedback(String evaluationFeedback) {
        this.evaluationFeedback = evaluationFeedback;
    }

    public boolean isManualReviewRequired() {
        return manualReviewRequired;
    }

    public void setManualReviewRequired(boolean manualReviewRequired) {
        this.manualReviewRequired = manualReviewRequired;
    }
}