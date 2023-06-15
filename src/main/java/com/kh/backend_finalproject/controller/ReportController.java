package com.kh.backend_finalproject.controller;

import com.kh.backend_finalproject.service.PostService;
import com.kh.backend_finalproject.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReportController {
    @Autowired
    ReportService reportService;

    // ✅게시글 신고하기
    @DeleteMapping("/post/{postId}/report")
    public ResponseEntity<?> reportPost(@PathVariable Long postId) {
        reportService.reportPost(postId);
        return new ResponseEntity<>("게시글 신고 완료 ❤️", HttpStatus.ACCEPTED);
    }

    // 사용자 차단하기

    // 사용자 신고하기
}