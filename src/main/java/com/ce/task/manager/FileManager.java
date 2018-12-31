package com.ce.task.manager;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * saveDocument() saves document. updateDocument() update document for given
 * document Id. deleteDocument() deletes document for gives documentId
 * getDocument() download document for given documentId.
 */
public interface FileManager {

    public String saveDocument(MultipartFile file) throws IOException;

    public File getDocument(String documentId) throws FileNotFoundException;

    public String updateDocument(String documentId, MultipartFile file) throws IOException;

    public String deleteDocument(String documentId) throws IOException;

}
