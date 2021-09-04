# Microservices-ecommerce-project-demo
Demo Maven Project to show case how basic microservices works (with minimal business logic). Implemented Eureka, API-Gateway, Config-server, Resilience4j, RabbitMQ, Keycloak, Vault, zipkin/sleuth, logstash, elasticsearch, kibana and four basic services for ecommerce.

# Why Microservices?
Microservice architecture - is an architectural style that structures an application as a collection of services that are

=> Highly maintainable and testable
=> Loosely coupled
=> Independently deployable
=> Organized around business capabilities
=> Owned by a small team
=> The microservice architecture enables the rapid, frequent and reliable delivery of large, complex applications. It also enables an organization to evolve its technology stack.

# My Project's Technical-Stack :
![](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/PSX_20210904_171828.jpg)

# Open both Databases as we're implementing polygot persistance :  MySql, MongoDB.
![MySql after inventory-service being loaded the data.sql file uploads data to DB ](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/mysql.JPG)
![MongoDB after being hit POST request by product-service API ](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/MongoDBProduct.JPG)

# HashiCorp VAULT : Open vault server from where its installed using cmd and type command.
     In my system  => D:\Window10 Softwares\vault_1.7.0_windows_amd64> vault server -dev
- (now copy the unseal key & token. Put the token in bootstrap.properties files of order-service, inventory-service, product-service).
- Open new cmd from same location & type: cmd> set VAULT_ADDR=http://127.0.0.1:8200
- cmd> now go to location of order-service json file using ‘cd’,  say   D:\ProjectEcommerce-Maven\order-service. And then type command to generate key-value pair to store data(as value) in vault. I choosed json file instead of saving key-values pairs one by one, thus created json with required secrets in project folder of order-service.
- cmd> vault kv put secret/order-service @order-service-credentials.json
Verify your secret data get saved in vault or not in key-value pair by using command,
                                        cmd> vault kv get secret/order-service
- Repeat same above thing for product-service & inventory-service.

#Note : Everytime you restart vault, you need to change your TOKEN in your all properties files.

# RABBITMQ : Either use Docker, or Open RabbitMq server in my system with path.
      C:\Program Files\RabbitMQ Server\rabbitmq_server-3.8.8\sbin> rabbitmq-plugins enable rabbitmq_management
- Now open browser and enter, http://localhost:15672 
- Login with default credentials, guest/guest.
- ![After login when all microservices are started](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/rabbitMQ1.JPG)

# KEYCLOAK : Open keycloak server for Identity and Access Management. Go to  =>  
                               D:\keycloak-12.0.4\bin> standalone.bat -Djboss.http.port=8180
- Now login to url : http://localhost:8180/auth/ with admin/admin as credentials.
- After running all services, to get your resource secured by keycloak : http://localhost:8080/api/product 
- My keycloak realm => microservice-realm
- Credentials => user: 'test'     &nbsp;   password: 'test'

![When user been created inside Keycloak](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/keycloak.JPG)

- Once you access the address http://localhost:8080 you should get a Keycloak Login screen, once you logged in, you will see the session id as the response.
![to get seesionId hit link : ](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/4.JPG)

- Source from I learned Keycloak : 
          ~ https://www.keycloak.org/ 
          ~ https://www.baeldung.com/postman-keycloak-endpoints 

# How Logstash, Elasticsearch and Kibana does Logging : 

![](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/IMG_20210827_022150.jpg)

# LOGSTASH (port - 9600): Set up ‘logstash.conf’ file as per requirement (input, filter, output)  => 
                           D:\logstash-7.12.0\bin\logstash.conf
                   - Now either spin logstash through Docker, else Go to ,
	                         D:\logstash-7.12.0\bin>logstash.bat -f logstash.conf
# ELASTICSEARCH (port - 9200): Either use Docker, or Go to
                    D:\elasticsearch-7.12.0\bin>elasticsearch.bat
        ~ Now open browser and enter, http://localhost:9200/_cat/indices

![](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/elasticSearch.JPG)

# KIBANA (port - 5601):  Ensure you’ve this line, “elasticsearch.hosts: ["http://localhost:9200"]” 
                     inside “D:\kibana-7.12.0-windows-x86_64\config\kibana.yml” file.
     - Now either use Docker or Go to, 
                             D:\kibana-7.12.0-windows-x86_64\bin>kibana.bat
  - Now open browser and enter, http://localhost:5601 

![](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/Screenshot_2021-09-04-16-53-14-08_4aed3257f278fcf7bfa3abd644e23333.jpg)

# ZIPKIN : Go to place where you had downloaded zipkin (In my system, D:\Window10 Softwares). 
     ~ Now use command:
       cmd> java -jar zipkin-server-2.12.9-exec.jar
  ~ Now go to => http://localhost:9411/zipkin 
- ![Tracking using zipkin : ](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/zipkin.JPG)

# Open All Microservices APIs In Intellij : 

- Order => eureka, config-server, product-service, order-service, inventry-service, notification-service, api-gateway.

- Use "config-server" service from my earlier developed repository. Link : https://github.com/AadityaUoHyd/config-server/tree/master

- Eureka-Server :
![Alt After all services been started. Started two instances of order,inventory & product service](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/eureka1.JPG)

![POST request for product-service via POSTMAN](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/productPostman.JPG)
![Hitting Get Request for product-service api in browser](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/product.JPG)


![Auth Header with SessionId](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/3.JPG)
![success with order-service](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/1.JPG)
![when order got success](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/postOrderPlacedMysql.JPG)

![when inventory-service returns to order-service, stock is unavialable](https://github.com/AadityaUoHyd/Microservices-ecommerce-project-demo/blob/master/2.JPG)
