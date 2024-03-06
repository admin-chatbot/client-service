FROM maven:3.8.6-amazoncorretto-17 AS build
CMD mkdir /app
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -f pom.xml clean package -DskipTests
RUN cp target/client-service-0.0.2.jar client-service.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","client-service.jar"]