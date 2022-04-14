package io.codero.fileservice.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.maven.shared.utils.io.FileUtils.mkdir;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class FileControllerTest extends AbstractControllerTest {

    private static final String URL = "/api/files";

    @Autowired
    private MockMvc mvc;

    @Value("${app.document-root}")
    private String documentRoot;

    private final List<Path> filesToBeDeleted = new ArrayList<>();

    @AfterEach
    public void cleanup() {
        filesToBeDeleted.forEach(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @BeforeEach
    void setUp() {
        if (!Files.exists(Path.of(documentRoot))) {
            mkdir(documentRoot);
        }
    }

    private final MockMultipartFile file1 = new MockMultipartFile(
            "file",
            "file1.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
    );

    private final MockMultipartFile file2 = new MockMultipartFile(
            "file",
            "file2.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Welcome!".getBytes()
    );

    // Crud-------------
    @Test
    void shouldReturnUUIDAndAddFileToDiskAndDataBase() throws Exception {
        Path docRootPath = Path.of(documentRoot, file1.getOriginalFilename());

        Assertions.assertFalse(Files.exists(docRootPath));

        mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertTrue(Files.exists(docRootPath));
        filesToBeDeleted.add(docRootPath);
    }

    @Test
    void shouldReturnExceptionCodeIfFileWithThisNameExist() throws Exception {
        Path docRootPath = Path.of(documentRoot, file1.getOriginalFilename());

        Assertions.assertFalse(Files.exists(docRootPath));

        mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        mvc.perform(multipart(URL).file(file1))
                .andExpect(status().is(409))
                .andReturn();

        Assertions.assertTrue(Files.exists(docRootPath));
        filesToBeDeleted.add(docRootPath);
    }

    // cRud-------------
    @Test
    void shouldReturnFileIfItExist() throws Exception {
        Path docRootPath = Path.of(documentRoot, file1.getOriginalFilename());
        Assertions.assertFalse(Files.exists(docRootPath));

        MvcResult postResult = mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        String strPostResult = postResult.getResponse().getContentAsString();
        String strUUID = strPostResult.substring(1, strPostResult.length() - 1);
        UUID id = UUID.fromString(strUUID);

        MvcResult getResult = mvc.perform(get(URL + "/" + id))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().getContentAsString())
                .andReturn();

        String contentActual = getResult.getResponse().getContentAsString();
        String contentExpected = "Hello, World!";

        Assertions.assertEquals(contentExpected, contentActual);
        Assertions.assertTrue(Files.exists(docRootPath));
        filesToBeDeleted.add(docRootPath);
    }

    @Test
    void shouldReturnExceptionIfFileNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        mvc.perform(get(URL + id))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    //crUd--------------
    @Test
    void ShouldUpdateFileIfUpdatedFileExist() throws Exception {
        Path docRootPath = Path.of(documentRoot, file1.getOriginalFilename());
        Path docRootPath2 = Path.of(documentRoot, file2.getOriginalFilename());
        Assertions.assertFalse(Files.exists(docRootPath));

        MvcResult postResult = mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        String strPostResult = postResult.getResponse().getContentAsString();
        String strUUID = strPostResult.substring(1, strPostResult.length() - 1);
        UUID id = UUID.fromString(strUUID);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(URL + "/" + id);

        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mvc.perform(builder.file(file2))
                .andExpect(status().isOk());

        Assertions.assertFalse(Files.exists(docRootPath));
        Assertions.assertTrue(Files.exists(docRootPath2));
        filesToBeDeleted.add(docRootPath);
        filesToBeDeleted.add(docRootPath2);
    }

    @Test
    void shouldReturnExceptionIfUpdatedFileNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(URL + "/" + id);

        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mvc.perform(builder.file(file2))
                .andExpect(status().isNotFound());
    }

    //cruD--------------------------------------

    @Test
    void shouldDeleteFileIfFileExist() throws Exception {
        Path docRootPath = Path.of(documentRoot, file1.getOriginalFilename());
        Assertions.assertFalse(Files.exists(docRootPath));

        MvcResult postResult = mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        String strPostResult = postResult.getResponse().getContentAsString();
        String strUUID = strPostResult.substring(1, strPostResult.length() - 1);
        UUID id = UUID.fromString(strUUID);

        Assertions.assertTrue(Files.exists(docRootPath));
        mvc.perform(delete(URL + "/" + id))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertFalse(Files.exists(docRootPath));
    }

    @Test
    void shouldReturnExceptionFileIfFileNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete(URL + "/" + id))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void IntegrationTestFileControllerGoodChance() throws Exception {
        Path docRootPath = Path.of(documentRoot, file1.getOriginalFilename());
        Path docRootPath2 = Path.of(documentRoot, file2.getOriginalFilename());

        MvcResult postResult = mvc.perform(multipart(URL).file(file1))
                .andExpect(status().isOk())
                .andReturn();

        String strPostResult = postResult.getResponse().getContentAsString();
        String strUUID = strPostResult.substring(1, strPostResult.length() - 1);
        UUID id = UUID.fromString(strUUID);

        MvcResult getResult = mvc.perform(get(URL + "/" + id))
                .andExpect(status().isOk())
                .andReturn();

        String strGetResult = getResult.getResponse().getContentAsString();

        Assertions.assertEquals("Hello, World!", strGetResult);
        Assertions.assertTrue(Files.exists(docRootPath));

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(URL + "/" + id);

        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mvc.perform(builder.file(file2))
                .andExpect(status().isOk());

        Assertions.assertFalse(Files.exists(docRootPath));
        Assertions.assertTrue(Files.exists(docRootPath2));

        mvc.perform(delete(URL + "/" + id))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertFalse(Files.exists(docRootPath2));
    }
}