package com.blueveery.bluehr.model;

import com.blueveery.core.model.BaseEntity;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by tomek on 08.09.16.
 */
@Entity
@Table(name = "system_config")
public class SystemConfig extends BaseEntity {
    private String name;
    @Nullable
    private String value;

    public SystemConfig() {
    }

    public SystemConfig(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
