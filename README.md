# Agroprecios

Aplicacion web para consultar subastas agricolas y sus precios historicos.

Este repositorio incluye:

- Frontend en Angular, compilado y servido en Nginx dentro de Docker.
- Backend en Spring Boot con API REST.
- Base de datos PostgreSQL.

La carpeta scripts contiene utilidades de scraping y no forma parte del despliegue principal de la app.

## Arquitectura

| Capa | Tecnologia | Puerto local | Funcion |
| --- | --- | --- | --- |
| Frontend | Angular + Nginx | 4200 | Interfaz web |
| Backend | Spring Boot | 8080 | API REST bajo /api |
| Base de datos | PostgreSQL 16 | 5432 | Persistencia |

## Requisitos

### Para Docker

- Docker Desktop con Compose habilitado.

### Para ejecucion local sin Docker

- Node.js 22+
- npm
- Java 17
- Maven Wrapper incluido
- PostgreSQL 16+

## Ejecucion rapida con Docker Compose

Desde la raiz del proyecto:

    docker compose up -d --build

Servicios disponibles:

- Frontend: http://localhost:4200
- Backend: http://localhost:8080/api
- PostgreSQL: localhost:5432

Parar servicios:

    docker compose down

Parar y eliminar tambien el volumen de datos:

    docker compose down -v

Ver estado:

    docker compose ps

Ver logs en vivo:

    docker compose logs -f

## Despliegue en Docker (detalle)

### Frontend

- Build de Angular en una etapa Node.
- Publicacion de estaticos en Nginx.
- Soporte de rutas SPA con fallback a index.html.

### Backend

- Build con Maven en etapa de compilacion.
- Imagen final con JRE 17 para ejecucion.
- Conexion a PostgreSQL mediante variables de entorno.

### Base de datos

- Imagen oficial postgres:16-alpine.
- Volumen persistente llamado db_data.
- Healthcheck para asegurar disponibilidad antes de levantar backend.

## Variables de entorno relevantes

### Backend

- DB_URL
- DB_USERNAME
- DB_PASSWORD

Valores por defecto en Compose:

- DB_URL=jdbc:postgresql://db:5432/agroprecios
- DB_USERNAME=postgres
- DB_PASSWORD=postgres

## Ejecucion local sin Docker

## 1) Base de datos PostgreSQL

Crear una base de datos llamada agroprecios y configurar usuario/clave segun tus variables.

## 2) Backend

Entrar en la carpeta backend y arrancar:

    ./mvnw spring-boot:run

API disponible en:

- http://localhost:8080/api/subastas
- http://localhost:8080/api/familias
- http://localhost:8080/api/productos
- http://localhost:8080/api/preciosubasta?subasta_id=1

## 3) Frontend

Entrar en la carpeta frontend e instalar dependencias:

    npm install

Modo desarrollo:

    npm start

Build produccion:

    npm run build

## Inicializacion de datos

El backend carga datos semilla automaticamente cuando detecta tablas vacias.

Archivo de seed:

- backend/src/main/resources/seed/db.json

## Estructura principal

- backend
- frontend
- docker-compose.yml

## Problemas comunes

### El frontend no construye en Docker

Si aparece un error por desajuste entre package.json y package-lock.json, regenera el lockfile:

    cd frontend
    npm install

Luego reconstruye:

    cd ..
    docker compose up -d --build

### El backend no conecta a la base

Revisa:

- Que db este healthy: docker compose ps
- Que DB_URL use el host db dentro de Docker
- Que usuario y clave coincidan
