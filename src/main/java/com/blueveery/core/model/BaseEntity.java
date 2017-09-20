package com.blueveery.core.model;

import com.blueveery.scopes.jackson.ShortNameIdResolver;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Created by tomek on 08.09.16.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.EXISTING_PROPERTY,property = "id", visible = true)
@JsonTypeIdResolver(ShortNameIdResolver.class)

@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@MappedSuperclass
public class BaseEntity {


    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    @JsonIgnore
    @Id
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonProperty("id")
    public String getJsonId() {
        return getClass().getSimpleName().toLowerCase() + "/" + id;
    }

    public void setJsonId(String jsonId) {
        String[] idComponents = jsonId.toString().split("/");
        id = UUID.fromString(idComponents[1]);
    }
}
