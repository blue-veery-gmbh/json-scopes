package com.blueveery.core.model;

import com.blueveery.scopes.jackson.ShortTypeNameIdResolverJackson;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Created by tomek on 08.09.16.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.EXISTING_PROPERTY,property = "id", visible = true)
@JsonTypeIdResolver(ShortTypeNameIdResolverJackson.class)

@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@MappedSuperclass
public class BaseEntity {


    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    @Id
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setJsonId(String jsonId) {
        String[] idComponents = jsonId.toString().split("/");
        id = UUID.fromString(idComponents[1]);
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof BaseEntity){
            BaseEntity otherBaseEntity = (BaseEntity) object;
            return id.equals(otherBaseEntity.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
