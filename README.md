# Arquitectura y Entorno de Desarrollo - Liora

Bienvenido al repositorio de la aplicación móvil de Liora. Este documento establece las bases tecnológicas, la arquitectura del proyecto y las reglas de diseño para mantener un código limpio, escalable y organizado.

## Stack Tecnológico y Ubicación en el Proyecto

A continuación se detalla la tecnología elegida, su propósito y dónde encontrarla o aplicarla dentro de nuestro código fuente.

| Tecnología | Uso en el proyecto | Dónde vive / Cómo se usa en nuestro código |
| :--- | :--- | :--- |
| **Android Studio** | Entorno de desarrollo principal para crear, programar, probar y ejecutar la aplicación móvil[cite: 5]. | Es la herramienta base. Requiere usar el emulador (API 37+) para probar los cambios locales. |
| **Kotlin** | Lenguaje de programación utilizado para desarrollar la lógica y funcionamiento de la aplicación[cite: 5]. | Todo el código fuente se ubica dentro de la ruta `app/src/main/java/com/.../liora/`. |
| **Jetpack Compose** | Framework de Android utilizado para construir las interfaces y pantallas de la aplicación[cite: 5]. | Reemplaza a los antiguos XML. Toda la interfaz se construye mediante funciones anotadas con `@Composable`. |
| **Material 3** | Se utilizará para diseñar los componentes visuales, como botones, tarjetas, menús, colores y elementos de la interfaz[cite: 5]. | Centralizado en la carpeta `ui/theme/` (archivos `Color.kt`, `Theme.kt`, `Type.kt`). Nunca usar colores "quemados" (ej. `Color.Red`); usar siempre `MaterialTheme.colorScheme`. |
| **Navigation Compose** | Permitirá controlar la navegación entre las diferentes pantallas de la aplicación, como Inicio, Cursos, Tienda, Pedidos y Perfil[cite: 5]. | La dependencia se gestiona en `gradle/libs.versions.toml`. El enrutador central se configurará próximamente en un archivo dedicado (ej. `AppNavigation.kt`). |
| **API / Backend** | Permitirá gestionar la comunicación y las funciones necesarias entre la aplicación y los servicios del sistema[cite: 5]. | **No vive en esta app.** Será un servicio externo en la nube. La app consumirá las rutas HTTP (REST) usando librerías de red más adelante. |
| **MySQL** | Base de datos externa donde se almacenará información de usuarios, cursos, productos, pedidos y demás datos de la aplicación[cite: 5]. | **Tampoco vive en esta app.** Se alojará en **Aiven** como un servicio administrado en la nube. La aplicación Android no tendrá credenciales de base de datos directas por seguridad. |

## Estructura de Carpetas (Evitando el Código Espagueti)

Para mantener el proyecto organizado, **no** crearemos todas las funciones en el mismo archivo. Toda nueva interfaz debe respetar la siguiente estructura dentro de `app/src/main/java/com/.../liora/`:

*   📁 **`ui/screens/`**: Aquí van las pantallas completas (ej. `HomeScreen.kt`, `StoreScreen.kt`, `CourseDetailScreen.kt`). Una pantalla ensambla varios componentes menores.
*   📁 **`ui/components/`**: Aquí van los elementos reutilizables. Si vas a crear una **tarjeta de producto**, un **botón personalizado** o una **barra de búsqueda**, créalo aquí (ej. `ProductCard.kt`). Así, cualquier desarrollador puede importar tu componente en lugar de reescribir el código.
*   📁 **`ui/theme/`**: Reservado exclusivamente para la configuración de Material 3 (paleta de contrastes: Azul Marino, Morado, Teal, Aqua y Lila).

## Guía Práctica: ¿Cómo crear una nueva pantalla o componente?

1.  **Identifica el alcance:** ¿Es una pantalla completa (ej. Perfil) o una pieza reutilizable (ej. un botón de "Comprar")?
2.  **Crea el archivo:** Ve a `ui/screens/` o `ui/components/` y crea un archivo Kotlin.
3.  **Usa Jetpack Compose:**
    ```kotlin
    @Composable
    fun ProductCard(productName: String) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary // Usar siempre colores del tema
            )
        ) {
            Text(text = productName, color = MaterialTheme.colorScheme.onSecondary)
        }
    }
    ```
4.  **Integra:** Llama a tu nuevo componente desde la pantalla principal que le corresponda.

## Aclaración sobre Base de Datos y Backend

Es fundamental comprender que **esta aplicación móvil (frontend) es completamente independiente de la base de datos**. 
Nuestra base de datos relacional **MySQL** estará alojada en **Aiven**. Por razones de seguridad, Android Studio jamás ejecutará consultas SQL directas (`SELECT * FROM...`). En su lugar, el equipo de backend desarrollará una API que actuará como puente. Esta aplicación Android simplemente enviará peticiones web a la API para solicitar o enviar los datos del negocio.
