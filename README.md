# Proyecto didactico: Spring Boot + Amazon S3

Este proyecto esta pensado para alumnos que estan aprendiendo Cloud Native y necesitan entender, de forma practica, como conectar una API Spring Boot con un bucket S3 de AWS.

La aplicacion expone endpoints REST para:

- Subir archivos a S3.
- Listar archivos almacenados.
- Descargar archivos por nombre.

## Objetivo de aprendizaje

Al trabajar con este repositorio deberias poder:

1. Configurar credenciales AWS en una aplicacion Spring Boot.
2. Entender el uso de `S3Client` (AWS SDK v2).
3. Consumir endpoints multipart para subir archivos.
4. Validar resultados en el bucket S3.
5. Desplegar la aplicacion con Docker y variables de entorno.

## Como funciona el proyecto

### Flujo general

1. El cliente (Postman, curl o frontend) envia una solicitud HTTP a la API.
2. El controlador recibe la solicitud y delega en el servicio.
3. El servicio construye la operacion de S3 (put, list o get).
4. AWS S3 responde y la API devuelve un resultado al cliente.

### Estructura principal

- `ArchivoController`: define los endpoints REST.
- `S3Service`: contiene la logica de subida, listado y descarga.
- `S3Config`: crea el bean `S3Client` con region y credenciales AWS.
- `application.properties`: centraliza configuracion por variables de entorno.

### Prefijo de almacenamiento

Los archivos se guardan con el prefijo `uploads/` dentro del bucket.
Ejemplo: si subes `documento.pdf`, se almacena como `uploads/documento.pdf`.

## Requisitos previos

- Java 17+
- Maven 3.6+
- Cuenta AWS con un bucket S3 creado
- Credenciales con permisos sobre S3 (`s3:PutObject`, `s3:GetObject`, `s3:ListBucket`)

## Configuracion

La app usa variables de entorno. En `src/main/resources/application.properties` se leen asi:

- `AWS_REGION`
- `AWS_S3_BUCKET`
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_SESSION_TOKEN` (si usas credenciales temporales)
- `MAX_FILE_SIZE` y `MAX_REQUEST_SIZE` para limite de carga

Importante:

- El bucket debe declararse solo con nombre, sin `s3://`.
- Ejemplo correcto: `cloud-native-ggrobier-semana2-prueba1`.

## Ejecutar localmente

Define variables en tu terminal y luego ejecuta:

```bash
mvn clean spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8080
```

## Endpoints de la API

### 1) Subir archivo

- Metodo: `POST`
- URL: `/api/files/upload`
- Tipo: `multipart/form-data`
- Campo: `file`

Ejemplo con curl:

```bash
curl -F "file=@/ruta/mi-archivo.pdf" http://localhost:8080/api/files/upload
```

### 2) Listar archivos

- Metodo: `GET`
- URL: `/api/files/list`

Ejemplo:

```bash
curl http://localhost:8080/api/files/list
```

### 3) Descargar archivo

- Metodo: `GET`
- URL: `/api/files/download/{name}`

Ejemplo:

```bash
curl -O http://localhost:8080/api/files/download/mi-archivo.pdf
```

## Prueba en Postman

Para subir archivos correctamente:

1. Selecciona metodo `POST`.
2. Usa URL `http://localhost:8080/api/files/upload`.
3. Ve a Body -> form-data.
4. Crea key `file` y tipo `File`.
5. Adjunta archivo y envia.

Errores frecuentes:

- `getaddrinfo ENOTFOUND http`: URL mal escrita (por ejemplo, doble `http://` o variable mal configurada).
- `413 Request Entity Too Large`: archivo supera el limite de `multipart`.

## Limites de carga

El proyecto permite configurar limite de subida con:

- `MAX_FILE_SIZE` (por defecto `50MB`)
- `MAX_REQUEST_SIZE` (por defecto `50MB`)

Ejemplo:

```text
MAX_FILE_SIZE=100MB
MAX_REQUEST_SIZE=100MB
```

## Docker y despliegue

El proyecto incluye:

- `Dockerfile` para construir la imagen Spring Boot.
- `docker-compose.yml` para ejecutar el contenedor con variables AWS.
- Workflow de GitHub Actions para build/push y despliegue en EC2.

En CI/CD debes definir secretos como:

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_SESSION_TOKEN` (si aplica)
- `AWS_REGION`
- `AWS_S3_BUCKET`
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
- `EC2_HOST`, `EC2_USER`, `EC2_SSH_KEY`

## Buenas practicas para alumnos

1. No subas credenciales reales al repositorio.
2. Usa variables de entorno o secretos del pipeline.
3. Prueba primero local y luego despliega.
4. Revisa en AWS S3 que el objeto realmente se haya creado.
5. Diferencia entre errores de cliente (Postman/URL) y errores de servidor (Spring/AWS).

## Siguiente ejercicio sugerido

Como extension didactica, puedes implementar:

1. Borrado de archivos (`DELETE /api/files/{name}`).
2. Validacion de tipo de archivo (por ejemplo, solo PDF/JPG).
3. Control de tamano por endpoint.
4. Uso de URL prefirmadas (pre-signed URLs) para descargas temporales.