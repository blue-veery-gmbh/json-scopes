package com.blueveery.bluehr.services.impl;

import com.blueveery.bluehr.model.SystemConfig;
import com.blueveery.bluehr.services.SystemConfigService;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class SystemConfigServiceImpl extends BluehrBaseServiceImpl<SystemConfig> implements SystemConfigService {

    private Map<String, SystemConfig> tempSystemConfigs = new HashMap<>();

    public SystemConfigServiceImpl() {
        tempSystemConfigs.put("cv.doc.dir", new SystemConfig("cv.doc.dir", "/home/tomek/bluehr"));
        tempSystemConfigs.put("mail.smtp.host", new SystemConfig("mail.smtp.host", "poczta.interia.pl"));
        tempSystemConfigs.put("bluehr.mail", new SystemConfig("bluehr.mail", "bluehr@interia.pl"));
        tempSystemConfigs.put("bluehr.url", new SystemConfig("bluehr.url", "https://localhost:8443"));
    }

    public SystemConfig getSystemConfig(String name){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SystemConfig> criteriaQuery = criteriaBuilder.createQuery(SystemConfig.class);
        Root<SystemConfig> enrollmentRoot = criteriaQuery.from(SystemConfig.class);
        Predicate namePredicate = criteriaBuilder.equal(enrollmentRoot.get("name"), name);
        CriteriaQuery<SystemConfig> selectAllQuery = criteriaQuery.select(enrollmentRoot).where(namePredicate);

        SystemConfig systemConfig = entityManager.createQuery(selectAllQuery).getResultList().stream().findFirst().orElse(null);
        if(systemConfig==null){//TODO remove it
            systemConfig = tempSystemConfigs.get(name);
        }

        return systemConfig;

    }
}
