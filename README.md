# JSON Scopes
JSON Scopes allows to serialize part of JPA entity graph to JSON and next such JSON representation could be deserialized into
detached JPA entity graph which can be merged into EntityManager which allows to skip creation of Data Transfer Objects/Value Objects.
One Spring REST endpoint could serialize as much data as it is described in `JsonScope` (additional spring annotation) and it can accept 
less or up to scope defined in `JsonScope`   
# JSON Scopes motivation
In Java at the server side, when JPA is used there is a frequent need to transfer data described by JPA entities to clients, 
currently the most popular format is JSON. JPA entities are hard to serialize due to few reasons:
  + bi-directional references are common in JPA which causes endless loops during serialization
  + reach model described by entities has many association which could cause that client will get to much data and in
   the extreme case whole database
  + if we limit somehow object network during serialization from server to client by means provided by JSON serialization 
  libraries which is skipping not needed fields serialization to cut object network, after manipulation at client side 
  we get these fields as null values, which causes hard question...we get these null values because we didn't serialize 
  part of entity network or client set this fields to null
    
Two handle mentioned problems there is created second data model which has few names
  + Data Transfer Objects DTO 
  + Value Objects VO
  + Request Response model 

for rest of this guide DTO abbreviation will be used 

These models reflects JPA entities model but they are reducing bidirectional references and actually each endpoint has
 its own model which is its own view on JPA entities, some part/scope of the JPA model to limit serialization so 
 if the same JPA entities are serialized with different scope there is a need to duplicate DTO classes to create another
  view on JPA entities. This is not the end of the story, we need to somehow copy data from JPA entities to DTO 
  objects and from DTO objects to JPA entities when client executes updates, there is required following data transfers
   between models:
   + JPA -> DTO          
   + DTO -> JPA          
due to different types on input and output usually we have to create two separate operation for each endpoint 
which will execute these data transfer between models -> endless calling of getters and setters
  
DTO in fact solves real problem. JSON scopes library is intended to solve these problems too but in smarter way. 
JPA entities serialization without model duplications which allows to focus on the server side on real business logic
and in spirit it is close to GraphQL but it has tight integration with JPA and Spring 

# JSON Scopes introduction
Main problem solved by JSON Scopes is ability to say on the REST endpoint level how much data should serialize
this endpoint from the root of serialization and the second solved problem is a smart object network serialization boundaries
which allows to:
  + send data to client (usually web application)
  + safely modify such data on the client   
  + send back this object network to server and safely merge it into JPA EntityManager
  
This library is an extension to GSON serialization library and Spring REST but this idea could be implemented 
for others `Java to JSON` serialization libraries.

## object network serialization boundaries
If we take one to one association in JPA entity when we want to cut serialisation on such reference and for example 
we don't send to client value when client will resend such object in response we get null which is problematic as it was mentioned.
To avoid such problems there is introduced concept of "lazy loads" in JSON
which is similar to lazy loads in JPA which allows to limit JPA entity network which is loaded from database 
### one-to-one reference
For one-to-one reference such lazy load reference in JSON is object which has only object id of referred object
such id contains referred object type and UUID of this object, UUID's are used for entity's primary key because they allow to
generate objects id's on the client side without communication with the server. Object type is appended to entity id during 
serialization to JSON and it is Java class name in kebab case. Type is required because in JPA we can have polymorphic associations 

Following Java model (JPA annotations are skipped for simplicity purposes):
```java
   import java.util.UUID;
   class Person{
       UUID id;
       String name;  
       String secondName;
       PersonAddress personAddress;   
   } 
   class PersonAddress{
        UUID id;
        String street;
        int number;
        Person person; 
   }    
```   

