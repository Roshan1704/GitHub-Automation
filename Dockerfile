FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app
COPY . .

RUN mvn -B -DskipTests clean package

CMD ["mvn", "-B", "-Ptest", "test"]
