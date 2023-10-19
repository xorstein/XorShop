FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build XorShopWebParent/XorShopFrontEnd/target/XorShopFrontEnd-0.0.1-SNAPSHOT.jar XorShopFrontEnd.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","XorShopFrontEnd.jar"]