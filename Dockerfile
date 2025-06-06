FROM openjdk:17-jdk

WORKDIR /app

COPY target/kafka-yandex-0.0.1.jar app.jar

COPY ./kafka-schema ./kafka-schema

ENTRYPOINT ["java", "-jar", "app.jar"]
