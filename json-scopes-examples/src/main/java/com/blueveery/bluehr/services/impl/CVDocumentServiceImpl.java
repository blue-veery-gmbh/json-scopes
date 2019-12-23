package com.blueveery.bluehr.services.impl;

import com.blueveery.bluehr.model.CVDocument;
import com.blueveery.bluehr.model.SystemConfig;
import com.blueveery.bluehr.services.CVDocumentService;
import com.blueveery.bluehr.services.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by tomek on 12.09.16.
 */
@Component
public class CVDocumentServiceImpl extends BluehrBaseServiceImpl<CVDocument> implements CVDocumentService {

    @Autowired
    private SystemConfigService systemConfigService;

    @Transactional
    @Override
    public void update(UUID id, String fileName, InputStream inputStream) {
        try {
            SystemConfig systemConfig = systemConfigService.getSystemConfig("cv.doc.dir");
            Path cvdocDir = Paths.get(systemConfig.getValue());
            if(Files.notExists(cvdocDir)){
                Files.createDirectories(cvdocDir);
            }

            CVDocument cvDocument = find(id);
            cvDocument.setFileName(fileName);
            Path cvdocFile = cvdocDir.resolve(cvDocument.getId().toString());

            Files.copy(inputStream, cvdocFile);
        }catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void readCVDocument(UUID id, OutputStream outputStream) throws IOException {
        SystemConfig systemConfig = systemConfigService.getSystemConfig("cv.doc.dir");
        Path cvdocDir = Paths.get(systemConfig.getValue());

        Path cvdocFile = cvdocDir.resolve(id.toString());
        Files.copy(cvdocFile, outputStream);
        outputStream.close();
    }
}
