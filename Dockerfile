
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw
COPY pom.xml .
#RUN ./mvnw dependency:resolve
COPY src src
RUN ./mvnw package -DskipTests

# --------------------------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 9004
ENTRYPOINT ["java", "-jar", "app.jar", "--server.address=0.0.0.0"]



