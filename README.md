# Spring Boot Elastic Cloud APM & Logstash Demo

This is a complete Spring Boot application demonstrating integration with Elastic APM and Logstash for comprehensive
monitoring, logging, and observability.

## Features

- **Spring Boot 3.1.5** with Java 17
- **Elastic APM** integration for application performance monitoring
- **Logstash** integration for centralized logging
- **H2 Database** for demonstration (easily replaceable with PostgreSQL/MySQL)
- **RESTful API** with full CRUD operations
- **Structured logging** with JSON format
- **Docker support** with complete ELK stack
- **Health checks and metrics** via Spring Actuator

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (for local ELK stack)

## Quick Start

### 1. Local Development (without Docker)

```bash
# Clone and build
mvn clean package

# Run the application
mvn spring-boot:run

# Or run the jar
java -jar target/elastic-demo-1.0.0.jar
```

The application will start on `http://localhost:8080`

### 2. With Docker (Full ELK Stack)

```bash
# Build and start all services
docker-compose up --build

# Services will be available at:
# - Application: http://localhost:8080
# - Elasticsearch: http://localhost:9200
# - Kibana: http://localhost:5601
# - APM Server: http://localhost:8200
```

## API Endpoints

### User Management

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/search?name={name}` - Search users by name

### Testing Endpoints

- `POST /api/users/simulate-error` - Simulate application error
- `POST /api/users/simulate-slow` - Simulate slow operation

### Monitoring

- `GET /actuator/health` - Health check
- `GET /actuator/metrics` - Application metrics
- `GET /h2-console` - H2 Database console (dev profile)

## Sample API Calls

```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'

# Get all users
curl http://localhost:8080/api/users

# Get user by ID
curl http://localhost:8080/api/users/1

# Update user
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Jane Doe", "email": "jane@example.com"}'

# Search users
curl "http://localhost:8080/api/users/search?name=John"

# Simulate error for testing
curl -X POST http://localhost:8080/api/users/simulate-error

# Simulate slow operation
curl -X POST http://localhost:8080/api/users/simulate-slow
```

## Configuration

### Environment Variables

```bash
# Elastic APM Configuration
ELASTIC_APM_SERVER_URL=https://your-apm-server.apm.us-central1.gcp.cloud.es.io:443
ELASTIC_APM_SECRET_TOKEN=your-secret-token
ELASTIC_APM_ENVIRONMENT=production

# Logstash Configuration
LOGSTASH_HOST=your-logstash-host
LOGSTASH_PORT=5044

# Application Configuration
SPRING_PROFILES_ACTIVE=production
```

### Elastic Cloud Configuration

For Elastic Cloud, update your `application.yml`:

```yaml
elastic:
  apm:
    server-url: https://your-cluster.apm.us-central1.gcp.cloud.es.io:443
    secret-token: your-secret-token
    service-name: elastic-demo-app
    environment: production
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/elasticdemo/
│   │   ├── ElasticDemoApplication.java      # Main application class
│   │   ├── controller/
│   │   │   └── UserController.java          # REST API endpoints
│   │   ├── service/
│   │   │   └── UserService.java             # Business logic with APM annotations
│   │   ├── repository/
│   │   │   └── UserRepository.java          # JPA repository
│   │   └── model/
│   │       └── User.java                    # Entity model
│   └── resources/
│       ├── application.yml                  # Application configuration
│       └── logback-spring.xml              # Logging configuration
├── docker-compose.yml                      # Local ELK stack
├── Dockerfile                              # Application container
├── logstash/
│   ├── config/logstash.yml                 # Logstash configuration
│   └── pipeline/logstash.conf              # Log processing pipeline
└── pom.xml                                 # Maven dependencies
```

## Key Features Explained

### 1. Elastic APM Integration

- Automatic transaction and span tracking
- Custom span annotations with `@CaptureSpan` and `@CaptureTransaction`
- Performance monitoring and error tracking
- Distributed tracing support

### 2. Structured Logging

- JSON format logs using Logstash encoder
- MDC (Mapped Diagnostic Context) for contextual information
- Correlation IDs for request tracking
- Different log levels for various environments

### 3. Observability

- Spring Boot Actuator for health checks and metrics
- Custom metrics exported to Elastic
- Error simulation endpoints for testing
- Performance monitoring with slow operation simulation

### 4. Database Integration

- H2 in-memory database for development
- JPA/Hibernate for data persistence
- Repository pattern for data access
- Database query logging

## Monitoring and Dashboards

### Kibana Dashboards

After starting the application, create these visualizations in Kibana:

1. **Log Analysis Dashboard**
    - Log level distribution
    - Error rate over time
    - Top error messages
    - Service performance metrics

2. **APM Dashboard**
    - Request throughput
    - Response times
    - Error rates
    - Database query performance

### Sample Kibana Queries

```
# Filter by service
service_name: "elastic-demo-app"

