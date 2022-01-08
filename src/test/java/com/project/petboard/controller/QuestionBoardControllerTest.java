package com.project.petboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.petboard.appilcation.QuestionBoardController;
import com.project.petboard.domain.member.Member;
import com.project.petboard.domain.questionboard.QuestionBoard;
import com.project.petboard.domain.questionboard.QuestionBoardRepository;
import com.project.petboard.domain.questionboard.QuestionBoardService;
import com.project.petboard.domain.questionboard.QuestionResponseDto;
import com.project.petboard.infrastructure.configure.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = QuestionBoardController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class QuestionBoardControllerTest{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionBoardRepository questionBoardRepository;

    @MockBean
    private QuestionBoardService questionBoardService;

    private final Long questionBoardNumber = 1L;

    @WithMockUser(roles = "ADMIN")
    @DisplayName("질문 게시글 가져오기 테스트")
    @Test
    public void fetchQuestionBoardTestShouldBeSuccess() throws Exception {
        String title = "title";
        String content = "contents";
        QuestionBoard questionBoard = QuestionBoard.builder()
                .questionBoardContents(content)
                .questionBoardTitle(title)
                .member(new Member())
                .build();

        doReturn(new QuestionResponseDto(questionBoard)).when(questionBoardService).fetchQuestionBoard(questionBoardNumber);

        mockMvc.perform(get("/question")
                        .param("questionNumber", String.valueOf(questionBoardNumber)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionBoardTitle").value(title))
                .andExpect(jsonPath("$.questionBoardContents").value(content));
    }

    @WithMockUser(roles = "ADMIN")
    @DisplayName("질문 게시글 삭제 테스트")
    @Test
    public void deleteQuestionBoardTestShouldBeSuccess() throws Exception {

        mockMvc.perform(delete("/question")
                        .param("questionNumber", String.valueOf(questionBoardNumber)))
                .andExpect(status().isOk());

        assertThat(questionBoardRepository.findByQuestionBoardNumber(questionBoardNumber)).isNull();
    }

    @WithMockUser(roles = "ADMIN")
    @DisplayName("질문 답변 테스트")
    @Test
    public void QuestionBoardAnswerTestShouldBeSuccess() throws Exception {
        Map<String, Object> questionAnswerRequestDto = new HashMap<>();

        questionAnswerRequestDto.put("questionBoardNumber", 1);
        questionAnswerRequestDto.put("questionBoardAnswer", "answer11111111");

        mockMvc.perform(patch("/question/answer")
                        .content(objectMapper.writeValueAsString(questionAnswerRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @DisplayName("질문 답변 실패 테스트")
    @Test
    public void QuestionBoardAnswerTestShouldBeFail() throws Exception {
        Map<String, Object> questionAnswerRequestDto = new HashMap<>();

        questionAnswerRequestDto.put("questionBoardNumber", 1);
        questionAnswerRequestDto.put("questionBoardAnswer", "");

        mockMvc.perform(patch("/question/answer")
                        .content(objectMapper.writeValueAsString(questionAnswerRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(roles = "MEMBER")
    @DisplayName("질문게시글 생성 테스트")
    @Test
    public void createQuestionBoardTestShouldBeSuccess() throws Exception {
        Map<String, Object> questionBoardRequestDto = new HashMap<>();

        questionBoardRequestDto.put("memberResponseDto", 1);
        questionBoardRequestDto.put("questionBoardTitle", "title111111111111111");
        questionBoardRequestDto.put("questionBoardContents", "contents11111111111111");

        mockMvc.perform(post("/question")
                        .content(objectMapper.writeValueAsString(questionBoardRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @WithMockUser(roles = "MEMBER")
    @DisplayName("질문게시글 생성 실패 테스트")
    @Test
    public void createQuestionBoardTestShouldBeFail() throws Exception {
        Map<String, Object> questionBoardRequestDto = new HashMap<>();

        questionBoardRequestDto.put("questionBoardNumber", 1);
        questionBoardRequestDto.put("memberResponseDto", 1);
        questionBoardRequestDto.put("questionBoardTitle", "");
        questionBoardRequestDto.put("questionBoardContents", "contents");

        mockMvc.perform(post("/question")
                        .content(objectMapper.writeValueAsString(questionBoardRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("질문글 페이지 가져오기 테스트")
    @Test
    public void getQuestionPageTestShouldBeSuccess() throws Exception {
        mockMvc.perform(get("/question/page")
                        .param("page",String.valueOf(0)))
                .andExpect(status().isOk());
    }
}
