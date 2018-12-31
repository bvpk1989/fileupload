package com.ce.task.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileUtils {

    public static File getDocument(String filePath, String documentId) {
        File file = null;
        File dir = new File(filePath);
        String name = "";
        FilenameFilter filter = (dir1, name1) -> {
            return name1.startsWith(documentId);
        };
        String files[] = dir.list(filter);
        if (files.length == 0)
            ;
        else {
            name = files[0];
            file = new File(filePath + "/" + name);
        }
        return file;
    }

    public static void saveDocument(String filePath, String documentId, MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String[] fileMetaData = fileName.split("\\.");
        if (data.length > 0) {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(
                    filePath + "/" + documentId + "_" + contentType.replace("/", "_") + "." + fileMetaData[1])));
            bos.write(data);
            bos.flush();
            bos.close();
        }

    }

}
