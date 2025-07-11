# 🧩 Task Management API - Desafio Tecnico Spring Boot 

Proyecto de API RESTful para la gestión de tareas, desarrollado como parte de un desafío técnico solicitado por HF Solutions. 
Permite autenticar usuarios y realizar operaciones CRUD sobre tareas, con control de acceso por roles (`ADMIN`, `USER`) mediante JWT. 
La API está documentada con OpenAPI/Swagger.

---

## ✅ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.4.4**
- **Maven**
- **Spring Security con JWT**
- **H2 Database**
- **Spring Data JPA**
- **OpenAPI 3 / Swagger**
- **JUnit 5 + MockMvc**

---

## ⚙️ Requisitos del sistema

- Java 17+
- Maven 3.8+

---

## 🚀 Cómo ejecutar la aplicación

1. Clonar el repositorio:  
   `git clone https://github.com/alvarovergaracortes/desafio-spring-boot.git`  
   `cd desafio-spring-boot`

2. Ejecutar la aplicación:
   `mvn spring-boot:run`

3. Acceder a la documentación Swagger:
   `http://localhost:8082/swagger-ui/index.html`

4. Acceder a la consola H2:
   `http://localhost:8082/h2-console`

   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **User**: `sa`
   - **Password**: (vacío)

---

## 🧪 Usuarios Pre-cargados
```plaintext
| Usuario  | Contraseña | Rol    |  
|----------|------------|--------|  
| `admin`  | `admin123` | `ADMIN`|  
| `user`   | `user123`  | `USER` |  
```

> *Contraseñas encriptadas con BCrypt (ver clase: cl.previred.common.helper.CreateEncryptedPassword)*

---

## 🔐 Autenticación

1. Inicia sesión con credenciales válidas y retorna un token JWT.  

Request Body:  

```
{
  "username": "admin",
  "password": "admin123",
  "roles": "ADMIN"
}
```
Response (200):

```
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI..."
}
```


2. Luego de obtener el Token usarlo en el encabezado `Authorization`:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
```

---
##🛡️ Seguridad
* Se utiliza JWT en el encabezado Authorization: Bearer <token>.  
* Los endpoints de tareas requieren estar autenticado.  
* Solo los usuarios con rol ADMIN pueden crear, actualizar y eliminar tareas.  
* Los usuarios con rol USER solo pueden ver tareas.

---

## 📌 Endpoints principales

### Usuarios
```plaintext
| Método | Endpoint       | Descripción               | Acceso  |
|--------|----------------|---------------------------|---------|
| POST   | /user/login    | Login y obtención de token| Público |
```

### Tareas

```plaintext
| Método | Endpoint               | Descripción                      | Rol requerido |
|--------|------------------------|----------------------------------|---------------|
| GET    | /tasks                 | Listar todas las tareas          | USER, ADMIN   |
| GET    | /tasks/{id}            | Obtener tarea por ID             | USER, ADMIN   |
| GET    | /tasks/users/{userId}  | Obtener tareas por ID de usuario | USER, ADMIN   |
| POST   | /tasks                 | Crear nueva tarea                | ADMIN         |
| PUT    | /tasks/{id}            | Actualizar tarea existente       | ADMIN         |
| DELETE | /tasks/{id}            | Eliminar tarea por ID            | ADMIN         |
```

---
### 📝 Ejemplo de Creación de Tarea
Request Body:  

```
{
  "title": "Completar desafio",
  "description": "Implementar la API con seguridad y validaciones",
  "stateId": 1,
  "userId": 1
}
```
Response - Genera el id de la tarea (200):

```
{
  "id": 1,
  "title": "Completar desafío",
  "description": "Implementar la API con seguridad y validaciones",
  "creationDate": "2025-07-11T23:00:00",
  "stateId": 1,
  "userId": 1
}
```
---

## 📄 Validaciones

- `title`: no debe estar vacío
- `description`: no debe estar vacío
- `userId` y `stateId`: deben existir previamente
- JWT obligatorio en endpoints protegidos
- Solo `ADMIN` puede crear/modificar/eliminar tareas

---

## 🧪 Pruebas Automatizadas

- Pruebas de integración con `MockMvc` para todos los endpoints
- Casos cubiertos:
  - Éxito y error en login
  - CRUD completo de tareas
  - Validaciones y errores de seguridad

Ejecutar pruebas:

```bash
mvn test
```

---

## 📂 Estructura del Proyecto

```
src
├── main
│   ├── java/cl/previred
│   │   ├── common        → Seguridad y manejo global
│   │   ├── user          → Login y usuarios
│   │   ├── task          → Lógica de tareas
│   │   └── taskstates    → Estados predefinidos
│   └── resources
│       ├── data.sql               → Datos precargados
│       ├── application.properties → Configuración general
│       ├── schema.sql             → Script de creación de tablas (DDL)
│       └── data.sql               → Datos precargados (DML)
├── test  → Pruebas de integración
```

---

## 📦 Generación del .jar

```bash
mvn clean package
java -jar target/desafio-spring-boot-0.0.1-SNAPSHOT.jar
```

---

## 📁 Documentacion de apoyo

- 📂 `docs/` → Diagramas, instrucciones y ejemplos
- 📄 `desafio-spring-boot.postman_collection.json` → Ejemplos de consumo

---

## 🙋 Autor

**Álvaro Vergara Cortés**  
Correo: alvaro.vergara@ejemplo.com  
