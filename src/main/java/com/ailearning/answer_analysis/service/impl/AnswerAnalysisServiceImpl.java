
package com.ailearning.answer_analysis.service.impl;

import com.ailearning.answer_analysis.dto.request.AnswerAnalysisRequest;
import com.ailearning.answer_analysis.dto.request.QuestionAnswerRequest;
import com.ailearning.answer_analysis.dto.response.AnswerAnalysisResponse;
import com.ailearning.answer_analysis.service.AnswerAnalysisService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class AnswerAnalysisServiceImpl implements AnswerAnalysisService {


    @Override
    public AnswerAnalysisResponse analyzeAnswers(
            AnswerAnalysisRequest request) {

        validateRequest(request);

        AnswerAnalysisResponse response = new AnswerAnalysisResponse();
        response.setActivityId(request.getActivityId());
        response.setAttemptId(request.getAttemptId());
        response.setStudentId(request.getStudentId());

        int correctCount = 0;
        int incorrectCount = 0;
        int unansweredCount = 0;
        int answeredCount = 0;
        int pendingCount = 0;

        double totalPossibleMarks = 0.0;
        double evaluatedPossibleMarks = 0.0;
        double marksEarned = 0.0;

        List<AnswerAnalysisResponse.QuestionResult> results =
                new ArrayList<>();

        for (QuestionAnswerRequest question : request.getQuestions()) {

            String type = normalizeType(question.getQuestionType());
            double maxMarks = question.getMaxMarks();

            // Polls collect responses but are not scored by default.
            boolean poll = type.equals("POLL");

            if (!poll) {
                totalPossibleMarks += maxMarks;
            }

            boolean unanswered = isUnanswered(question);
            boolean pending = false;
            boolean correct = false;
            double marksAwarded = 0.0;
            String feedback;
            String evaluationStatus;

            if (unanswered) {
                unansweredCount++;
                feedback = "No answer was submitted.";
                evaluationStatus = "UNANSWERED";

            } else {
                answeredCount++;

                if (poll) {
                    feedback = "Poll response recorded. No marks awarded.";
                    evaluationStatus = "NOT_GRADED";

                } else if (requiresManualReview(type)
                        && (question.isManualReviewRequired()
                        || question.getRubricMarks() == null)) {

                    pending = true;
                    pendingCount++;

                    feedback = "Pending rubric-based evaluation or manual review.";
                    evaluationStatus = "PENDING";

                } else if (requiresManualReview(type)) {

                    marksAwarded = question.getRubricMarks();
                    evaluatedPossibleMarks += maxMarks;

                    correct = maxMarks > 0
                            && Double.compare(marksAwarded, maxMarks) == 0;

                    if (correct) {
                        correctCount++;
                        feedback = "Full marks awarded by the rubric.";
                    } else {
                        incorrectCount++;
                        feedback = question.getEvaluationFeedback() != null
                                && !question.getEvaluationFeedback().isBlank()
                                ? question.getEvaluationFeedback()
                                : "Rubric evaluation completed.";

                    }

                    evaluationStatus = "EVALUATED";

                } else {
                    evaluatedPossibleMarks += maxMarks;
                    correct = evaluateQuestion(question);

                    if (correct) {
                        correctCount++;
                        marksAwarded = maxMarks;
                        feedback = "Correct answer.";
                    } else {
                        incorrectCount++;
                        feedback = "Incorrect answer.";
                    }

                    evaluationStatus = "EVALUATED";
                }
            }

            marksEarned += marksAwarded;

            AnswerAnalysisResponse.QuestionResult result =
                    new AnswerAnalysisResponse.QuestionResult();

            result.setQuestionNumber(question.getQuestionNumber());
            result.setQuestionType(type);
            result.setStudentAnswer(question.getStudentAnswer());
            result.setStudentMatchingAnswers(
                    question.getStudentMatchingAnswers());
            result.setStudentSequence(question.getStudentSequence());
            result.setStudentPlacements(question.getStudentPlacements());
            result.setStudentHotspotId(question.getStudentHotspotId());

            result.setCorrect(correct);
            result.setUnanswered(unanswered);
            result.setPendingEvaluation(pending);
            result.setEvaluationStatus(evaluationStatus);
            result.setMarksAwarded(marksAwarded);
            result.setMaxMarks(maxMarks);
            result.setFeedback(feedback);

            results.add(result);
        }

        response.setTotalQuestions(request.getQuestions().size());
        response.setCorrectAnswers(correctCount);
        response.setIncorrectAnswers(incorrectCount);
        response.setUnansweredQuestions(unansweredCount);
        response.setAnsweredQuestions(answeredCount);
        response.setPendingEvaluationQuestions(pendingCount);

        response.setTotalPossibleMarks(totalPossibleMarks);
        response.setEvaluatedPossibleMarks(evaluatedPossibleMarks);
        response.setMarksEarned(marksEarned);

        // Exclude pending-review questions and ungraded polls
        // from the score denominator.
        double scorePercentage = totalPossibleMarks > 0
                ? (marksEarned / totalPossibleMarks) * 100.0
                : 0.0;

        response.setScorePercentage(
                Math.round(scorePercentage * 100.0) / 100.0);

        // Pending questions and ungraded polls do not count as
        // evaluated answers when calculating accuracy.
        int evaluatedAnsweredCount = correctCount + incorrectCount;

        double accuracyPercentage = evaluatedAnsweredCount > 0
                ? ((double) correctCount / evaluatedAnsweredCount) * 100.0
                : 0.0;

        response.setAccuracyPercentage(
                Math.round(accuracyPercentage * 100.0) / 100.0);

        String performanceSummary;

        if (pendingCount > 0) {
            performanceSummary =
                    "Analysis is provisional. Some questions are awaiting evaluation.";
        } else if (answeredCount == 0) {
            performanceSummary =
                    "No questions were answered. Try completing the activity.";
        } else if (accuracyPercentage >= 80.0) {
            performanceSummary =
                    "Strong performance. Review any incorrect answers to improve further.";
        } else if (accuracyPercentage >= 50.0) {
            performanceSummary =
                    "Moderate performance. Review incorrect answers to strengthen understanding.";
        } else {
            performanceSummary =
                    "More practice is recommended. Review the questions and their explanations.";
        }

        response.setPerformanceSummary(performanceSummary);
        response.setQuestionResults(results);

        return response;
    }

    private boolean requiresManualReview(String type) {
        return type.equals("ESSAY")
                || type.equals("PROBLEM_SOLVING")
                || type.equals("APPLICATION_BASED");
    }


    private void validateRequest(AnswerAnalysisRequest request) {

        if (request == null
                || request.getActivityId() == null
                || request.getActivityId() <= 0
                || request.getAttemptId() == null
                || request.getAttemptId() <= 0
                || request.getStudentId() == null
                || request.getStudentId() <= 0
                || request.getQuestions() == null
                || request.getQuestions().isEmpty()) {

            throw new IllegalArgumentException(
                    "A valid analysis request with questions is required.");
        }

        Set<Integer> questionNumbers = new HashSet<>();

        for (QuestionAnswerRequest question : request.getQuestions()) {

            if (question == null
                    || question.getQuestionNumber() == null
                    || question.getQuestionNumber() <= 0
                    || question.getQuestionType() == null
                    || question.getQuestionType().isBlank()
                    || question.getMaxMarks() == null
                    || !Double.isFinite(question.getMaxMarks())
                    || question.getMaxMarks() < 0) {

                throw new IllegalArgumentException(
                        "Question details or maximum marks are invalid.");
            }

            if (!questionNumbers.add(question.getQuestionNumber())) {
                throw new IllegalArgumentException(
                        "Duplicate question number: "
                                + question.getQuestionNumber());
            }

            String type = normalizeType(question.getQuestionType());

            switch (type) {
                case "MCQ":
                case "TRUE_FALSE":
                    requireText(
                            question.getCorrectAnswer(),
                            "Correct answer is required for " + type
                    );
                    break;

                case "MATCHING":
                    if (question.getCorrectMatchingAnswers() == null
                            || question.getCorrectMatchingAnswers().isEmpty()) {
                        throw new IllegalArgumentException(
                                "Correct matching pairs are required."
                        );
                    }

                    for (Map.Entry<String, String> entry
                            : question.getCorrectMatchingAnswers().entrySet()) {
                        requireText(
                                entry.getKey(),
                                "Matching question keys cannot be blank."
                        );
                        requireText(
                                entry.getValue(),
                                "Matching question values cannot be blank."
                        );
                    }
                    break;

                case "FILL_BLANK":
                case "SHORT_ANSWER":
                    if (question.getAcceptedAnswers() == null
                            || question.getAcceptedAnswers().isEmpty()) {
                        throw new IllegalArgumentException(
                                "Accepted answers are required for " + type
                        );
                    }
                    break;

                case "SORTING":
                    if (question.getCorrectSequence() == null
                            || question.getCorrectSequence().isEmpty()) {
                        throw new IllegalArgumentException(
                                "Correct sequence is required for SORTING."
                        );
                    }
                    break;

                case "DRAG_DROP":
                    if (question.getCorrectPlacements() == null
                            || question.getCorrectPlacements().isEmpty()) {
                        throw new IllegalArgumentException(
                                "Correct placements are required for DRAG_DROP."
                        );
                    }
                    break;

                case "HOTSPOT":
                    requireText(
                            question.getCorrectHotspotId(),
                            "Correct hotspot ID is required."
                    );
                    break;

                case "ESSAY":
                case "PROBLEM_SOLVING":
                case "APPLICATION_BASED":

                    if (question.getRubricMarks() != null) {
                        double rubricMarks = question.getRubricMarks();

                        if (!Double.isFinite(rubricMarks)
                                || rubricMarks < 0
                                || rubricMarks > question.getMaxMarks()) {
                            throw new IllegalArgumentException(
                                    "Rubric marks must be between zero and maximum marks."
                            );
                        }
                    }

                    break;

                case "POLL":
                    // Poll responses are recorded but not scored by default.
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unsupported question type: "
                                    + question.getQuestionType()
                    );
            }
        }
    }


    private boolean isUnanswered(QuestionAnswerRequest question) {
        String type = normalizeType(question.getQuestionType());

        switch (type) {
            case "MATCHING":
                return question.getStudentMatchingAnswers() == null
                        || question.getStudentMatchingAnswers().isEmpty();

            case "SORTING":
                return question.getStudentSequence() == null
                        || question.getStudentSequence().isEmpty();

            case "DRAG_DROP":
                return question.getStudentPlacements() == null
                        || question.getStudentPlacements().isEmpty();

            case "HOTSPOT":
                return question.getStudentHotspotId() == null
                        || question.getStudentHotspotId().isBlank();

            default:
                return question.getStudentAnswer() == null
                        || question.getStudentAnswer().isBlank();
        }
    }


    private boolean evaluateQuestion(QuestionAnswerRequest question) {
        String type = normalizeType(question.getQuestionType());

        switch (type) {
            case "MCQ":
            case "TRUE_FALSE":
                return normalizeAnswer(question.getStudentAnswer())
                        .equals(normalizeAnswer(question.getCorrectAnswer()));

            case "MATCHING":
                return evaluateMatching(
                        question.getStudentMatchingAnswers(),
                        question.getCorrectMatchingAnswers()
                );

            case "FILL_BLANK":
            case "SHORT_ANSWER":
                return question.getAcceptedAnswers().stream()
                        .filter(answer -> answer != null)
                        .map(this::normalizeAnswer)
                        .anyMatch(answer -> answer.equals(
                                normalizeAnswer(question.getStudentAnswer())
                        ));

            case "SORTING":
                return normalizeSequence(question.getStudentSequence())
                        .equals(normalizeSequence(question.getCorrectSequence()));

            case "DRAG_DROP":
                return evaluatePlacements(
                        question.getStudentPlacements(),
                        question.getCorrectPlacements()
                );

            case "HOTSPOT":
                return normalizeAnswer(question.getStudentHotspotId())
                        .equals(normalizeAnswer(question.getCorrectHotspotId()));

            case "ESSAY":
            case "PROBLEM_SOLVING":
            case "APPLICATION_BASED":
                throw new IllegalArgumentException(
                        type + " requires rubric-based evaluation "
                                + "or manual review; it cannot be graded "
                                + "by exact-answer comparison."
                );

            default:
                throw new IllegalArgumentException(
                        "Unsupported question type: " + type
                );
        }
    }

    private boolean evaluateMatching(
            Map<String, String> studentAnswers,
            Map<String, String> correctAnswers) {

        if (studentAnswers == null
                || studentAnswers.size() != correctAnswers.size()) {
            return false;
        }

        for (Map.Entry<String, String> entry : correctAnswers.entrySet()) {

            String studentValue = findValueIgnoreCase(
                    studentAnswers, entry.getKey());

            if (studentValue == null
                    || !normalizeAnswer(studentValue)
                    .equals(normalizeAnswer(entry.getValue()))) {
                return false;
            }
        }

        return true;
    }

    private String findValueIgnoreCase(
            Map<String, String> answers, String key) {

        for (Map.Entry<String, String> entry : answers.entrySet()) {
            if (entry.getKey() != null
                    && entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }

        return null;
    }

    private String normalizeType(String type) {
        return type.trim()
                .replace('-', '_')
                .replace(' ', '_')
                .toUpperCase(Locale.ROOT);
    }

    private String normalizeAnswer(String answer) {
        return answer == null
                ? ""
                : answer.trim()
                .replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
    }

    private List<String> normalizeSequence(List<String> values) {
        if (values == null) {
            return List.of();
        }

        return values.stream()
                .map(this::normalizeAnswer)
                .toList();
    }

    private boolean evaluatePlacements(
            Map<String, String> student,
            Map<String, String> correct) {

        if (student == null || correct == null
                || student.size() != correct.size()) {
            return false;
        }

        for (Map.Entry<String, String> entry : correct.entrySet()) {
            String studentZone = findValueIgnoreCase(
                    student, entry.getKey()
            );

            if (studentZone == null
                    || !normalizeAnswer(studentZone)
                    .equals(normalizeAnswer(entry.getValue()))) {
                return false;
            }
        }

        return true;
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
