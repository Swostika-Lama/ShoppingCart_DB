# ---------- BUILD STAGE ----------
FROM maven:3.8-amazoncorretto-21-debian AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

#RUN mvn -q -DskipTests package
RUN mvn clean package -DskipTests

# ---------- RUNTIME STAGE ----------
FROM maven:3.9-eclipse-temurin-21

ENV DISPLAY=host.docker.internal:0.0

RUN apt-get update && apt-get install -y wget unzip libgtk-3-0 libgbm1 libx11-6 && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN wget https://download2.gluonhq.com/openjfx/21.0.11/openjfx-21.0.11-ea+3_linux-aarch64_bin-sdk.zip -O /tmp/openjfx.zip && unzip /tmp/openjfx.zip -d /opt && mv /opt/javafx-* /opt/openjfx && rm /tmp/openjfx.zip

WORKDIR /app

COPY --from=build /app/target/ShoppingCartApp.jar app.jar

ENTRYPOINT ["java", "--module-path", "/opt/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]