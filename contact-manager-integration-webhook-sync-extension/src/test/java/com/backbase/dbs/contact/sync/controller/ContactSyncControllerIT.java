package com.backbase.dbs.contact.sync.controller;

import static com.backbase.buildingblocks.test.http.TestRestTemplateConfiguration.TEST_SERVICE_BEARER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncPostRequestBody;
import com.backbase.dbs.contact.sync.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("it")
class ContactSyncControllerIT {

    private static final String BASE_URL = "/service-api/v1/webhooks/contacts/sync";
    private static final String SYNC_CREATE_REQUEST_FILE = "/contact-sync-create-post-request-body.json";
    private static final String SYNC_UPDATE_REQUEST_FILE = "/contact-sync-update-post-request-body.json";
    private static final String SYNC_DELETE_REQUEST_FILE = "/contact-sync-delete-post-request-body.json";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSyncContactCreation() throws Exception {
        shouldSyncContact(SYNC_CREATE_REQUEST_FILE).andExpect(status().isOk());
    }

    @Test
    void shouldSyncContactUpdate() throws Exception {
        shouldSyncContact(SYNC_UPDATE_REQUEST_FILE).andExpect(status().isOk());
    }

    @Test
    void shouldSyncContactDeletion() throws Exception {
        shouldSyncContact(SYNC_DELETE_REQUEST_FILE).andExpect(status().isOk());
    }

    @Nested
    class RequestValidationTest {

        @Test
        void shouldReturnErrorOnSyncContactWithoutAction() throws Exception {
            ContactSyncPostRequestBody request = objectMapper.readValue(
                readFile(SYNC_CREATE_REQUEST_FILE), ContactSyncPostRequestBody.class);
            request.setAction(null);

            shouldSyncContact(request).andExpect(status().isBadRequest());

        }

        @Test
        void shouldReturnErrorOnSyncContactWithoutId() throws Exception {
            ContactSyncPostRequestBody request = objectMapper.readValue(
                readFile(SYNC_CREATE_REQUEST_FILE), ContactSyncPostRequestBody.class);
            request.getAfter().setId(null);

            shouldSyncContact(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnErrorOnSyncContactWithoutName() throws Exception {
            ContactSyncPostRequestBody request = objectMapper.readValue(
                readFile(SYNC_CREATE_REQUEST_FILE), ContactSyncPostRequestBody.class);
            request.getAfter().setName(null);

            shouldSyncContact(request).andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnErrorOnSyncContactWithoutAccounts() throws Exception {
            ContactSyncPostRequestBody request = objectMapper.readValue(
                readFile(SYNC_CREATE_REQUEST_FILE), ContactSyncPostRequestBody.class);
            request.getAfter().setAccounts(null);

            shouldSyncContact(request).andExpect(status().isBadRequest());
        }
    }

    private ResultActions shouldSyncContact(String requestFileName) throws Exception {
        return shouldSyncContact(readFile(requestFileName));
    }

    private ResultActions shouldSyncContact(ContactSyncPostRequestBody request) throws Exception {
        return shouldSyncContact(objectMapper.writeValueAsBytes(request));
    }

    private ResultActions shouldSyncContact(byte[] bytes) throws Exception {
        return mockMvc.perform(post(BASE_URL)
                .header("Authorization", TEST_SERVICE_BEARER)
                .contentType(APPLICATION_JSON)
                .characterEncoding(UTF_8.name())
                .content(bytes))
            .andDo(print());
    }

    private byte[] readFile(String fileName) throws IOException {
        try (InputStream inputStream = this.getClass().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException("File not found:" + fileName);
            }
            return inputStream.readAllBytes();
        }
    }
}
