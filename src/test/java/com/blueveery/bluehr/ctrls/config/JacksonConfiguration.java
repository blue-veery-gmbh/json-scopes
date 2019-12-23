package com.blueveery.bluehr.ctrls.config;

import com.blueveery.scopes.jackson.ScopesModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tomek on 12.09.16.
 */
@Configuration
public class JacksonConfiguration {

    @Bean(name="jacksonObjectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        objectMapper.registerModule(new ScopesModule());

        return objectMapper;
    }

//    @Bean
//    public JsonScopeResponseBodyAdvice jsonScopeResponseBodyAdvice(){
//        return new JsonScopeResponseBodyAdvice();
//    }
//
//    @Bean
//    public JsonScopeRequestBodyAdvice jsonScopeRequestBodyAdvice(){
//        return new JsonScopeRequestBodyAdvice();
//    }

}
