# pricing-service
To build project you should:
1. Start mysql server.
2. Create datebase in mysql server by script from pricing-service/src/main/resources/mysql_schema.sql file.
3. Fill in properties 'spring.datasource.url', 'spring.datasource.username', 'spring.datasource.password'
    according to yours in config file pricing-service/src/main/resources/application.properties.
4. Execute 'mvn package && java -jar target/pricing-service-0.0.1-SNAPSHOT.jar' in command line from project folder.
