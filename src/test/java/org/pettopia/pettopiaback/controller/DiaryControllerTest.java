//package org.pettopia.pettopiaback.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.pettopia.pettopiaback.dto.DiaryDTO;
//import org.pettopia.pettopiaback.exception.NotFoundException;
//import org.pettopia.pettopiaback.service.DiaryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.util.Collections;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(DiaryController.class)
//@WithMockUser
//public class DiaryControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private DiaryService diaryService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("다이어리 작성 테스트")
//    public void testMakeDiary() throws Exception {
//        DiaryDTO.DiaryRequest request = new DiaryDTO.DiaryRequest(
//                2, 1, Collections.emptyList(), 1, null, "Defecation text", "Etc", LocalDate.now()
//        );
//
//        mockMvc.perform(post("/api/v1/life/diary/{petPk}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    @DisplayName("다이어리 조회 테스트")
//    public void testGetDiary() throws Exception {
//        DiaryDTO.DiaryResponse response = new DiaryDTO.DiaryResponse(
//                2, 1, 1, null, "Defecation text", "Etc", null, "2024-06-04"
//        );
//
//        Mockito.when(diaryService.getDiary(anyLong())).thenReturn(response);
//
//        mockMvc.perform(get("/api/v1/life/diary/{diaryPk}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.mealCont", is(2)))
//                .andExpect(jsonPath("$.snackCnt", is(1)))
//                .andExpect(jsonPath("$.walkCnt", is(1)))
//                .andExpect(jsonPath("$.defecationText", is("Defecation text")))
//                .andExpect(jsonPath("$.etc", is("Etc")));
//    }
//
//    @Test
//    @DisplayName("날짜로 다이어리 조회 테스트")
//    public void testGetDiaryByDate() throws Exception {
//        DiaryDTO.DiaryDateResponse response = new DiaryDTO.DiaryDateResponse(
//                1L, 2, 1, 1, null, "Defecation text", "Etc", null, "2024-06-04"
//        );
//
//        Mockito.when(diaryService.getDiaryByDate(eq(1L), any(LocalDate.class))).thenReturn(response);
//
//        mockMvc.perform(get("/api/v1/life/diary/date/{petPk}", 1L)
//                        .param("date", "2024-06-04"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.diaryPk", is(1)))
//                .andExpect(jsonPath("$.mealCont", is(2)))
//                .andExpect(jsonPath("$.snackCnt", is(1)))
//                .andExpect(jsonPath("$.walkCnt", is(1)))
//                .andExpect(jsonPath("$.defecationText", is("Defecation text")))
//                .andExpect(jsonPath("$.etc", is("Etc")));
//    }
//
//    @Test
//    @DisplayName("AI 펫 배변상태 조회 테스트")
//    public void testGetAIDefecation() throws Exception {
//        Mockito.when(diaryService.getDefecation(anyLong())).thenReturn("Healthy");
//
//        mockMvc.perform(get("/api/v1/life/diary/defecation/{petPk}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.defecation", is("Healthy")));
//    }
//
//    @Test
//    @DisplayName("다이어리 삭제 테스트")
//    public void testDeleteDiary() throws Exception {
//        mockMvc.perform(delete("/api/v1/life/diary/{diaryPk}", 1L))
//                .andExpect(status().isNoContent());
//
//        Mockito.doThrow(new NotFoundException("Diary not found")).when(diaryService).delete(anyLong());
//
//        mockMvc.perform(delete("/api/v1/life/diary/{diaryPk}", 2L))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("다이어리 수정 테스트")
//    public void testUpdateDiary() throws Exception {
//        DiaryDTO.DiaryUpdateRequest updateRequest = new DiaryDTO.DiaryUpdateRequest(
//                2, 1, Collections.emptyList(), 1, null, "Updated defecation text", "Updated etc"
//        );
//
//        DiaryDTO.DiaryResponse response = new DiaryDTO.DiaryResponse(
//                2, 1, 1, null, "Updated defecation text", "Updated etc", null, "2024-06-04"
//        );
//
//        Mockito.when(diaryService.updateDiary(eq(1L), any(DiaryDTO.DiaryUpdateRequest.class))).thenReturn(response);
//
//        mockMvc.perform(patch("/api/v1/life/diary/{diaryPk}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.mealCont", is(2)))
//                .andExpect(jsonPath("$.snackCnt", is(1)))
//                .andExpect(jsonPath("$.walkCnt", is(1)))
//                .andExpect(jsonPath("$.defecationText", is("Updated defecation text")))
//                .andExpect(jsonPath("$.etc", is("Updated etc")));
//
//        Mockito.doThrow(new NotFoundException("Diary not found")).when(diaryService).updateDiary(eq(2L), any(DiaryDTO.DiaryUpdateRequest.class));
//
//        mockMvc.perform(patch("/api/v1/life/diary/{diaryPk}", 2L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isNotFound());
//    }
//}
