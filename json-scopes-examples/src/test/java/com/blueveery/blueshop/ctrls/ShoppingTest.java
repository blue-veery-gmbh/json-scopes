package com.blueveery.blueshop.ctrls;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by tomek on 29.09.16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration(locations = {"classpath*:bluehr-ctrls-spring-context.xml"})
public class ShoppingTest {

    @Autowired
    private WebApplicationContext wac;

    ObjectMapper objectMapper;

    MockMvc mockMvc;
    @Autowired
    ServletContext servletContext;
    private ObjectWriter objectWriter;

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

        JdbcConnectionPool ds = JdbcConnectionPool.create("jdbc:h2:mem:bluehrdb", "sa", "sa");
        ic.bind("java:/bluehrDS", ds);

        Map properties = new HashMap();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("blue-shop-pu", properties);
        ic.bind("java:jboss/jpa/BluehrEMF", entityManagerFactory);



    }

    /**
     * jackson mapper is used to simulate java script code to serialize object to string
     * which is send in mock mvc request
     */
    @Before
    public void initObjectMapper(){
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    }

    /**
     * method creates few products used in later examples
     * @throws Exception
     */
    @Test
    public void a_createProducts() throws Exception {
        String[] itemNames = { "red car", "blue chair", "green apple"};
        for (String itemName : itemNames) {
            ObjectNode nextProductItem = new ObjectNode(JsonNodeFactory.instance);
            nextProductItem.put("id", String.format("product-item/%s", UUID.randomUUID()));
            nextProductItem.put("version", 1);
            nextProductItem.put("name", itemName);

            String jsonValue = objectWriter.writeValueAsString(nextProductItem);
            MockHttpServletRequestBuilder postRequest = post("/api/product-item/")
                    .contentType("application/json").content(jsonValue);
            mockMvc.perform(postRequest).andExpect(status().is2xxSuccessful());
            System.out.println(jsonValue);
        }
    }

    /**
     * method creates person object used in later examples
     * @throws Exception
     */
    @Test
    public void b_createPerson() throws Exception {
        ObjectNode johnSmith = new ObjectNode(JsonNodeFactory.instance);
        johnSmith.put("id", String.format("person/%s", UUID.randomUUID()));
        johnSmith.put("version", 1);
        johnSmith.put("firstName", "John");
        johnSmith.put("secondName", "Smith");

        ObjectNode locationReference = getLocationReference();
        ObjectNode personReference = new ObjectNode(JsonNodeFactory.instance);
        personReference.set("id",johnSmith.get("id"));
        locationReference.set("person", personReference);
        johnSmith.set("location",locationReference);

        String jsonValue = objectWriter.writeValueAsString(johnSmith);
        MockHttpServletRequestBuilder postRequest = post("/api/person/")
                                                    .contentType("application/json").content(jsonValue);
        mockMvc.perform(postRequest).andExpect(status().is2xxSuccessful());
        System.out.println(jsonValue);
    }


    /**
     * method reads all persons and for first on the list it create new role customer
     * customer points to persons by johnSmithReference to not to send person in customer create request.
     * In JavaScript developer is just pointing to person object and scoped is imposed by JsonScopedSerializer
     * @throws Exception
     */
    @Test
    public void c_createCustomer() throws Exception {
        MvcResult getAllPersonsResponse = mockMvc.perform(get("/api/person/")).andExpect(status().isOk()).andReturn();
        String responseAsString = getAllPersonsResponse.getResponse().getContentAsString();
        ArrayNode personsArray = (ArrayNode) objectMapper.readTree(responseAsString);


        ObjectNode johnSmith = (ObjectNode) personsArray.get(0);
        ObjectNode johnSmithReference = new ObjectNode(JsonNodeFactory.instance);
        johnSmithReference.set("id", johnSmith.get("id"));


        ObjectNode johnSmithAsCustomer = new ObjectNode(JsonNodeFactory.instance);
        johnSmithAsCustomer.put("id", String.format("customer/%s", UUID.randomUUID()));
        johnSmithAsCustomer.put("version", 1);
        johnSmithAsCustomer.set("person", johnSmithReference);
        johnSmithAsCustomer.put("email", "john.smith@json-scopes.org");
        johnSmithAsCustomer.put("mobilePhone", "777 666 555");

        String jsonValue = objectWriter.writeValueAsString(johnSmithAsCustomer);
        MockHttpServletRequestBuilder postRequest = post("/api/customer/")
                                                    .contentType("application/json").content(jsonValue);
        mockMvc.perform(postRequest).andExpect(status().is2xxSuccessful());
        System.out.println(jsonValue);
    }

    /**
     * Method test of update  OneToOne relation
     * 1. Method get list persons, next update the location
     * 2. In response we receive null reference to person in location object, because we do not touch the object
     * @throws Exception
     */
    @Test
    public void bb_updatePersonLocation() throws Exception {
        ResultActions perform = mockMvc.perform(get("/api/person").contentType("application/json"));
        MvcResult allPersonResult = perform.andExpect(status().isOk()).andReturn();
        String responseAsString = allPersonResult.getResponse().getContentAsString();
        ArrayNode personsArray = (ArrayNode) objectMapper.readTree(responseAsString);

        ObjectNode johnSmith = (ObjectNode) personsArray.get(0);
        System.out.println("GET person object response \n"+objectWriter.writeValueAsString(johnSmith));
        ObjectNode locationReference = getLocationReference();
        locationReference.put("flatNumber",333444);
        johnSmith.set("location",locationReference);

        String urlTemplate = "/api/person/".concat(johnSmith.get("id").asText().split("/")[1]);
        String updatedPerson = objectWriter.writeValueAsString(johnSmith);
        MockHttpServletRequestBuilder postRequest = put(urlTemplate).contentType("application/json").content(updatedPerson);
        MvcResult mvcResult = mockMvc.perform(postRequest).andExpect(status().is2xxSuccessful()).andReturn();
        System.out.println("PUT person object response\n"+objectWriter.writeValueAsString(objectMapper.readTree(mvcResult.getResponse().getContentAsString())));

    }

    /**
     * @return new <code>ObjectNode</code> as location reference
     */
    private ObjectNode getLocationReference(){
        return new ObjectNode(JsonNodeFactory.instance) {{
            put("id", String.format("location/%s", UUID.randomUUID()));
            put("flatNumber", 123456);
            put("streetName", "Times Square");
            put("postalCode", "99-420");
            put("version", 1);
        }};
    }
    /**
     * 1. method reads list of all customers
     * 2. it gets first and reads again it with connected person object
     * 3. it reads all products
     * 4. next it creates order order points customer via reference johnSmithReference to limit serialization scope,
     * normally in JavaScript it is done by JsonScopedSerializer, product item points order by reference to avoid cyclic
     * references and in JS again it is solve by JsonScopedSerializer
     *
     * @throws Exception
     */
    @Test
    public void d__createOrder() throws Exception {
        JsonNode customerJohnSmithFull = getFirstCustomerOnTheList();
        ObjectNode johnSmithReference = new ObjectNode(JsonNodeFactory.instance);
        johnSmithReference.set("id", customerJohnSmithFull.get("id"));

        ArrayNode productItemsArray = getAllProductItems();

        ObjectNode order = new ObjectNode(JsonNodeFactory.instance);
        order.put("id", String.format("order/%s", UUID.randomUUID()));
        order.put("version", 1);
        order.put("status", "waiting");
        order.put("created", "2019-12-28");
        order.set("customer", johnSmithReference);
        order.set("productItemList", productItemsArray);
        ObjectNode orderReference = new ObjectNode(JsonNodeFactory.instance);
        orderReference.set("id", order.get("id"));
        for (JsonNode jsonNode : productItemsArray) {
            ObjectNode productItemNode = (ObjectNode) jsonNode;
            productItemNode.set("order", orderReference);
        }

        String jsonValue = objectWriter.writeValueAsString(order);
        MockHttpServletRequestBuilder postRequest = post("/api/order/")
                                                        .contentType("application/json").content(jsonValue);
        mockMvc.perform(postRequest).andExpect(status().is2xxSuccessful());
        System.out.println(jsonValue);
    }

    private JsonNode getFirstCustomerOnTheList() throws Exception {
        MvcResult getAllCustomersResponse = mockMvc.perform(get("/api/customer/")).andExpect(status().isOk()).andReturn();
        String responseAsString = getAllCustomersResponse.getResponse().getContentAsString();
        ArrayNode customerArray = (ArrayNode) objectMapper.readTree(responseAsString);
        System.out.println("[customers array - there is only reference to person]");
        System.out.println(objectWriter.writeValueAsString(customerArray));
        JsonNode customerJohnSmith = customerArray.get(0);

        MvcResult getJohnSmithResponse = mockMvc.perform(get("/api/"+customerJohnSmith.get("id").asText())).andExpect(status().isOk()).andReturn();
        responseAsString = getJohnSmithResponse.getResponse().getContentAsString();
        JsonNode customerJohnSmithFull =  objectMapper.readTree(responseAsString);
        System.out.println("[customer with person and orders]");
        System.out.println(objectWriter.writeValueAsString(customerJohnSmithFull));
        return customerJohnSmithFull;
    }

    private ArrayNode getAllProductItems() throws Exception {
        String responseAsString;
        MvcResult getAllProductResponse = mockMvc.perform(get("/api/product-item/")).andExpect(status().isOk()).andReturn();
        responseAsString = getAllProductResponse.getResponse().getContentAsString();
        ArrayNode productArray = (ArrayNode) objectMapper.readTree(responseAsString);
        System.out.println("[product items arrays]");
        System.out.println(objectWriter.writeValueAsString(productArray));
        return productArray;
    }


    /**
     * method reads all orders, with narrow scope when order object is returned from server and updates it state
     * @throws Exception
     */
    @Test
    public void e_updateOrderStatus() throws Exception {
        String responseAsString;
        MvcResult getAllOrdersResponse = mockMvc.perform(get("/api/order")).andExpect(status().isOk()).andReturn();
        responseAsString = getAllOrdersResponse.getResponse().getContentAsString();
        ArrayNode ordersArray = (ArrayNode) objectMapper.readTree(responseAsString);
        System.out.println("[orders arrays]");
        System.out.println(objectWriter.writeValueAsString(ordersArray));
        for (JsonNode jsonNode : ordersArray) {
            ObjectNode orderNode = (ObjectNode) jsonNode;
            orderNode.put("status", "processing");
            String orderJson = objectWriter.writeValueAsString(orderNode);
            mockMvc.perform(put("/api/"+orderNode.get("id").asText()).contentType("application/json").content(orderJson)).andExpect(status().isOk());
        }
    }

    /**
     * method reads all orders, and for each order it fetches full object graph
     * @throws Exception
     */
    @Test
    public void f_readOrders() throws Exception {
        String responseAsString;
        MvcResult getAllOrdersResponse = mockMvc.perform(get("/api/order")).andExpect(status().isOk()).andReturn();
        responseAsString = getAllOrdersResponse.getResponse().getContentAsString();
        ArrayNode ordersArray = (ArrayNode) objectMapper.readTree(responseAsString);
        System.out.println("[orders arrays]");
        System.out.println(objectWriter.writeValueAsString(ordersArray));
        for (JsonNode jsonNode : ordersArray) {
            ObjectNode orderNode = (ObjectNode) jsonNode;
            MockHttpServletRequestBuilder getRequest = get("/api/" + orderNode.get("id").asText());
            MvcResult orderResponse = mockMvc.perform(getRequest).andExpect(status().isOk()).andReturn();
            JsonNode fullOrder = objectMapper.readTree(orderResponse.getResponse().getContentAsString());
            System.out.println("[order with connected objects]");
            System.out.println(objectWriter.writeValueAsString(fullOrder));
        }
    }
}
