package com.ce.task.controller;

import com.ce.task.controller.DocumentStorageController;
import com.ce.task.manager.FileManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class DocumentStorageControllerTest {

    @Mock
    private FileManager fileManager;

    private DocumentStorageController documentStorageController;
    private HttpServletResponse httpServletResponse;

    private String responseEntity;
    private MultipartFile multipartFile;
    private MultipartFile multipartFile2;
    private File file;

    @Before
    public void setUp() throws FileNotFoundException, IOException {
        MockitoAnnotations.initMocks(this);
        documentStorageController = new DocumentStorageController(fileManager);

        responseEntity = "http://localhost:8080/storage/document/kmZJBK8RVvH4RbA8XR9y";
        file = new File("http://localhost:8080/storage/document/kmZJBK8RVvH4RbA8XR9y");

        multipartFile = new MultipartFile() {

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return null;
            }
        };

        multipartFile2 = new MultipartFile() {

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return null;
            }
        };

    }

    @After
    public void tearDown() {
    }

    @Test(expected = Exception.class)
    public void testSaveDocumentSuccess() throws IOException {
        when(fileManager.saveDocument(Matchers.any(MultipartFile.class))).thenReturn(responseEntity);
        documentStorageController.saveDocument(multipartFile);
    }

    @Test
    public void testSaveDocumentToThrowException() throws IOException {
        doThrow(IOException.class).when(fileManager).saveDocument(Matchers.any(MultipartFile.class));
        documentStorageController.saveDocument(multipartFile);
    }

    @Test
    public void testSaveDocumentSuccessWithEmptyFile() throws IOException {
        doThrow(IOException.class).when(fileManager).saveDocument(Matchers.any(MultipartFile.class));
        documentStorageController.saveDocument(multipartFile2);
    }

    @Test
    public void testupdateDocumentSuccess() throws IOException {
        when(fileManager.updateDocument(Matchers.any(String.class), Matchers.any(MultipartFile.class)))
                .thenReturn("NO_FILE_EXISTS");
        ResponseEntity responseEntity = documentStorageController.updateDocument("123", multipartFile);
        assertNotNull(responseEntity);
    }

    @Test
    public void testupdateDocumentSuccess2() throws IOException {
        when(fileManager.updateDocument(Matchers.any(String.class), Matchers.any(MultipartFile.class)))
                .thenReturn("NO_FILE_EXIST");
        ResponseEntity responseEntity = documentStorageController.updateDocument("123", multipartFile);
        assertNotNull(responseEntity);
    }

    @Test
    public void testupdateDocumentSuccessWithEmptyFile() throws IOException {
        when(fileManager.updateDocument(Matchers.any(String.class), Matchers.any(MultipartFile.class)))
                .thenReturn("NO_FILE_EXISTS");
        ResponseEntity responseEntity = documentStorageController.updateDocument("123", multipartFile2);
        assertNotNull(responseEntity);
    }

    @Test
    public void testupdateDocumentToThrowException() throws IOException {
        doThrow(IOException.class).when(fileManager).updateDocument(Matchers.any(String.class),
                Matchers.any(MultipartFile.class));
        documentStorageController.updateDocument("123", multipartFile);
    }

    @Test
    public void testdeleteDocumentSuccess() throws IOException {
        when(fileManager.deleteDocument(Matchers.any(String.class))).thenReturn("NO_FILE_EXISTS");
        ResponseEntity responseEntity = documentStorageController.deleteDocument("123");
        assertNotNull(responseEntity);
    }

    @Test
    public void testdeleteDocumentSuccess2() throws IOException {
        when(fileManager.deleteDocument(Matchers.any(String.class))).thenReturn("NO_FILE_EXIST");
        ResponseEntity responseEntity = documentStorageController.deleteDocument("123");
        assertNotNull(responseEntity);
    }

    @Test
    public void testdeleteDocumentToThrowException() throws IOException {
        doThrow(IOException.class).when(fileManager).deleteDocument(Matchers.any(String.class));
        ResponseEntity responseEntity = documentStorageController.deleteDocument("123");
        assertNotNull(responseEntity);
    }

    @Test
    public void testgetDocumentSuccess() throws IOException {
        when(fileManager.getDocument(Matchers.any(String.class))).thenReturn(file);
        ResponseEntity responseEntity = documentStorageController.getDocument(httpServletResponse, "123");
        assertNotNull(responseEntity);
    }

}
