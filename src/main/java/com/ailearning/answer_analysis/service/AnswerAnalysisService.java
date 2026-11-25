package com.ailearning.answer_analysis.service;

import com.ailearning.answer_analysis.dto.request.AnswerAnalysisRequest;
import com.ailearning.answer_analysis.dto.response.AnswerAnalysisResponse;

public interface AnswerAnalysisService {

    AnswerAnalysisResponse analyzeAnswers(
            AnswerAnalysisRequest request);
}