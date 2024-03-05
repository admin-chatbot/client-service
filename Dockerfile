FROM amazoncorretto:17
EXPOSE 80
RUN mvn clean package -DskipTests
ADD target/client-service-0.0.2.jar client-service-0.0.2.jar
ENTRYPOINT ["java","-jar","client-service-0.0.2.jar"]
