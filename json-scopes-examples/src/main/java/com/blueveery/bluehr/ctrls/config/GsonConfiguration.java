package com.blueveery.bluehr.ctrls.config;

import com.blueveery.bluehr.model.Customer;
import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.ProxyInstanceFactory;
import com.blueveery.scopes.ReflectionUtil;
import com.blueveery.scopes.ShortTypeNameIdResolver;
import com.blueveery.scopes.gson.BaseEntityDeserializer;
import com.blueveery.scopes.gson.BaseEntitySerializer;
import com.blueveery.scopes.hibernate.JPASpecificOperationsHibernateImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfiguration {

    @Bean(name="scopedGson")
    public Gson scopedGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.setDateFormat("yyyy-MM-dd");
        ShortTypeNameIdResolver shortTypeNameIdResolver = new ShortTypeNameIdResolver();
        shortTypeNameIdResolver.addPackage(Customer.class.getPackage());


        ReflectionUtil reflectionUtil = new ReflectionUtil();

        JPASpecificOperationsHibernateImpl jpaSpecificOperations = new JPASpecificOperationsHibernateImpl();
        ProxyInstanceFactory proxyInstanceFactory = new ProxyInstanceFactory(jpaSpecificOperations);
        BaseEntityDeserializer baseEntityDeserializer = new BaseEntityDeserializer(reflectionUtil, shortTypeNameIdResolver, proxyInstanceFactory);
        gsonBuilder.registerTypeHierarchyAdapter(BaseEntity.class, baseEntityDeserializer);

        BaseEntitySerializer baseEntitySerializer = new BaseEntitySerializer(reflectionUtil, shortTypeNameIdResolver, jpaSpecificOperations);
        gsonBuilder.registerTypeHierarchyAdapter(BaseEntity.class, baseEntitySerializer);


        Gson gson = gsonBuilder.create();
        return  gson;
    }
}
