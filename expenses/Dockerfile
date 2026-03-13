# build stage
FROM gradle:8.5-jdk21 AS build

WORKDIR /app
COPY . .

RUN ./gradlew bootJar -x test

# runtime stage
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]