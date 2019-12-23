package com.blueveery.bluehr.services;

import com.blueveery.bluehr.model.CvDocument;
import com.blueveery.core.services.BaseService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by tomek on 12.09.16.
 */
public interface CVDocumentService extends BaseService<CvDocument> {
    void update(UUID id, String fileName, InputStream inputStream);
    void readCVDocument(UUID id, OutputStream outputStream) throws IOException;
}
