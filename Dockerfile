FROM amazoncorretto:17
EXPOSE 8080
ADD target/client-service-0.0.2.jar client-service-0.0.2.jar
ENTRYPOINT ["java","-jar","client-service-0.0.2.jar"]