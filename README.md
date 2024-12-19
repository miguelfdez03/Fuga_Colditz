# La Fuga de Colditz

Este proyecto es una adaptación en Java del clásico juego de estrategia **"La Fuga de Colditz"**, donde el jugador asume el rol de un prisionero que intenta escapar de una fortaleza (la famosa prisión de Colditz) durante la Segunda Guerra Mundial. En esta versión, he implementado una **nueva interfaz gráfica** junto con mecánicas mejoradas y el movimiento autónomo de los guardias, controlados mediante **hilos**.

## Descripción del juego

El objetivo del juego es escapar de la prisión reunificando tres elementos clave, evitando ser atrapado por los guardias que patrullan la prisión. Los guardias se mueven de forma autónoma siguiendo patrones aleatorios gracias a la implementación de hilos, lo que añade emoción y dificultad al juego.

El jugador interactúa en un mapa estilo tablero, explorando distintas áreas de la prisión en busca de los elementos necesarios mientras planea su fuga.

### Características principales

- **Objetivo:** Recoge los tres elementos esenciales para poder escapar de la prisión:
    - Una llave de la celda.
    - Un mapa de la ruta de escape.
    - Un disfraz para engañar a los guardias.

- **Movimiento de los guardias:** Los guardias se desplazan por el mapa de manera aleatoria, gestionados de forma autónoma mediante **hilos en Java**, para simular un comportamiento dinámico y autónomo mientras intentan atraparte.

- **Interfaz gráfica mejorada:** Experiencia visual moderna, amigable e inmersiva.

- **Patrullaje y estrategia:** Analiza el movimiento de los guardias y encuentra rutas seguras para cumplir tu objetivo.

## Requisitos

Para poder jugar, necesitas cumplir con los siguientes requisitos:

- **Java JDK 23** o superior.
- Un IDE como **IntelliJ IDEA** (recomendado) o cualquier otra herramienta compatible con Java.
- Sistema operativo compatible con Java.

## Instalación y ejecución

Sigue estos pasos para instalar y ejecutar el proyecto:

1. Clona este repositorio:
   ```bash
   git clone https://github.com/miguelfdez03/Fuga-de-Colditz.git
   ```

2. Abre el proyecto en un IDE como **IntelliJ IDEA**.

3. Compila el proyecto seleccionando el archivo principal (`Prison.java` en la carpeta `src/main/java/...`).

4. Ejecuta el archivo principal.

5. ¡Empieza a jugar y trata de escapar!

## Controles del juego

- Usa las **teclas de flecha** ("W" arriba, "S" abajo, "A" izquierda, "D" derecha) para moverte por el mapa usa A,W,S,D o las flechitas de la interfaz grafica.
- Evita el contacto con los guardias para no ser atrapado.
- Busca los tres elementos clave dispersos por el mapa para desbloquear la salida.
