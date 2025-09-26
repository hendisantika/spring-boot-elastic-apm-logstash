FROM amazoncorretto:25-alpine3.22
LABEL authors="hendisantika"

# Set working directory
WORKDIR /app

# Copy the jar file
COPY target/*.jar app.jar

# Create logs directory
RUN mkdir -p logs

# Environment variables
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
#ENV SPRING_PROFILES_ACTIVE=production

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]