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

### 🏗️ Arquitectura (simplificada)

```
┌──────────────┐
│    Usuario   │
└───────┬──────┘
        │ Pregunta
        ▼
┌────────────────────┐
│ API Java (Spring)  │
│  Endpoint /ask     │
└───────┬────────────┘
        │
        │ 1. Analiza la pregunta
        ▼
┌────────────────────┐
│ Búsqueda en        │
│ documentos locales │
│ (SearchService)    │
└───────┬────────────┘
        │
        │ 2. Textos relevantes
        ▼
┌────────────────────┐
│ Construcción del   │
│ prompt RAG         │
│ (pregunta + textos)│
└───────┬────────────┘
        │
        │ 3. Generar respuesta
        ▼
┌────────────────────┐
│ IA Generativa      │
│ (solo redacta)     │
└───────┬────────────┘
        │
        ▼
┌──────────────┐
│  Respuesta   │
└──────────────┘
```

Este diagrama representa el flujo simplificado de la arquitectura del proyecto.

### SearchService

Este servicio se encarga de buscar fragmentos relevantes
en documentos locales a partir de una pregunta del usuario.

Para simplificar el aprendizaje:
- Los documentos se dividen en párrafos
- Se usan palabras clave para calcular relevancia

En un sistema real, este componente se sustituiría por
búsqueda semántica basada en vectores.

### SemanticSearchService

El `SemanticSearchService` es una implementación del servicio `SearchService` que utiliza embeddings para realizar búsquedas semánticas en los documentos cargados. Este servicio:

- Genera embeddings para cada fragmento de documento utilizando un cliente de embeddings (`EmbeddingClient`).
- Calcula la similitud entre el embedding de la pregunta y los embeddings de los fragmentos para encontrar los más relevantes.
- Devuelve los fragmentos más relevantes ordenados por su similitud.

### SimulatedOpenAiEmbeddingClient

El `SimulatedOpenAiEmbeddingClient` es una implementación simulada del cliente de embeddings (`EmbeddingClient`). Este cliente:

- Genera un vector de tamaño fijo (256) para representar un texto.
- Utiliza un enfoque simple basado en hashing de tokens para asignar valores al vector.
- Normaliza el vector resultante para que tenga una magnitud unitaria.

Este cliente es útil para pruebas y simulaciones, ya que no requiere una conexión a un servicio externo.

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

### HybridSearchService

El `HybridSearchService` es una implementación del servicio `SearchService` que combina resultados de búsquedas por palabras clave y búsquedas semánticas. Este servicio:

- Utiliza `KeyWordSearchService` para realizar búsquedas precisas basadas en palabras clave.
- Utiliza `SemanticSearchService` para realizar búsquedas flexibles basadas en embeddings.
- Asigna diferentes pesos a los resultados de cada búsqueda y los combina para devolver los fragmentos más relevantes.

Este enfoque híbrido equilibra precisión y flexibilidad, proporcionando resultados más completos.

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

---

### Autenticación

La aplicación utiliza Spring Security para proteger ciertos endpoints mediante autenticación básica. A continuación, se describen los detalles de la configuración:

- **Endpoints protegidos**:
  - `/ask`
  - `/status`
- **Usuarios predeterminados**:
  - Usuario estándar:
    - Nombre de usuario: `user`
    - Contraseña: `password`
    - Rol: `USER`
  - Administrador:
    - Nombre de usuario: `admin`
    - Contraseña: `admin`
    - Rol: `ADMIN`
- **Método de autenticación**:
  - Autenticación básica (Basic Auth), donde el cliente envía las credenciales en el encabezado de la solicitud.

#### Ejemplo de uso
Puedes probar la autenticación utilizando herramientas como `curl` o Postman.

**Ejemplo con `curl`**:
```bash
curl -u user:password http://localhost:8080/ask
```

Si las credenciales son correctas, recibirás una respuesta exitosa. Si no, obtendrás un error `401 Unauthorized`.

---

**Nota:** Este proyecto ha sido generado parcialmente utilizando GitHub Copilot, una herramienta de asistencia de código basada en inteligencia artificial. Todo el código generado ha sido revisado y adaptado para cumplir con los requisitos del proyecto.
