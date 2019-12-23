package com.blueveery.bluehr.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by tomek on 08.09.16.
 */
@Entity
@DiscriminatorValue(value = "hr-admin")
public class HRAdmin extends PersonRole {

}
