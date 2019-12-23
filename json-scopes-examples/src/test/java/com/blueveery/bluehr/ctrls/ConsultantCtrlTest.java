package com.blueveery.bluehr.ctrls;

import com.blueveery.bluehr.model.Consultant;
import com.blueveery.bluehr.model.Enrollment;
import com.blueveery.bluehr.services.ConsultantService;
import com.blueveery.bluehr.services.EnrollmentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import java.util.*;

import static org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by tomek on 29.09.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration(locations = {"classpath*:bluehr-ctrls-spring-context.xml"})
public class ConsultantCtrlTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;
    @Autowired
    ServletContext servletContext;

    @Autowired
    ConsultantService consultantService;

    @Autowired
    EnrollmentService enrollmentService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @BeforeClass
    public static void initJNDI() throws Exception{

        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.memory.MemoryContextFactory");
        System.setProperty("org.osjava.sj.jndi.shared", "true");
        InitialContext ic = new InitialContext();
        ic.createSubcontext("java:");
        ic.createSubcontext("java:/comp");
        ic.createSubcontext("java:/comp/env");
        ic.createSubcontext("java:/comp/env/jdbc");

        //;MODE=PostgreSQL
        JdbcConnectionPool ds = JdbcConnectionPool.create("jdbc:h2:mem:bluehrdb", "sa", "sa");
        ic.bind("java:/bluehrDS", ds);

        Map properties = new HashMap();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("bluehr-pu", properties);
        ic.bind("java:jboss/jpa/BluehrEMF", entityManagerFactory);

    }

    @Test
    public void testEnrollment() throws Exception {
        UUID id = enrollmentService.enroll("tomaszwozniak@interia.pl");
        Enrollment enrollment = enrollmentService.find(id);
        Consultant consultant = consultantService.find(enrollment.getConsultant().getId());
        consultant.setMobilePhone("111 222 333");
        consultant.getPerson().setFirstName("Tomasz");
        consultant.getPerson().setSecondName("Wozniak");
        consultantService.merge(consultant);
        consultant = consultantService.find(consultant.getId());




        id = enrollmentService.enroll("tomasz@interia.pl");
        Enrollment secondEnrollment = enrollmentService.find(id);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/enrollment/")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        JsonNode enrollmentNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        String consultantURL = "/api/" + enrollmentNode.get(0).get("consultant").get("id").textValue();
        mvcResult = mockMvc.perform(get(consultantURL)).andExpect(status().isOk()).andReturn();
        String responseAsString = mvcResult.getResponse().getContentAsString();
        JsonNode consultantNode = objectMapper.readTree(responseAsString);

        MockHttpServletRequestBuilder put = put(consultantURL).contentType("application/json").
                content( objectMapper.writeValueAsString(consultantNode));

        mockMvc.perform(put).andExpect(status().isOk());

        UUID secondCvDocumentId = consultantService.find(secondEnrollment.getConsultant().getId()).getCvDocument().getId();
        String cvDocumentURL = "cvdocument/" + secondCvDocumentId;
        Random random = new Random();
        ((ObjectNode)consultantNode).put("mobilePhone", random.nextInt());
        if(consultantNode.get("cvDocument") instanceof ObjectNode) {
            ObjectNode cvDocument = (ObjectNode) consultantNode.get("cvDocument");
            cvDocument.put("fileName", "test.doc"+random.nextInt());
            if(cvDocument.has("ref")){
                JsonNode ref = cvDocument.get("ref");
                cvDocument.remove("ref");
                cvDocument.set("id", ref);
            }
        }

        put = put(consultantURL).contentType("application/json").
                                            content( objectMapper.writeValueAsString(consultantNode));

        mockMvc.perform(put).andExpect(status().isOk());

    }

}
