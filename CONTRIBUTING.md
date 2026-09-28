# Guía de Contribución y Flujo de Trabajo (Gitflow)

Bienvenido al equipo de desarrollo de **Liora**. Para mantener un historial limpio, evitar conflictos en el código y trabajar de manera eficiente, seguimos un modelo basado en ramas (Gitflow). Todo el código nuevo se integra a `develop` mediante Pull Requests.

---

## 1. Configuración Inicial (Solo la primera vez)

Para iniciar el proyecto en tu máquina local, clona el repositorio:

```bash
git clone [https://github.com/vladimirvbel/Liora.git](https://github.com/vladimirvbel/Liora.git)

```

Abre la carpeta `Liora` desde **Android Studio**. No necesitas ejecutar comandos manuales de instalación; **Gradle** leerá la configuración y descargará automáticamente todas las dependencias en segundo plano. Espera a que la barra de carga inferior termine.

## 2. Mantenerse Actualizado

Antes de iniciar a programar cualquier tarea nueva, debes asegurarte de tener los últimos cambios del equipo. Nuestra rama principal de trabajo es `develop`.

```bash
git checkout develop
git pull origin develop

```

## 3. Crear tu Rama de Trabajo

**Nunca trabajes ni hagas commits directamente en `main` o `develop`.** Crea una nueva rama a partir de `develop` usando la nomenclatura estándar:

* `feature/` o `f/`: Para nuevas características o pantallas (ej. `feature/catalogo-prendas`).
* `fix/`: Para corrección de errores (ej. `fix/crash-navegacion`).

```bash
git checkout -b feature/nombre-de-la-tarea

```

## 4. Escribir Código y Commits

A medida que avances, guarda tu progreso con mensajes de commit descriptivos. Utilizamos prefijos para identificar rápidamente el tipo de cambio:

* `feat:` Nueva funcionalidad o característica.
* `fix:` Corrección de un error.
* `ui:` Cambios exclusivos de interfaz, Material 3 o animaciones.
* `docs:` Modificaciones en documentación o README.

```bash
git add .
git commit -m "feat: agregar estructura de navegación principal con Compose"

```

## 5. Subir Cambios y Crear Pull Request (PR)

Cuando tu tarea esté terminada y probada en el emulador, súbela a GitHub:

```bash
git push -u origin feature/nombre-de-la-tarea

```

1. Ve a la página del repositorio en GitHub.
2. Haz clic en el botón verde **Compare & pull request**.
3. Asegúrate de configurar las ramas así: `base: develop`  <-  `compare: feature/tu-rama`.
4. Añade una descripción de lo que hiciste y solicita la revisión (Review) de un compañero antes de fusionar.

## 6. Mergear y Limpieza de Ramas

Una vez que tu PR es aprobado y fusionado a `develop` en GitHub, tu rama de trabajo ya no es útil. Es importante borrarla para mantener limpio el entorno.

1. Regresa a la rama principal e integra los cambios:
```bash
git checkout develop
git pull origin develop

```


2. Borra tu rama de trabajo localmente:
```bash
git branch -d feature/nombre-de-la-tarea

```


3. (Opcional) Si GitHub no eliminó la rama remota automáticamente al hacer el merge, bórrala desde la terminal:
```bash
git push origin --delete feature/nombre-de-la-tarea

```



---

## 💡 Buenas Prácticas Adicionales

* **Sincroniza frecuentemente:** Si tu tarea toma varios días, ejecuta `git pull origin develop` dentro de tu rama periódicamente. Esto te ayudará a resolver conflictos pequeños a tiempo en lugar de enfrentar un conflicto masivo al final.
* **Prueba antes de subir:** Nunca abras un PR si el código no compila o si la aplicación colapsa al abrirse en el emulador.
* **Archivos sensibles:** El `.gitignore` ya está configurado para evitar subir archivos pesados o locales. No fuerces la subida de archivos generados como `local.properties` o la carpeta `build/`.
