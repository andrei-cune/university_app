FROM maven:3.5-jdk-8
WORKDIR /app
COPY pom.xml /app/
COPY src /app/src
RUN mvn clean install -DskipTests
ENTRYPOINT ["java", "-jar", "target/university-processor-0.0.1-SNAPSHOT.jar"]
