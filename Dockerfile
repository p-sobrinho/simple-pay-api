FROM gradle:8.14.3-jdk21 AS build

COPY build.gradle.kts settings.gradle.kts /app/
COPY src /app/src

WORKDIR /app

RUN gradle clean bootJar --no-daemon


FROM amazoncorretto:21

COPY --from=build /app/build/libs/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]