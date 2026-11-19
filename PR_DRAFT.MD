## 1. Actualizar `develop` y crear tu rama local
Antes de empezar cualquier tarea, asegúrate de partir de la versión más reciente de `develop`:

```bash
git checkout develop
git pull origin develop
git checkout -b feature/nombre-de-tu-tarea
```

---

## 2. Commit inicial para habilitar el PR
* **Si aún no tienes cambios ni un commit inicial** en esta rama, crea un commit vacío únicamente para poder subir la rama y abrir el Draft PR desde el inicio:
  ```bash
  git commit --allow-empty -m "chore: iniciar desarrollo de tarea"
  git push -u origin feature/nombre-de-tu-tarea
  ```
* **Si ya avanzaste con código local**, omite el commit vacío y sube tu primer avance de forma normal (`git add .`, `git commit -m "..."` y `git push -u origin feature/nombre-de-tu-tarea`).

---

## 3. Crear el Draft Pull Request en GitHub
1. Abre un nuevo Pull Request en GitHub asegurándote de que apunte a la rama **`develop`** (nunca directo a `main`).
2. En la descripción del PR, agrega la palabra clave de cierre para vincularlo automáticamente con tu issue:
   * Si el issue está en el **mismo repositorio**
```text
Closes #6 (ID Issue)
```
 * Si el issue está en **otro repositorio** (por ejemplo, el tablero central):
```text
Closes usuario/repositorio#6 = Closes vladimirbvel/Liora#6
```
3. En el botón verde de creación, despliega la flecha y selecciona **"Create draft pull request"**.

---
```bash
git add .
git commit -m "feat: describir avance realizado"
git push
```

---
## 5. Finalizar tarea y pasar a revisión (PR normal)
Cuando termines todo el desarrollo y verifiques que cumple los criterios del issue:
1. Sube tus últimos cambios locales:
   ```bash
   git add .
   git commit -m "feat: finalizar implementación"
   git push
   ```
2. Entra a tu Draft PR en GitHub y haz clic en el botón **"Ready for review"**. Esto lo convierte en un Pull Request normal listo para ser revisado y aprobado.

---

## 6. Movimiento automático en el tablero Scrum
* **Al abrir el Draft PR:** Queda vinculado al issue y lo mantiene/mueve a **`In progress`**.
* **Al pulsar "Ready for review":** El PR pasa a estado normal y mueve la tarjeta a **`In review`**.
* **Al aprobarse y hacer Merge a `develop`:** GitHub detecta la palabra clave `Closes`, cierra el issue automáticamente y mueve la tarjeta a **`Done`**.
