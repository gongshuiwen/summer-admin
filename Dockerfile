# syntax=docker/dockerfile:1.7-labs
# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine as builder

# Set the working directory
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

# Copy the necessary files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Add executable privilege to mvnw
RUN chmod +x ./mvnw

# Copy the source code
COPY --parents erp-* .

# Download dependencies
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline

# Build application jar
RUN --mount=type=cache,target=/root/.m2 ./mvnw install -DskipTests

# Extract the layers
RUN java -Djarmode=layertools -jar erp-server/target/*.jar extract --destination extracted

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the layers
ARG EXTRACTED=/builder/extracted
COPY --from=builder $EXTRACTED/dependencies/ ./
COPY --from=builder $EXTRACTED/snapshot-dependencies/ ./
COPY --from=builder $EXTRACTED/application/ ./

# Create a non-root user in a more secure way
RUN addgroup -g 1000 spring && adduser -u 1000 -G spring -s /bin/sh -D spring
USER spring:spring

# Set the environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Set the entrypoint
ENTRYPOINT ["sh", "-c", "exec java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} ${JAVA_OPTS} -cp BOOT-INF/classes:BOOT-INF/lib/* ${MAIN_CLASS}"]

# Set default CMD arguments
CMD []