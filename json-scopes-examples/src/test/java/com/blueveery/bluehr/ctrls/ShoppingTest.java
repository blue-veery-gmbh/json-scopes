package com.blueveery.bluehr.ctrls;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by tomek on 29.09.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration(locations = {"classpath*:bluehr-ctrls-spring-context.xml"})
public class ShoppingTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;
    @Autowired
    ServletContext servletContext;

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
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("blue-shop-pu", properties);
        ic.bind("java:jboss/jpa/BluehrEMF", entityManagerFactory);

    }

    @Test
    public void testEnrollment() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        JsonNode ordersNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        String consultantURL = "/api/" + ordersNode.get(0).get("consultant").get("id").textValue();
        mvcResult = mockMvc.perform(get(consultantURL)).andExpect(status().isOk()).andReturn();
        String responseAsString = mvcResult.getResponse().getContentAsString();
        JsonNode consultantNode = objectMapper.readTree(responseAsString);

        MockHttpServletRequestBuilder put = put(consultantURL).contentType("application/json").
                content( objectMapper.writeValueAsString(consultantNode));

        mockMvc.perform(put).andExpect(status().isOk());

        mockMvc.perform(put).andExpect(status().isOk());

    }

}