after serialization to JSON a person who has an address which we don't want to serialize we get a following JSON object
```JSON
   { 
    "id": "person:6c25a0da-098f-11ea-8d71-362b9e155667",
     "name": "John",
      "secondName":"Smith",
       "personAddress": {
                          "id":"person-address/7206c33a-098f-11ea-8d71-362b9e155667" 
                        }      
    }

```
and if we want to have in JSON person address
```JSON
   { 
    "id": "person:6c25a0da-098f-11ea-8d71-362b9e155667",
     "name": "John",
      "secondName":"Smith",
       "personAddress": {
                          "id":"person-address:7206c33a-098f-11ea-8d71-362b9e155667", 
                          "street": "Oxford street",
                          "number": 11,
                          "person": {"id":"person/6c25a0da-098f-11ea-8d71-362b9e155667"}
                        }      
    }
```

in JSON object containing only id is a proxy, we are able to change it and refer other object but we cannot modify other
fields of proxy until we don't load this object (like in JPA). In the second JSON example we see that proxy is used to refer 
person from personAddress to avoid cyclic references in JSON.

**what gives such approach??**
on the server when such JSON is get from client during deserialization in places where proxy is used JSON Scopes 
deserializer inserts Hibernate lazy proxy and in effect deserialized object network is valid network of JPA entities 
in state **detached**. What can be done with detached entities? they can be merged to JPA EntityManager which will transfer
changes made by client on entities which are in scope of current EntityManager which causes that all this play with coping data
from DTO to entities is not required. Developer should provide only change authorisation, validation and commit transaction
which gives huge reduction in server side code

On the web application side specialized JSON deserializer provided also by JSON Scopes library could change JSON object tree got
from server into JavaScript object graph based on object proxies in the deserialized JSON if referenced object is in this JSON
In the given example in case where person has address, in address object field person is changed to person which refers this address
During serialization JavaScript objects to JSON which have cycles, specialized JSON serializer breaks cycles by use of proxies

  
### one-to-many reference
for collection in JPA entity if it is no loaded JPA proxy it serialize such collection field as null which is save in this case,
because it is different from empty collection, so null for collection means that client is not allowed to touch such collection 
because it hasn't been loaded from server. During deserialization on server from JSON to Java for such collection 
there is created Hibernate proxy collection which allows later to merge with EntityManager.

All collections supported by hibernate with lazy loads are supported by JSON Scopes:
  + Collection
  + Set
  + List
  + Map
  

## Integration with Spring framework
For REST endpoints developed in Spring MVC, there is provided a new annotation `JsonScope` with boolean field `positive` 
which by default is `true` this field says 
  + if JPA classes listed in this entities should be serialised (`positive=true`) and other met in network skipped by use of proxies 
  + or JPA classes listed in this annotation should be skipped (`positive=false`) and other classes met during serialisation serialized.
  
This is similar to GraphQL concept where scope of serialization is defined by fields names in the graph, here in JSON Scopes
serialisation scope is defined by list of classes which should be serialized or not, depending on scope type => positive/negative

`JsonScope` can be used on method level or class level. For fetching data from server it limits data sent to client, 
for data send from client it cut off part of JSON which is out of scope so client is not able to modify data which is not
 allowed. Client could send JSON which doesn't cover whole defined scope on endpoint such data is deserialized and after merge into EntityManger
 endpoint code could safely operate on such data. It is helpful when client modified only few objects from scope and 
 it doesn't want to send whole graph of objects just to not to break server code     

## Project configuration 
To enable json scopes serializer and JsonScope annotations, gson serializer must be configured in spring like it is done in 
json-scopes-examples in class GsonConfiguration
in following way:
```java
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
``` 
all JPA entities must be derived from `BaseEntity` 

## Json Scopes Examples
To demonstrate json scopes flexibility in fetching and sending data to server there is created maven module `json-scopes-example`
which exposes spring REST api for simple shop, where clients could create orders for selected products.
`ShoppingTest` is set of methods which shows full sequence of operations and they have to be run together. `ShoppingTest` simulates
Java Scripts clients and calls REST API by spring mock mvc. To avoid loops in objects or limits object graph sent to serer
`ShoppingTest` creates object references (JSON object which contains only object id). In JS developer doesn't have to care about it
because it is solved automatically by JsonScopedSerializer TypesScript class. What is important in `ShoppingTest` it is what is get from server
in responses and what is send to server in requests and how it is deserialized to JPA entities according `JsonScope` annotation 

               
 


     


  
   




                   

         