# Filter by log level
log_level: "ERROR"

# Filter by user operations
message: "User*"

# Performance issues
@fields.executionTime: >1000
```

## Testing the Integration

### 1. Generate Test Data

```bash
# Create multiple users
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/users \
    -H "Content-Type: application/json" \
    -d "{\"name\": \"User$i\", \"email\": \"user$i@example.com\"}"
done

# Generate errors
for i in {1..5}; do
  curl -X POST http://localhost:8080/api/users/simulate-error
done

# Generate slow operations
for i in {1..3}; do
  curl -X POST http://localhost:8080/api/users/simulate-slow
done
```

### 2. View in Kibana

1. Navigate to `http://localhost:5601`
2. Go to **Discover** to view logs
3. Go to **APM** to view application performance
4. Create custom dashboards for monitoring

## Production Deployment

### 1. Build for Production

```bash
mvn clean package -Pprod
```

### 2. Docker Deployment

```bash
docker build -t elastic-demo:1.0.0 .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e ELASTIC_APM_SERVER_URL=your-apm-url \
  -e ELASTIC_APM_SECRET_TOKEN=your-token \
  elastic-demo:1.0.0
```

### 3. Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: elastic-demo
spec:
  replicas: 3
  selector:
    matchLabels:
      app: elastic-demo
  template:
    metadata:
      labels:
        app: elastic-demo
    spec:
      containers:
        - name: elastic-demo
          image: elastic-demo:1.0.0
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "production"
            - name: ELASTIC_APM_SERVER_URL
              valueFrom:
                secretKeyRef:
                  name: elastic-config
                  key: apm-server-url
            - name: ELASTIC_APM_SECRET_TOKEN
              valueFrom:
                secretKeyRef:
                  name: elastic-config
                  key: apm-secret-token
```

## Troubleshooting

### Common Issues

1. **APM Agent Not Connecting**
    - Verify `ELASTIC_APM_SERVER_URL` is correct
    - Check network connectivity to APM server
    - Validate secret token

2. **Logs Not Appearing in Kibana**
    - Check Logstash configuration
    - Verify Elasticsearch connectivity
    - Ensure index patterns are created

3. **High Memory Usage**
    - Adjust JVM heap size with `JAVA_OPTS`
    - Configure APM agent memory settings
    - Monitor application metrics

### Debug Commands

```bash
# Check application health
curl http://localhost:8080/actuator/health

# View application metrics
curl http://localhost:8080/actuator/metrics

# Check Elasticsearch cluster health
curl http://localhost:9200/_cluster/health

# View Logstash pipeline stats
curl http://localhost:9600/_node/stats/pipelines
```

## Performance Tuning

### JVM Settings

```bash
export JAVA_OPTS="-Xms1024m -Xmx2048m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### APM Agent Settings

```yaml
elastic:
  apm:
    transaction-sample-rate: 0.1  # Sample 10% of transactions
    capture-body: errors          # Only capture request body on errors
    stack-trace-limit: 50         # Limit stack trace depth
```

### Logstash Optimization

```ruby
output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "logs-%{+YYYY.MM.dd}"
    template_name => "spring-logs"
    workers => 4
    flush_size => 500
    idle_flush_time => 1
  }
}
```

## Security Considerations

1. **Never commit sensitive data**
    - Use environment variables for secrets
    - Configure proper secret management

2. **Network Security**
    - Use HTTPS for Elastic Cloud connections
    - Configure proper firewall rules
    - Implement authentication and authorization

3. **Log Security**
    - Avoid logging sensitive information
    - Implement log sanitization
    - Configure proper log retention policies

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Support

For issues and questions:

- Check the [Elastic Stack documentation](https://www.elastic.co/guide/)
- Review Spring Boot [documentation](https://spring.io/projects/spring-boot)
- Open an issue in the repository