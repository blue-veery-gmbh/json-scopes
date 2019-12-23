package com.blueveery.bluehr.ctrls;

import com.blueveery.bluehr.model.CVDocument;
import com.blueveery.bluehr.services.CVDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by tomek on 09.09.16.
 */
@Component
@RequestMapping("/api/cv-document")
public class CVDocumentCtrl {

    @Autowired
    private CVDocumentService cvDocumentService;


    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = {"application/octet-stream"})
    @ResponseBody
    public void getCVDocument(@PathVariable("id") UUID id, HttpServletResponse response) throws IOException {
        CVDocument cvDocument = cvDocumentService.find(id);
        response.setHeader("Content-Disposition", "attachment;filename=" + cvDocument.getFileName());
        cvDocumentService.readCVDocument(id, response.getOutputStream());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = {"application/octet-stream"})
    @ResponseBody
    public void updateCVDocument(@PathVariable("id") UUID id, @Nullable @RequestParam("fileName") String fileName,
                                 HttpServletRequest request) throws IOException {
       try(InputStream inputStream = request.getInputStream()) {
           cvDocumentService.update(id, fileName, inputStream);
       }
    }

}
