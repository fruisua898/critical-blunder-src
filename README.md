# 🎲 Critical Blunder

Aplicación web para la gestión de partidas de rol, pensada para facilitar la labor del máster.  
Proyecto desarrollado para el Grado Superior en Desarrollo de Aplicaciones Web, con stack completo.

## 🧰 Tecnologías utilizadas

### 🖥️ Back-end (Java)
- Java 17
- Spring Boot 3.3.4
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- MySQL 8
- JWT (Json Web Tokens) – `jjwt`
- MapStruct
- Lombok
- OpenAPI / Swagger UI – `springdoc-openapi`
- Jacoco (coverage)
- Maven

### 🌐 Front-end (JavaScript)
- React 19
- React Router DOM 7
- Vite 6
- Sass (SCSS)
- ESLint
- React Icons
- PropTypes

## ⚙️ Requisitos previos

### Back-end
- Tener instalado:
  - Java 17
  - Maven
  - MySQL 8
- Crear la base de datos ejecutando el script SQL que se encuentra en el directorio `/sql`.
- Abrir el proyecto backend en un IDE compatible con Maven (por ejemplo IntelliJ IDEA o Eclipse).
- Desde el directorio raíz (donde está el `pom.xml`), ejecutar:
  ```bash
  mvn clean install

## 🚜 Cómo ejecutar el proyecto

1. Abrir el proyecto en un IDE compatible con Maven (por ejemplo IntelliJ o Eclipse).
2. Verificar que los valores del CORS de `application.properties` sean donde se va a exponer el frontend:
    - cors.allowed-origin=${CORS_ALLOWED_ORIGIN:http://localhost:5173} 
    - cors.allowed-origin-list=${CORS_ALLOWED_ORIGIN_LIST:http://localhost,http://localhost:3000,http://localhost:5173}
3. Desde la raíz del proyecto backend (donde está el `pom.xml`), ejecutar:
    - mvn clean install
4. Ejecutar como aplicación Spring Boot (`Run as → Spring Boot App` en el IDE).

### 🌼 Front-end

1. Ir al directorio del frontend.
2. Revisar el archivo `.env` con el siguiente contenido:
    - VITE_BASE_URL=http://localhost:8080/api 
    - Cambiar por el dominio donde tengamos el back seguido de /api
3. Instalar dependencias:
     ```bash
    - npm install
5. Ejecutar el servidor de desarrollo:
     ```bash
    - npm run dev -- --host
6. Acceder desde el navegador a `http://localhost:5173` o la URL que nos devuelva la consola.

```
## 📁 Estructura del proyecto

critical-blunder/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│
│
└── frontend/
│       ├── src/
│       ├── public/
│       ├── package.json
│       └── vite.config.js
│   
└── sql/
    └── init.sql
```

## Notas

- El frontend y el backend están desacoplados.
- Swagger está disponible en `/swagger-ui.html` una vez desplegado el backend.
- Asegúrate de que los puertos `8080` (backend) y `5173` (frontend) estén libres.

Made with 🧠 y ☕️ by Francisco.
