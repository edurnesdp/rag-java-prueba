# Mini RAG en Java – Pregunta a tus propios documentos

Este proyecto es un **ejemplo mínimo y didáctico** de cómo implementar el patrón **RAG (Retrieval‑Augmented Generation)** usando Java.

El objetivo **no es construir un sistema complejo**, sino **entender qué es RAG, para qué sirve y por qué es útil en entornos reales**.

---

## 🧠 ¿Qué problema resuelve RAG?

Los modelos de IA generativa (como ChatGPT) tienen un problema importante:

- No conocen tus documentos
- No conocen tu empresa
- No conocen tus normas internas
- Si no saben algo, **pueden inventar la respuesta**

Ejemplo:
> “¿Cómo funciona nuestro sistema de facturación?”

Una IA genérica no lo sabe → puede responder algo incorrecto.

---

## ✅ La idea de RAG (explicada fácil)

**RAG combina dos cosas**:

1. 🔍 **Buscar información real** (tus documentos)
2. ✍️ **Usar la IA solo para redactar la respuesta**

En vez de preguntar directamente a la IA, el sistema:
1. Busca primero en tus documentos
2. Selecciona los textos relevantes
3. Le pide a la IA que responda **usando solo esa información**

👉 La IA **no inventa**, solo explica.

---

## 🧩 ¿Qué hace este mini‑proyecto?

Este proyecto implementa un flujo RAG **muy sencillo**:

1. Carga documentos de texto locales (`.txt`)
2. Busca los fragmentos más relevantes para una pregunta
3. Construye un prompt con esa información
4. Pide a una IA que genere la respuesta
5. Devuelve la respuesta al usuario

Todo de forma clara y fácil de entender.

---

## 🏗️ Arquitectura (simplificada)

![Arquitectura Simplificada](docs/images/arquitectura-simplificada.png)

Este diagrama representa el flujo simplificado de la arquitectura del proyecto.

### SearchService

Este servicio se encarga de buscar fragmentos relevantes
en documentos locales a partir de una pregunta del usuario.

Para simplificar el aprendizaje:
- Los documentos se dividen en párrafos
- Se usan palabras clave para calcular relevancia

En un sistema real, este componente se sustituiría por
búsqueda semántica basada en vectores.

### DocumentLoader

Este componente se encarga de cargar documentos locales
y dividirlos en fragmentos (DocumentChunks).

Cada fragmento contiene:
- El texto del documento (content)
- El origen del texto (source)

Su única responsabilidad es preparar la información
para que pueda ser buscada posteriormente.

### RagService

Este servicio implementa el patrón RAG (Retrieval‑Augmented Generation).

Su responsabilidad es:
- Unir la pregunta del usuario
- Los fragmentos de documentos encontrados
- Y construir un prompt claro para la IA

La IA no busca información ni decide qué usar;
solo genera la respuesta basándose en el contexto proporcionado.

### LlmClient

La integración con la IA se encapsula en un cliente específico (`LlmClient`).

La IA:
- No busca documentos
- No accede al sistema
- No decide qué información es válida

Solo recibe un texto (prompt) y genera una respuesta.
El control del comportamiento reside completamente en la arquitectura.

---

## 🚀 Cómo compilar, ejecutar y debuggear la aplicación

### Compilar la aplicación
Para compilar el proyecto, asegúrate de tener Maven instalado y ejecuta el siguiente comando:

```bash
mvn clean install
```

### Ejecutar la aplicación
Puedes ejecutar la aplicación utilizando el siguiente comando:

```bash
mvn spring-boot:run
```

Si deseas ejecutarla con un perfil específico, como `mock`, utiliza:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mock
```

### Debuggear la aplicación
Para debuggear la aplicación, puedes iniciar Spring Boot en modo debug con el siguiente comando:

```bash
mvn spring-boot:run -Dspring-boot.run.fork=false -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
```

Esto abrirá el puerto `5005` para que puedas conectar tu depurador favorito.
