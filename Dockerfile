FROM maven:3.9.3-eclipse-temurin-20-alpine
LABEL maintainer="jorge"
COPY . /usr/src/zippy
WORKDIR /usr/src/zippy
RUN mvn clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/api-1.jar"]
