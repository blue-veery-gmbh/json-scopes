package com.blueveery.bluehr.model;

import javax.persistence.*;

/**
 * Created by tomek on 08.09.16.
 */
@Entity
@DiscriminatorValue(value = "consultant")
public class Consultant extends PersonRole{

    private String email;
    private String mobilePhone;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Enrollment enrolledBy;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private CVDocument cvDocument = new CVDocument();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Enrollment enrollment;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public CVDocument getCvDocument() {
        return cvDocument;
    }

    public void setCvDocument(CVDocument cvDocument) {
        this.cvDocument = cvDocument;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public Enrollment getEnrolledBy() {
        return enrolledBy;
    }

    public void setEnrolledBy(Enrollment enrolledBy) {
        this.enrolledBy = enrolledBy;
    }
}
