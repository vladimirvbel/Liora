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


---
Aclarando tu duda principal antes de pasar al código: **Un Pull Request (PR) y comandos como `git fetch` o `git pull` hacen cosas diferentes pero complementarias.**

* El **Pull Request** se hace en la página de GitHub; es una solicitud formal para *unir* tu rama con `develop`.
* Comandos como **`git fetch`** (que descarga la información de qué cambió) o **`git pull`** (que descarga y aplica los cambios) se usan en tu terminal local para *traer* a tu computadora ese código que ya fue unido en GitHub por tus compañeros.



---

## 🛠️ Diccionario de Comandos Git Útiles

Para moverte con fluidez en la terminal, aquí tienes los comandos del día a día y cómo interpretar lo que te responde Git.

### 1. Saber dónde estás y qué has modificado
```bash
git status

```

**¿Para qué sirve?** Es tu radar. Te dice en qué rama estás parado actualmente y qué archivos has modificado, agregado o eliminado.

* **Texto en rojo:** Archivos modificados que Git ha notado, pero que aún no están listos para guardarse (necesitan un `git add .`).
* **Texto en verde:** Archivos que ya están en el "Staging Area", listos para que les hagas un `git commit`.
* **"Nothing to commit, working tree clean":** Tu entorno está limpio y no hay cambios pendientes.

### 2. Ver tus ramas locales y remotas

```bash
git branch -a

```

**¿Para qué sirve?** Muestra una lista de todas las ramas que existen en el proyecto. El parámetro `-a` (all) incluye tanto las que viven en tu PC como las que están en GitHub.

* **Asterisco verde (`* develop`):** El asterisco y el color verde indican la rama en la que estás trabajando exactamente en este momento.
* **Texto blanco/normal:** Son otras ramas que tienes descargadas en tu computadora (locales).
* **Texto rojo (`remotes/origin/feature/...`):** Son las ramas que viven en el servidor de GitHub (remotas). Si ves una rama aquí que no tienes en blanco, significa que un compañero la subió, pero tú aún no la has descargado.

Para ver **solo** tus ramas locales y la actual, usa simplemente:

```bash
git branch

```

### 3. Traer los cambios de tus compañeros (Actualizar tu PC)

```bash
git pull origin develop

```

**¿Para qué sirve?** Descarga el código más reciente que está en GitHub (`origin develop`) y lo fusiona inmediatamente con la rama en la que estás parado.

* *Nota:* Usa esto cuando sepas que un compañero ya aprobó un Pull Request y quieres tener su código en tu máquina para no quedarte desactualizado.

*(Si solo quieres ver si hay cambios nuevos en el servidor sin aplicarlos a tu código todavía, puedes usar `git fetch`).*

### 4. Ver el historial de cambios (Quién hizo qué)

```bash
git log --oneline

```

**¿Para qué sirve?** Te muestra una lista resumida de todos los commits que se han hecho, con su código identificador (hash) y el mensaje. Es útil para confirmar si tu commit se guardó correctamente o para ver en qué punto de retorno te encuentras. Presiona la letra `q` en tu teclado para salir de esta vista.

### 5. Deshacer errores antes de guardar

```bash
git restore .

```

**¿Para qué sirve?** Si modificaste varios archivos intentando hacer algo, rompiste el código y quieres que todo vuelva a estar exactamente como estaba en tu último commit (borrando tus cambios no guardados), este comando es tu botón de pánico. **Úsalo con precaución**, ya que los cambios borrados con este comando no se pueden recuperar.
