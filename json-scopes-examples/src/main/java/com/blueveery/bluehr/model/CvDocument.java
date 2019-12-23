package com.blueveery.bluehr.model;

import com.blueveery.core.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by tomek on 08.09.16.
 */

@Entity
@Table(name = "cv_document")
public class CvDocument extends BaseEntity {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
