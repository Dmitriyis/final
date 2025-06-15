FROM openjdk:17-jdk

WORKDIR /app

COPY target/kafka-yandex-0.0.1.jar app.jar

COPY kafka-schema-registry ./kafka-schema

ENTRYPOINT ["java", "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED", "-jar", "app.jar"]

