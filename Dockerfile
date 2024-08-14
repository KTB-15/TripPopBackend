# 1. Build stage
FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# 2. Run stage
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]