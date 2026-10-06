# SupportDesk API (Backend)

[![Java](https://img.shields.io/badge/Java-17-orange.svg?style=flat-square&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F.svg?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F.svg?style=flat-square&logo=springsecurity)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Supabase-4169E1.svg?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D.svg?style=flat-square&logo=swagger)](https://swagger.io/)
[![Docker](https://img.shields.io/badge/Docker-Container-2496ED.svg?style=flat-square&logo=docker)](https://www.docker.com/)

API RESTful desarrollada con **Spring Boot** y **Java 17** para el sistema de gestión de tickets **SupportDesk**. Proveé servicios de autenticación segura (JWT), gestión de tickets, control de usuarios con Roles, notificaciones vía WebSocket y documentación OpenAPI / Swagger UI.

---

## 🚀 Repositorios y Despliegue

* **Servicio Backend en Render:** Desplegado mediante Docker
* **Frontend en Vercel:** [supportdesk-frontend](https://supportdesk-frontend-nine.vercel.app/)
* **Repositorio Frontend GitHub:** [jordantejadadev/supportdesk-frontend](https://github.com/jordantejadadev/supportdesk-frontend)

---

## 🛠️ Tecnologías y Dependencias

* **Java 17** & **Spring Boot 4.0.6** (Web MVC, Validation, Mail)
* **Spring Data JPA** & **Hibernate** (ORM y persistencia de datos)
* **PostgreSQL** & **Supabase** (Base de datos relacional)
* **Spring Security** & **JJWT (`0.11.5`)** (Autenticación sin estado con tokens JWT y Refresh Tokens)
* **Spring WebSocket** & **STOMP** (Sincronización en tiempo real)
* **Springdoc OpenAPI (`2.8.9`)** (Documentación interactiva de la API con Swagger UI)
* **Lombok** (Generación automática de getters, setters y constructores)
* **JaCoCo (`0.8.12`)** (Medición de cobertura de pruebas unitarias)
* **Docker** (Containerización lista para despliegue en Render)

---

## 📋 Características Principales

1. **Autenticación y Seguridad Avanzada:**
    * Registro de usuarios (`ROLE_SUPPORT` por defecto) y autenticación mediante JWT.
    * Manejo de **Refresh Tokens** persistentes para renovación de sesiones.
    * Filtros de seguridad personalizados (`JwtFilter`, `JwtService`).

2. **Gestión de Tickets:**
    * Operaciones CRUD para solicitudes de soporte.
    * Estados configurables: `ABIERTO`, `EN_PROGRESO`, `CERRADO`.
    * Búsqueda por asunto y filtrado por estado.
    * Eliminación de tickets restringida al administrador (`ADMIN`).

3. **WebSockets e Interceptor JWT:**
    * Configuración de WebSockets con `JwtHandshakeInterceptor` para validar peticiones en tiempo real.

4. **Manejo Global de Excepciones:**
    * Controlador centralizado (`GlobalExceptionHandler`) para respuestas uniformes (`ResourceNotFoundException`, `ResourceAlreadyExistsException`, `UnauthorizedException`).

---

## 📁 Estructura del Proyecto

```text
src/main/java/com/jordan/ticket_system/
├── config/                  # Configuraciones (CORS, WebSocket, OpenAPI, DataLoader)
│   ├── CorsConfig.java
│   ├── DataLoader.java
│   ├── JwtHandshakeInterceptor.java
│   ├── OpenApiConfig.java
│   └── WebSocketConfig.java
├── controller/              # Controladores REST API
│   ├── AuthController.java
│   ├── HealthController.java
│   ├── TicketController.java
│   └── UserController.java
├── dto/                     # Objetos de Transferencia de Datos (DTOs)
│   ├── AuthResponse.java
│   ├── ErrorResponse.java
│   ├── LoginRequest.java
│   ├── RegisterRequestDTO.java
│   ├── TicketResponse.java
│   ├── TicketResponseDTO.java
│   ├── UpdateEstadoRequest.java
│   ├── UpdateUserRequestDTO.java
│   ├── UserRequestDTO.java
│   └── UserResponseDTO.java
├── entity/                  # Entidades JPA (PostgreSQL)
│   ├── EstadoTicket.java
│   ├── RefreshToken.java
│   ├── Role.java
│   ├── Ticket.java
│   └── User.java
├── exception/               # Manejo global de errores personalizados
│   ├── GlobalExceptionHandler.java
│   ├── ResourceAlreadyExistsException.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
├── mapper/                  # Mapeadores de Entidad a DTO
│   ├── TicketMapper.java
│   └── UserMapper.java
├── repository/              # Repositorios Spring Data JPA
│   ├── RefreshTokenRepository.java
│   ├── TicketRepository.java
│   ├── TokenRepository.java
│   └── UserRepository.java
├── security/                # Configuración de Spring Security & Filtros JWT
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtFilter.java
│   ├── JwtService.java
│   └── SecurityConfig.java
└── service/                 # Capa de Lógica de Negocio e Implementaciones
    ├── impl/
    │   ├── AuthServiceImpl.java
    │   ├── EmailReaderServiceImpl.java
    │   ├── TicketServiceImpl.java
    │   ├── TokenServiceImpl.java
    │   └── UserServiceImpl.java
    ├── AuthService.java
    ├── EmailReaderService.java
    ├── TicketService.java
    └── UserService.java
```

---

## ⚙️ Configuración (`application.properties`)

La aplicación utiliza variables de entorno con valores por defecto para entorno local:

```properties
# Base de Datos
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/ticketsdb}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:123456}

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Servidor
server.port=8080

# Configuración de Correo (IMAP / Mail)
spring.mail.host=${SPRING_MAIL_HOST:imap.gmail.com}
spring.mail.port=${SPRING_MAIL_PORT:993}
spring.mail.username=${SPRING_MAIL_USERNAME:}
spring.mail.password=${SPRING_MAIL_PASSWORD:}
spring.mail.properties.mail.store.protocol=${SPRING_MAIL_PROTOCOL:imaps}

# Logging
logging.level.org.springframework.security=DEBUG
```

---

## ⚡ Ejecución Local

### Prerrequisitos
* **JDK 17**
* **Maven 3.8+** (o usar el wrapper ejecutable `./mvnw`)
* Instancia local de **PostgreSQL** o base de datos en **Supabase**

### Pasos

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/jordantejadadev/supportdesk-backend.git
   cd supportdesk-backend
   ```

2. **Compilar y construir el proyecto:**
   ```bash
   ./mvnw clean package -DskipTests
   ```

3. **Ejecutar la aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```

La API estará corriendo localmente en `http://localhost:8080`.

---

## 📖 Documentación Swagger / OpenAPI

Puedes consultar y probar todos los endpoints REST directamente desde la interfaz gráfica de Swagger UI visitando:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🐳 Despliegue con Docker (Render)

El proyecto utiliza una estrategia de compilación en dos etapas (*multi-stage build*) para optimizar el tamaño de la imagen final:

```dockerfile
# Etapa 1: Compilar la aplicación
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecutar la aplicación
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/target/ticket-system-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 👨‍💻 Autor

Desarrollado por **Jordan Tejada**

* **GitHub:** [@jordantejadadev](https://github.com/jordantejadadev)
* **Demo del Proyecto:** [supportdesk-frontend-nine.vercel.app](https://supportdesk-frontend-nine.vercel.app/)