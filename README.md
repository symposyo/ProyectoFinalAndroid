# TaskSyncFinal 🚀 - Gestión de Tareas con Arquitectura Moderna

¡Bienvenido a **TaskSyncFinal**! Este proyecto es una aplicación Android robusta diseñada para demostrar la integración de múltiples capas de datos, desde la persistencia local hasta la sincronización con APIs remotas. Fue desarrollado como el proyecto final del curso para mostrar las mejores prácticas en el desarrollo móvil actual.

## 📝 Descripción

TaskSyncFinal es una solución completa de gestión de tareas que implementa un patrón **Offline-First**. El usuario puede autenticarse, gestionar tareas de forma local y sincronizar su contenido con un servidor remoto. El proyecto destaca por su resiliencia y manejo de estados de red.

---

## ✨ Características Principales

- **🔐 Autenticación Segura:** Sistema de login con persistencia de sesión mediante Tokens.
- **💾 Persistencia con Room:** Almacenamiento local en SQLite para funcionamiento sin conexión.
- **🔄 Sincronización API:** Consumo de servicios REST para mantener los datos actualizados.
- **🎨 Material Design 3:** Interfaz moderna, limpia y adaptativa.
- **⚡ Arquitectura Asíncrona:** Uso intensivo de Kotlin Coroutines para una experiencia fluida.

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** [Kotlin](https://kotlinlang.org/)
- **Persistencia Local:** [Room Database](https://developer.android.com/training/data-storage/room)
- **Networking:** [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/)
- **UI:** [ViewBinding](https://developer.android.com/topic/libraries/view-binding) + [ConstraintLayout](https://developer.android.com/training/constraint-layout)
- **Asincronía:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- **JSON Parsing:** [GSON](https://github.com/google/gson)
- **Imágenes:** [Glide](https://github.com/bumptech/glide)

---

## 🏗️ Arquitectura: El Enfoque Offline-First

El corazón del proyecto es su capacidad de funcionar sin internet. 
1. **Room como Fuente de Verdad:** La UI nunca lee directamente de la API. Siempre lee de la base de datos local.
2. **Sincronización Inteligente:** Cuando el usuario pulsa "Sincronizar", los datos bajan de la API, se guardan en Room y la UI se actualiza automáticamente. Esto garantiza que el usuario siempre vea sus datos, incluso si el servidor falla.

---

## 💡 Desafíos Superados: El caso "403 Forbidden"

Durante el desarrollo inicial con la API *ReqRes*, nos enfrentamos a bloqueos de seguridad de Cloudflare (Error 403). 
**Solución aplicada:** Migramos la lógica de red a **DummyJSON**. Este cambio implicó:
- Re-mapeo de modelos de datos usando `@SerializedName`.
- Ajuste del sistema de autenticación para manejar `username` en lugar de `email`.
- Mejora de los interceptores de red para depuración en tiempo real.

---

## 🚀 Cómo Ejecutar el Proyecto

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/TaskSyncFinal.git
   ```
2. **Abrir en Android Studio:** Importa el proyecto y deja que Gradle descargue las dependencias.
3. **Credenciales de Prueba:**
   - **Usuario:** `emilys`
   - **Contraseña:** `emilyspass`

---

## 📸 Capturas de Pantalla

| Login | Lista de Tareas | Sincronización |
|-------|-----------------|----------------|
| ![Login](https://via.placeholder.com/200x400?text=Login+Screen) | ![Tasks](https://via.placeholder.com/200x400?text=Task+List) | ![Sync](https://via.placeholder.com/200x400?text=Sync+Action) |

---

## 🎓 Conclusión
Este proyecto resume los conocimientos adquiridos en:
- Consumo de APIs RESTful.
- Bases de Datos Relacionales en dispositivos móviles.
- Manejo de hilos y concurrencia.
- UX/UI limpia y orientada al usuario.

---
Creado por **Jim Guillen** - 2026
