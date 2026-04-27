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
