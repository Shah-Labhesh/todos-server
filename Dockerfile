## Use a multi-stage build for caching dependencies
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
WORKDIR /app
# copy the pom.xml file to the container
COPY . /app/
#show logs when installing dependencies
RUN mvn -T 4 clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/Todos.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","Todos.jar"]
