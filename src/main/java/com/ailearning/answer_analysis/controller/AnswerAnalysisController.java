
package com.ailearning.answer_analysis.controller;

import com.ailearning.answer_analysis.dto.request.AnswerAnalysisRequest;
import com.ailearning.answer_analysis.dto.response.AnswerAnalysisResponse;
import com.ailearning.answer_analysis.service.AnswerAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answer-analysis")
public class AnswerAnalysisController {

    private final AnswerAnalysisService answerAnalysisService;

    public AnswerAnalysisController(
            AnswerAnalysisService answerAnalysisService) {
        this.answerAnalysisService = answerAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnswerAnalysisResponse> analyzeAnswers(
            @Valid @RequestBody AnswerAnalysisRequest request) {

        AnswerAnalysisResponse response =
                answerAnalysisService.analyzeAnswers(request);

        return ResponseEntity.ok(response);
    }
}
