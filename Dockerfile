# Shared multi-stage build for the whole AcademiaX Maven reactor.
# One "build" stage compiles every module once; each service gets its own
# small named runtime stage below, selected via `docker build --target=<name>`
# (docker-compose.yml's `build.target` does this per service). `common` is a
# library module with no main class — it has no runtime stage of its own,
# it's only ever a build-time reactor dependency of the others.

FROM maven:3.9.9-eclipse-temurin-22 AS build
WORKDIR /app

# Copy every module's pom.xml first (and nothing else) so dependency
# resolution is its own cached layer — editing Java source later won't
# invalidate this and force re-downloading the internet.
COPY pom.xml ./
COPY common/pom.xml common/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY auth-service/pom.xml auth-service/pom.xml
COPY user-management/pom.xml user-management/pom.xml
COPY student-management/pom.xml student-management/pom.xml
COPY academic-management/pom.xml academic-management/pom.xml
COPY faculty-management/pom.xml faculty-management/pom.xml
COPY attendance-management/pom.xml attendance-management/pom.xml
COPY examination-management/pom.xml examination-management/pom.xml
COPY platform-core/pom.xml platform-core/pom.xml
RUN mvn -q -B dependency:go-offline || true

COPY . .
RUN mvn -q -B clean package -DskipTests

# ---------------------------------------------------------------------------
# Runtime stages — one per runnable module. Each just copies its own jar out
# of the shared build stage onto a slim JRE. The /dev/tcp healthcheck is a
# liveness/port-bound check (the port is open), not a readiness check (DB
# connected, Eureka-registered) — none of these services except eureka-server
# and api-gateway have the actuator starter for a real /actuator/health, and
# adding it everywhere is out of scope for this pass.
# ---------------------------------------------------------------------------

FROM eclipse-temurin:22-jre AS eureka-server
WORKDIR /app
COPY --from=build /app/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8761
HEALTHCHECK --interval=10s --timeout=3s --start-period=30s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8761' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS api-gateway
WORKDIR /app
COPY --from=build /app/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --start-period=30s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8080' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS auth-service
WORKDIR /app
COPY --from=build /app/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8081' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS user-management
WORKDIR /app
COPY --from=build /app/user-management/target/user-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8082' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS student-management
WORKDIR /app
COPY --from=build /app/student-management/target/student-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8083' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS academic-management
WORKDIR /app
COPY --from=build /app/academic-management/target/academic-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8084' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS faculty-management
WORKDIR /app
COPY --from=build /app/faculty-management/target/faculty-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8085' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS attendance-management
WORKDIR /app
COPY --from=build /app/attendance-management/target/attendance-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8087
HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8087' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS examination-management
WORKDIR /app
COPY --from=build /app/examination-management/target/examination-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8088
HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8088' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:22-jre AS platform-core
WORKDIR /app
COPY --from=build /app/platform-core/target/platform-core-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8089
HEALTHCHECK --interval=10s --timeout=3s --start-period=45s --retries=10 \
    CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8089' || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
