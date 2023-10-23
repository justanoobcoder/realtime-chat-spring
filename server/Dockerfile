FROM eclipse-temurin:17-jdk-jammy AS resolve-dependencies
WORKDIR /app
COPY .mvn .mvn
COPY pom.xml mvnw ./
RUN --mount=type=cache,target=/root/.m2 ["./mvnw", "dependency:go-offline", "-B"]


FROM resolve-dependencies AS build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 ["./mvnw", "package", "-DskipTests"]


FROM eclipse-temurin:17-jre-jammy AS run
EXPOSE 8080
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]