package com.ailearning.answer_analysis.dto.response;

import java.util.List;
import java.util.Map;

public class AnswerAnalysisResponse {

    private Long activityId;
    private Long attemptId;
    private Long studentId;

    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private int unansweredQuestions;

    private double totalPossibleMarks;
    private double evaluatedPossibleMarks;
    private double marksEarned;
    private double scorePercentage;

    private int answeredQuestions;
    private double accuracyPercentage;
    private String performanceSummary;
    private int pendingEvaluationQuestions;

    private List<QuestionResult> questionResults;

    public AnswerAnalysisResponse() {
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

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getUnansweredQuestions() {
        return unansweredQuestions;
    }

    public void setUnansweredQuestions(int unansweredQuestions) {
        this.unansweredQuestions = unansweredQuestions;
    }

    public double getTotalPossibleMarks() {
        return totalPossibleMarks;
    }

    public void setTotalPossibleMarks(double totalPossibleMarks) {
        this.totalPossibleMarks = totalPossibleMarks;
    }

    public double getMarksEarned() {
        return marksEarned;
    }

    public void setMarksEarned(double marksEarned) {
        this.marksEarned = marksEarned;
    }

    public double getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(double scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public int getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(int answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public double getAccuracyPercentage() {
        return accuracyPercentage;
    }

    public void setAccuracyPercentage(double accuracyPercentage) {
        this.accuracyPercentage = accuracyPercentage;
    }

    public String getPerformanceSummary() {
        return performanceSummary;
    }

    public void setPerformanceSummary(String performanceSummary) {
        this.performanceSummary = performanceSummary;
    }

    public List<QuestionResult> getQuestionResults() {
        return questionResults;
    }

    public void setQuestionResults(List<QuestionResult> questionResults) {
        this.questionResults = questionResults;
    }

    public static class QuestionResult {

        private Integer questionNumber;
        private String questionType;
        private String studentAnswer;

        private Map<String, String> studentMatchingAnswers;

        private boolean correct;
        private boolean unanswered;
        private double marksAwarded;
        private double maxMarks;
        private String feedback;
        private boolean pendingEvaluation;
        private String evaluationStatus;
        private List<String> studentSequence;
        private Map<String, String> studentPlacements;
        private String studentHotspotId;

        public QuestionResult() {
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

        public String getStudentAnswer() {
            return studentAnswer;
        }

        public void setStudentAnswer(String studentAnswer) {
            this.studentAnswer = studentAnswer;
        }

        public boolean isCorrect() {
            return correct;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
        }

        public boolean isUnanswered() {
            return unanswered;
        }

        public void setUnanswered(boolean unanswered) {
            this.unanswered = unanswered;
        }

        public double getMarksAwarded() {
            return marksAwarded;
        }

        public void setMarksAwarded(double marksAwarded) {
            this.marksAwarded = marksAwarded;
        }

        public double getMaxMarks() {
            return maxMarks;
        }

        public void setMaxMarks(double maxMarks) {
            this.maxMarks = maxMarks;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public Map<String, String> getStudentMatchingAnswers() {
            return studentMatchingAnswers;
        }

        public void setStudentMatchingAnswers(
                Map<String, String> studentMatchingAnswers) {
            this.studentMatchingAnswers = studentMatchingAnswers;
        }

        public boolean isPendingEvaluation() {
            return pendingEvaluation;
        }

        public void setPendingEvaluation(boolean pendingEvaluation) {
            this.pendingEvaluation = pendingEvaluation;
        }

        public String getEvaluationStatus() {
            return evaluationStatus;
        }

        public void setEvaluationStatus(String evaluationStatus) {
            this.evaluationStatus = evaluationStatus;
        }

        public List<String> getStudentSequence() {
            return studentSequence;
        }

        public void setStudentSequence(List<String> studentSequence) {
            this.studentSequence = studentSequence;
        }

        public Map<String, String> getStudentPlacements() {
            return studentPlacements;
        }

        public void setStudentPlacements(Map<String, String> studentPlacements) {
            this.studentPlacements = studentPlacements;
        }

        public String getStudentHotspotId() {
            return studentHotspotId;
        }

        public void setStudentHotspotId(String studentHotspotId) {
            this.studentHotspotId = studentHotspotId;
        }
    }
    public int getPendingEvaluationQuestions() {
        return pendingEvaluationQuestions;
    }

    public void setPendingEvaluationQuestions(int pendingEvaluationQuestions) {
        this.pendingEvaluationQuestions = pendingEvaluationQuestions;
    }

    public double getEvaluatedPossibleMarks() {
        return evaluatedPossibleMarks;
    }

    public void setEvaluatedPossibleMarks(double evaluatedPossibleMarks) {
        this.evaluatedPossibleMarks = evaluatedPossibleMarks;
    }
}