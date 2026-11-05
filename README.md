# 🔧 Fixly — Aplicación Android de Servicios del Hogar

Fixly es una aplicación móvil Android que conecta a **clientes** con **proveedores de servicios del hogar** (plomería, electricidad, jardinería, cerrajería, pintura, mecánica, limpieza, instalaciones, etc.). Cuenta con un panel de **administración** para gestionar usuarios desde la misma app.

---

## ✨ Funcionalidades principales

### 👤 Cliente
- Registro e inicio de sesión
- Explorar categorías y servicios disponibles
- Ver perfil de proveedores con calificaciones y comentarios
- Solicitar servicios a proveedores
- Historial de solicitudes (editar, cancelar)
- Calificar servicio una vez finalizado
- Editar perfil y cambiar contraseña

### 🛠️ Proveedor (Prestador)
- Registro e inicio de sesión
- Ver y gestionar solicitudes recibidas (aceptar, finalizar)
- Ver su propio perfil
- Editar perfil y cambiar contraseña

### 🔐 Administrador
- Ver listado de todos los usuarios
- Buscar usuarios
- Activar / desactivar cuentas de usuario

---

## 🏗️ Arquitectura y tecnologías

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java |
| UI | XML Layouts + Material Design |
| Networking | Retrofit 2 + Gson |
| Carga de imágenes | Glide 4.16 |
| Sesión | SharedPreferences |
| Backend | API REST (Node.js — repositorio separado) |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 36 |

### Estructura de paquetes

```
com.example.fixly/
├── adapters/           # RecyclerView adapters
├── models/             # DTOs de request/response por módulo
│   ├── login/
│   ├── registroCliente/
│   ├── registroProveedor/
│   ├── historial/
│   ├── proveedores/
│   ├── calificar/
│   ├── catalogos/
│   ├── administrador/
│   └── ...
├── network/
│   ├── ApiService.java      # Endpoints Retrofit
│   └── RetrofitClient.java  # Singleton Retrofit
└── *Activity.java           # Pantallas de la app
```

---

## 🚀 Cómo ejecutar el proyecto

### Prerrequisitos
- Android Studio Hedgehog o superior
- JDK 11+
- Dispositivo / emulador con Android 7.0+ (API 24)
- Backend de Fixly corriendo localmente (ver sección Backend)

### Pasos

1. **Clona el repositorio**

2. **Abre el proyecto en Android Studio**

3. **Configura la URL del backend**

   Edita el archivo [`RetrofitClient.java`](app/src/main/java/com/example/fixly/network/RetrofitClient.java) y reemplaza la IP con la de tu máquina en la red local:
   ```java
   private static final String BASE_URL = "http://<TU_IP_LOCAL>:3000/api/";
   ```
   > ⚠️ Si usas un emulador de Android Studio, la IP de tu máquina host es `10.0.2.2`.

4. **Sincroniza Gradle** y ejecuta la app (`▶ Run`)

---

## 🔗 Backend

Esta app consume una API REST desarrollada en **Node.js**. Asegúrate de tener el backend corriendo antes de usar la aplicación.

> 📌 Repositorio del backend: https://github.com/Deni-Arellano/Fixly-back

### Endpoints principales consumidos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `auth/login` | Inicio de sesión |
| POST | `users/registrarCliente` | Registro de cliente |
| POST | `users/registrarPrestador` | Registro de proveedor |
| GET | `users/verPerfil` | Ver perfil propio |
| PUT | `users/editarPerfil` | Editar perfil (con/sin foto) |
| GET | `catalogs/categorias` | Listar categorías |
| GET | `providers/categoria/{id}` | Proveedores por categoría |
| POST | `service-requests/solicitarServicio` | Crear solicitud |
| GET | `service-requests/verSolicitudes` | Historial de solicitudes |
| PUT | `service-requests/{id}/cancelar` | Cancelar solicitud |
| PUT | `service-requests/{id}/aceptar` | Aceptar solicitud (proveedor) |
| PUT | `service-requests/{id}/finalizar` | Finalizar solicitud |
| POST | `reviews/{id}/calificar` | Calificar servicio |
| GET | `admin/usuarios` | Listar usuarios (admin) |
| PUT | `admin/usuarios/{id}/estado` | Activar/desactivar usuario |

---

## 📦 Dependencias principales

```toml
# gradle/libs.versions.toml
retrofit       = "2.9.0"
appcompat      = "1.6.1"
material       = "1.10.0"
constraintlayout = "2.1.4"
glide          = "4.16.0"
```

