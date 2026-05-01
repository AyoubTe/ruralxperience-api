# ================================
# Stage 1: Build
# ================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

RUN java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

# ================================
# Stage 2: Runtime
# ================================
FROM eclipse-temurin:21-jre-jammy AS runtime

RUN groupadd --system rxp_users && useradd --system --gid rxp_users rxp_user

WORKDIR /app

COPY --from=builder --chown=rxp_user:rxp_users /app/target/extracted/dependencies/ ./
COPY --from=builder --chown=rxp_user:rxp_users /app/target/extracted/spring-boot-loader/ ./
COPY --from=builder --chown=rxp_user:rxp_users /app/target/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=rxp_user:rxp_users /app/target/extracted/application/ ./

USER rxp_user

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "org.springframework.boot.loader.launch.JarLauncher"]