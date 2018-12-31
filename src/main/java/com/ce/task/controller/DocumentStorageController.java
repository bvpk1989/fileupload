package com.ce.task.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.ce.task.manager.FileManager;

/**
 * 
 * @author BVPK
 *
 */
@RestController
@RequestMapping(value = "/storage/document")
public class DocumentStorageController {

  // @Autowired
  private FileManager fileManager;

  @Autowired
  public DocumentStorageController(FileManager fileManager) {
    this.fileManager = fileManager;

  }

  /**
   * 
   * Saves document.Returns the unique docId and location to access document. Returns document
   * location
   * 
   * @param file file is input parameter which accepts multi part file
   * @return Returns document unique id with 201 status.
   */
  @RequestMapping(value = "/", method = RequestMethod.POST)
  public ResponseEntity<?> saveDocument(@RequestParam("document") MultipartFile file) {
    try {
      if (file != null && file.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document is mising.");
      }
      String documentId = fileManager.saveDocument(file);
      URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
          .buildAndExpand(documentId).toUri();
      return ResponseEntity.created(location).build();
    } catch (IOException ie) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
    }
  }

  /**
   * Updates document for given docId. Returns 204 if document found Returns 404 if document not
   * found.
   * 
   * @param documentId document id is used to update the document
   * @param file file is newly updated file replace with given document id.
   * @return Returns 204 on success. 404 when file not found.
   * 
   */
  @RequestMapping(value = "/{docId}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateDocument(@PathVariable("docId") String documentId,
      @RequestParam("document") MultipartFile file) {
    try {
      if (file.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document is mising.");
      }
      String response = fileManager.updateDocument(documentId, file);
      if (response.equals("NO_FILE_EXISTS"))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found.");
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Document updated.");
    } catch (IOException ie) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
    }
  }

  /**
   * Deletes the document for given docId.
   * 
   * @param documentId document id used to identify which document to delete.
   * @return Returns 204 if document found Returns 404 if document not found.
   */
  @RequestMapping(value = "/{docId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteDocument(@PathVariable("docId") String documentId) {
    try {
      String response = fileManager.deleteDocument(documentId);
      if (response.equals("NO_FILE_EXISTS"))
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document is not found.");
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Document is deleted.");
    } catch (IOException ie) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
    }
  }

  /**
   * 
   * Returns the document for a given docId. Returns 404 if document not found.
   * 
   * @param response used to send file in the stream.
   * @param documentId used to identify the document
   * @return file as output based on docId
   */
  @RequestMapping(value = "/{docId}", method = RequestMethod.GET)
  public ResponseEntity<?> getDocument(HttpServletResponse response,
      @PathVariable("docId") String documentId) {
    try {
      File file = fileManager.getDocument(documentId);
      if (file != null) {
        String fileMetaData[] = file.getName().split("\\.");
        String fileData[] = fileMetaData[0].split("_");
        InputStream inputStream = new FileInputStream(file);
        response.setContentType(fileData[1] + "/" + fileData[2]);
        response.addHeader("Content-Disposition",
            "attachment; filename=" + fileData[0] + "." + fileMetaData[1]);
        try {
          IOUtils.copy(inputStream, response.getOutputStream());
          response.getOutputStream().flush();
          inputStream.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
      return ResponseEntity.status(HttpStatus.OK).body("");
    } catch (FileNotFoundException fnf) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found.");
    }
  }

}
