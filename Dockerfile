# syntax=docker/dockerfile:1.7-labs

# Stage 1: Build application
FROM eclipse-temurin:17-jdk-alpine as builder

# Set working directory
WORKDIR /builder

# Set environment variables for Maven Wrapper (see mvnw)
ARG MVNW_REPOURL=https://repo.maven.apache.org/maven2
ARG MVNW_USERNAME
ARG MVNW_PASSWORD
ARG MVNW_VERBOSE
ENV MVNW_REPOURL=${MVNW_REPOURL} \
    MVNW_USERNAME=${MVNW_USERNAME} \
    MVNW_PASSWORD=${MVNW_PASSWORD} \
    MVNW_VERBOSE=${MVNW_VERBOSE}

# Set up Maven settings with mirror repository
RUN --mount=type=cache,target=/root/.m2 \
    echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" \
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \
                   xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 \
                                       http://maven.apache.org/xsd/settings-1.0.0.xsd">' > /root/.m2/settings.xml && \
    echo '<mirrors>' >> /root/.m2/settings.xml && \
    echo '  <mirror>' >> /root/.m2/settings.xml && \
    echo '    <id>mirror</id>' >> /root/.m2/settings.xml && \
    echo '    <mirrorOf>*</mirrorOf>' >> /root/.m2/settings.xml && \
    echo '    <url>'${MVNW_REPOURL}'</url>' >> /root/.m2/settings.xml && \
    echo '  </mirror>' >> /root/.m2/settings.xml && \
    echo '</mirrors>' >> /root/.m2/settings.xml && \
    echo '</settings>' >> /root/.m2/settings.xml

# Copy necessary files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Add executable privilege to mvnw
RUN chmod +x ./mvnw

# Copy source code
COPY --parents server .
COPY --parents admin-* .
COPY --parents module-* .

# Download dependencies
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline

# Build application jar
RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests

# Extract layers
RUN java -Djarmode=layertools -jar server/target/*.jar extract --destination extracted

# Stage 2: Create runtime image
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy layers
ARG EXTRACTED=/builder/extracted
COPY --from=builder $EXTRACTED/dependencies/ ./
COPY --from=builder $EXTRACTED/snapshot-dependencies/ ./
COPY --from=builder $EXTRACTED/application/ ./

# Create non-root user
RUN addgroup -g 1000 spring && adduser -u 1000 -G spring -s /bin/sh -D spring
USER spring:spring

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_MAIN_CLASS=io.summernova.admin.Application
ENV JAVA_OPTS=""

# Set entrypoint
ENTRYPOINT ["sh", "-c", "exec java -cp BOOT-INF/classes:BOOT-INF/lib/* -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} ${JAVA_OPTS} ${JAVA_MAIN_CLASS}"]

# Set default CMD arguments
CMD []