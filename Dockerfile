## Use a multi-stage build for caching dependencies
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
WORKDIR /app
# copy the pom.xml file to the container
COPY . .
#show logs when installing dependencies
RUN mvn -T 4 clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /app /app
EXPOSE 8080
ENV SERVER_PORT=8080
CMD ["java", "--enable-preview", "-jar", "target/Todos.jar"]