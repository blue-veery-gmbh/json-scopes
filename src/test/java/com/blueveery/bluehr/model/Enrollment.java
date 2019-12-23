package com.blueveery.bluehr.model;

import com.blueveery.core.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tomek on 08.09.16.
 */
@Entity
@Table(name = "enrollments")
public class Enrollment extends BaseEntity {
    private String email;
    private Date created;
    private Date emailDate;
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private HRAdmin createdBy;

    @OneToOne(mappedBy = "enrollment")
    private Consultant consultant;

    public Enrollment() {
        this.created = new Date();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public HRAdmin getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(HRAdmin createdBy) {
        this.createdBy = createdBy;
    }

    public Date getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(Date emailDate) {
        this.emailDate = emailDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public Consultant getConsultant() {
        return consultant;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }
}
