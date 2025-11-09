FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/commerce.service-0.0.1-SNAPSHOT.jar /app/commerce.service.jar

EXPOSE 8080

CMD ["java", "-jar", "commerce.service.jar"]