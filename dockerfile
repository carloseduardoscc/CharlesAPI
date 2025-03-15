FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/charles_api-3.4.3.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
