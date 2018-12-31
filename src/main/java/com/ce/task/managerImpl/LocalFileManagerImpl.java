package com.ce.task.managerImpl;

import com.ce.task.manager.FileManager;
import com.ce.task.util.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class LocalFileManagerImpl implements FileManager {

    @Autowired
    private Environment env;

    @Override
    public String saveDocument(MultipartFile file) throws IOException {
        String filePath = env.getProperty("file_base_path");
        String alphaNumeric = RandomStringUtils.randomAlphanumeric(20);
        FileUtils.saveDocument(filePath, alphaNumeric, file);
        return alphaNumeric;
    }

    @Override
    public File getDocument(String documentId) throws FileNotFoundException {
        String filePath = env.getProperty("file_base_path");
        File file = FileUtils.getDocument(filePath, documentId);
        if (file == null) {
            throw new FileNotFoundException("Document not found.");
        }
        return file;
    }

    @Override
    public String updateDocument(String documentId, MultipartFile file) throws IOException {

        String filePath = env.getProperty("file_base_path");
        File existingFile = FileUtils.getDocument(filePath, documentId);
        if (existingFile == null)
            return "NO_FILE_EXISTS";
        else {
            existingFile.delete();
            FileUtils.saveDocument(filePath, documentId, file);
            return "SUCCESS";
        }
    }

    @Override
    public String deleteDocument(String documentId) throws IOException {
        String filePath = env.getProperty("file_base_path");
        File file = FileUtils.getDocument(filePath, documentId);
        if (file == null)
            return "NO_FILE_EXISTS";
        else {
            file.delete();
            return "SUCCESS";
        }

    }

}
