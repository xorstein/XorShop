FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build XorshopWebParent/XorshopFrontEnd/target/XorshopFrontEnd-0.0.1-SNAPSHOT.jar XorshopFrontEnd.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","XorshopFrontEnd.jar"]