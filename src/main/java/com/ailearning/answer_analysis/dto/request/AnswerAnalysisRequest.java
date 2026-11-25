package com.ailearning.answer_analysis.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class AnswerAnalysisRequest {

    @NotNull(message = "Activity ID is required")
    @Positive(message = "Activity ID must be positive")
    private Long activityId;

    @NotNull(message = "Attempt ID is required")
    @Positive(message = "Attempt ID must be positive")
    private Long attemptId;

    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be positive")
    private Long studentId;

    // Overall activity format: QUIZ, TIMED_QUIZ, POLL, etc.
    private String activityType;

    @NotEmpty(message = "Questions are required for analysis")
    @Valid
    private List<QuestionAnswerRequest> questions;

    public AnswerAnalysisRequest() {
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public List<QuestionAnswerRequest> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionAnswerRequest> questions) {
        this.questions = questions;
    }
}
