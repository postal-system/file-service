package io.codero.fileservice.controller;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(properties = "spring.autoconfigure.exclude=io.codero.interceptor.context.WebContextAutoConfiguration")
class FileControllerTest extends AbstractControllerTest {

    private static final String URL = "/api/files";

    @Autowired
    private MockMvc mvc;

    private final MockMultipartFile file1 = new MockMultipartFile(
            "file",
            "file1.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
    );

    @Test
    void shouldReturnStatusOk() throws Exception {
        mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUUID() throws Exception {
        MvcResult result = mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        String id = result.getResponse().getContentAsString().replaceAll("^\"|\"$", "");
        String idMatcher = UUID.randomUUID().toString();

        Assertions.assertEquals(id.length(), idMatcher.length());
    }

    @Test
    void shouldReturnFileById() throws Exception {
        MvcResult postResult = mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        String strUUID = postResult.getResponse()
                .getContentAsString()
                .replaceAll("^\"|\"$", "");

        UUID id = UUID.fromString(strUUID);

        MvcResult getResult = mvc.perform(get(URL + "/" + id))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().getContentAsString())
                .andReturn();

        String contentActual = getResult.getResponse().getContentAsString();
        String contentExpected = "Hello, World!";

        Assertions.assertEquals(contentExpected, contentActual);
    }

    @Test
    void shouldReturnExceptionIfFileNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        mvc.perform(get(URL + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteFileIfFileExist() throws Exception {
        MvcResult postResult = mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        String strUUID = postResult.getResponse()
                .getContentAsString()
                .replaceAll("^\"|\"$", "");

        UUID id = UUID.fromString(strUUID);

        mvc.perform(delete(URL + "/" + id))
                .andExpect(status().isOk());

        mvc.perform(get(URL + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnExceptionFileIfFileNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete(URL + "/" + id))
                .andExpect(status().isNotFound());
    }
}