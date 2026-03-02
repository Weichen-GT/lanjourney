package com.lanai.lanjourney.controllers;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanai.lanjourney.dto.AddUserVocabRequest;
import com.lanai.lanjourney.dto.UpdateUserVocabStatusRequest;
import com.lanai.lanjourney.entity.UserVocabulary;
import com.lanai.lanjourney.entity.UserVocabularyId;
import com.lanai.lanjourney.service.UserVocabularyService;

@WebMvcTest(UserVocabularyController.class)
class UserVocabularyControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserVocabularyService service;

    // Helper: build an entity returned by the service
    private static UserVocabulary uv(long userId, long vocabId) {
        UserVocabulary uv = new UserVocabulary();

        UserVocabularyId id = new UserVocabularyId();
        id.setUserId(userId);
        id.setVocabId(vocabId);

        uv.setId(id);
        uv.setStatus("LEARNING");     // adjust if your type is enum
        uv.setTimesSeen(3);
        uv.setReviewCount(1);

        return uv;
    }

    @Test
    void add_shouldReturn201_andResponseBody() throws Exception {
        long userId = 10L;

        AddUserVocabRequest req = new AddUserVocabRequest();
        // set whatever fields your DTO requires
        // e.g. req.vocabId = 99L; req.status = "LEARNING"; etc.

        when(service.addUserVocabulary(eq(userId), any(AddUserVocabRequest.class)))
                .thenReturn(uv(userId, 99L));

        mockMvc.perform(
                post("/api/users/{userId}/vocabularies", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.vocabId").value(99))
                .andExpect(jsonPath("$.status").value("LEARNING"))
                .andExpect(jsonPath("$.timesSeen").value(3))
                .andExpect(jsonPath("$.reviewCount").value(1))
                .andExpect(jsonPath("$.firstAddedAt").exists());

        verify(service, times(1)).addUserVocabulary(eq(userId), any(AddUserVocabRequest.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void add_shouldReturn400_whenBodyValidationFails() throws Exception {
        long userId = 10L;

        // Intentionally invalid. This only works if AddUserVocabRequest has validation annotations
        // like @NotNull, @NotBlank, @Min, etc.
        AddUserVocabRequest invalidReq = new AddUserVocabRequest();

        mockMvc.perform(
                post("/api/users/{userId}/vocabularies", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq))
        )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void updateStatus_shouldReturn200_andResponseBody() throws Exception {
        long userId = 10L;
        long vocabId = 99L;

        UpdateUserVocabStatusRequest req = new UpdateUserVocabStatusRequest();
        // set required fields, e.g. req.status = "MASTERED";

        UserVocabulary updated = uv(userId, vocabId);
        updated.setStatus("MASTERED");
        updated.setTimesSeen(4);
        updated.setReviewCount(2);

        when(service.updateUserVocabularyStatus(
                eq(userId), eq(vocabId), any(UpdateUserVocabStatusRequest.class)
        )).thenReturn(updated);

        mockMvc.perform(
                put("/api/users/{userId}/vocabularies/{vocabId}", userId, vocabId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.vocabId").value(99))
                .andExpect(jsonPath("$.status").value("MASTERED"))
                .andExpect(jsonPath("$.timesSeen").value(4))
                .andExpect(jsonPath("$.reviewCount").value(2))
                .andExpect(jsonPath("$.firstAddedAt").exists());

        verify(service, times(1))
                .updateUserVocabularyStatus(eq(userId), eq(vocabId), any(UpdateUserVocabStatusRequest.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void updateStatus_shouldReturn400_whenBodyValidationFails() throws Exception {
        long userId = 10L;
        long vocabId = 99L;

        UpdateUserVocabStatusRequest invalidReq = new UpdateUserVocabStatusRequest();

        mockMvc.perform(
                put("/api/users/{userId}/vocabularies/{vocabId}", userId, vocabId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq))
        )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void delete_shouldReturn204_andCallService() throws Exception {
        long userId = 10L;
        long vocabId = 99L;

        doNothing().when(service).removeUserVocabulary(userId, vocabId);

        mockMvc.perform(delete("/api/users/{userId}/vocabularies/{vocabId}", userId, vocabId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service, times(1)).removeUserVocabulary(userId, vocabId);
        verifyNoMoreInteractions(service);
    }

    @Test
    void add_shouldReturn400_whenJsonIsMalformed() throws Exception {
        long userId = 10L;

        mockMvc.perform(
                post("/api/users/{userId}/vocabularies", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ this is not valid json }")
        )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }
}
