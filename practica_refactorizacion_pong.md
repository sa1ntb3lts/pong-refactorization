# Práctica de laboratorio: Refactorización y mejora de un videojuego Pong en Java

---

## 1. Propósito de la práctica

Aplicar técnicas de mantenimiento correctivo, refactorización y mejora de software sobre una aplicación Java existente que implementa una versión básica del videojuego Pong.

Los estudiantes deberán analizar el código fuente, identificar problemas relacionados con concurrencia, consumo de recursos, detección de colisiones, manejo de eventos y organización del código, e implementar soluciones que mejoren su funcionamiento, calidad y mantenibilidad.

La práctica permitirá comprender la importancia de separar responsabilidades, administrar correctamente el ciclo de vida de los procesos concurrentes y aplicar pruebas para verificar el comportamiento de una aplicación interactiva.

## 2. Objetivo de aprendizaje

Al finalizar la práctica, el estudiante será capaz de:

1. Analizar código Java existente para identificar defectos funcionales y problemas de diseño.
2. Aplicar técnicas de refactorización para mejorar la cohesión, el acoplamiento y la mantenibilidad.
3. Implementar mecanismos seguros para controlar la ejecución, pausa, reanudación y terminación de una animación.
4. Mejorar los algoritmos de detección de colisiones y movimiento de objetos gráficos.
5. Diseñar y ejecutar pruebas que permitan verificar las modificaciones realizadas.
6. Documentar las decisiones técnicas y justificar las mejoras implementadas.

## 3. Contexto del problema

Se proporciona el código fuente de `pong_project`, una aplicación desarrollada con Java Swing y Java 2D que implementa un videojuego Pong para un jugador.

El proyecto contiene cuatro clases principales:

| Clase | Responsabilidad actual |
|---|---|
| `Main` | Inicialización y ejecución de la aplicación. |
| `Board` | Administración del tablero, renderizado e interacción con el usuario. |
| `BallRunner` | Actualización del movimiento de la pelota. |
| `Paddle` | Representación y movimiento de la raqueta. |

El programa utiliza hilos para actualizar la posición de la pelota y solicitar el repintado de la interfaz.

Aunque la aplicación implementa las funcionalidades básicas del juego, hay oportunidades de mejora relacionadas con la sincronización de los hilos, el control de la ejecución, la detección de colisiones y la separación de responsabilidades.

**Reto:** mejorar la implementación existente, manteniendo la funcionalidad del videojuego y aplicando buenas prácticas de desarrollo de software.

## 4. Actividades de desarrollo

### Actividad 1. Análisis y diagnóstico del código

**Objetivo:** comprender la arquitectura del programa e identificar las causas de los problemas detectados.

Examine las clases `Main`, `Board`, `BallRunner` y `Paddle`.

Realice las siguientes tareas:

1. Identifique las responsabilidades de cada clase.
2. Localice los métodos que actualizan el estado del juego.
3. Determine qué objetos y variables son compartidos entre hilos.
4. Analice cómo se implementan las operaciones de pausa y reanudación.
5. Identifique las instrucciones responsables de la detección de colisiones.
6. Determine cómo se inicia y termina la ejecución de los hilos.
7. Identifique posibles violaciones al principio de responsabilidad única (SRP).

**Entregable parcial:** una tabla de diagnóstico con al menos cinco problemas identificados, su ubicación, consecuencias y propuesta de solución.

### Actividad 2. Mejora del control de ejecución

**Objetivo:** eliminar el uso de espera activa y establecer un mecanismo adecuado para administrar el estado del juego.

Actualmente, `BallRunner` utiliza un ciclo de ejecución que recurre a `Thread.yield()` cuando el juego está pausado.

Modifique la implementación para cumplir los siguientes requerimientos:

- El juego deberá poder iniciarse, pausarse y reanudarse.
- Durante la pausa, la pelota deberá permanecer inmóvil.
- La pausa no deberá provocar un consumo innecesario de CPU mediante ciclos de espera activa.
- Deberá existir un mecanismo para finalizar la ejecución de forma controlada.
- Al cerrar la ventana, no deberán permanecer hilos de la aplicación ejecutándose innecesariamente.
- El estado del juego deberá administrarse mediante una representación explícita, por ejemplo, `RUNNING`, `PAUSED` y `STOPPED`.

**Restricción:** no se permite resolver la pausa agregando únicamente un tiempo de espera dentro de un ciclo de sondeo continuo.

Se recomienda utilizar `javax.swing.Timer` para administrar el ciclo de actualización de la animación, dado que sus eventos se ejecutan en el Event Dispatch Thread (EDT) de Swing.

Alternativamente, podrá conservarse una implementación con hilos, siempre que se utilice un mecanismo de coordinación apropiado y se justifique técnicamente.

### Actividad 3. Corrección de problemas de concurrencia

**Objetivo:** garantizar la consistencia del estado del juego y evitar accesos concurrentes inseguros.

Revise las operaciones que modifican o consultan la posición de la pelota, la raqueta y las variables de control.

Implemente las modificaciones necesarias para:

1. Evitar condiciones de carrera.
2. Garantizar la visibilidad de los cambios de estado entre hilos, cuando corresponda.
3. Evitar modificaciones simultáneas no coordinadas de los objetos del juego.
4. Realizar las actualizaciones de los componentes Swing en el EDT.
5. Separar las operaciones de actualización del estado y representación gráfica.

**Consideración:** si se utiliza `javax.swing.Timer` y todas las modificaciones del estado del juego se realizan en el EDT, no será necesario incorporar sincronización adicional para ese estado.

Documente las decisiones adoptadas.

### Actividad 4. Mejora del movimiento y detección de colisiones

**Objetivo:** corregir posibles errores de rebote y mejorar la precisión del movimiento de la pelota.

Analice el algoritmo actual, particularmente el desplazamiento en incrementos de 20 píxeles y las comprobaciones utilizadas para determinar colisiones.

Implemente las siguientes mejoras:

- Representar la posición y velocidad de la pelota mediante valores numéricos apropiados.
- Separar la velocidad horizontal de la vertical.
- Detectar correctamente las colisiones con los límites laterales y superior del tablero.
- Detectar la colisión con la raqueta considerando sus dimensiones reales.
- Evitar que la pelota atraviese la raqueta debido a incrementos grandes de posición.
- Evitar que una misma colisión produzca múltiples inversiones consecutivas de velocidad.
- Definir explícitamente qué ocurre cuando la pelota supera el límite inferior del tablero.

La lógica de colisiones deberá considerar el tamaño de la pelota y no únicamente la posición de su origen.

**Requerimiento adicional:** la pelota deberá mantenerse dentro de los límites permitidos del área de juego, excepto cuando se produzca la condición definida para una pelota perdida.

### Actividad 5. Refactorización de la arquitectura

**Objetivo:** mejorar la organización del código mediante la separación de responsabilidades.

Refactorice el proyecto para distinguir claramente las siguientes responsabilidades:

| Responsabilidad | Descripción |
|---|---|
| Modelo del juego | Mantener el estado, posiciones y velocidades. |
| Lógica del juego | Actualizar posiciones y detectar colisiones. |
| Renderizado | Dibujar el tablero y los objetos gráficos. |
| Control de entrada | Interpretar las acciones del teclado. |
| Control de ejecución | Administrar inicio, pausa, reanudación y terminación. |

No es obligatorio implementar una arquitectura MVC completa. Sin embargo, la solución deberá demostrar una separación razonable entre la lógica del juego y su representación gráfica.

Considere las siguientes recomendaciones:

- Reducir las responsabilidades de `Board`.
- Evitar que `paintComponent()` modifique el estado del juego.
- Encapsular las propiedades de los objetos.
- Eliminar código duplicado.
- Utilizar constantes con nombres descriptivos.
- Evitar valores numéricos fijos dispersos en el código.
- Mantener métodos pequeños y con responsabilidades claramente definidas.

**Entregable parcial:** diagrama de clases UML de la arquitectura refactorizada.

### Actividad 6. Mejora de la interacción y controles

**Objetivo:** mejorar la confiabilidad de los controles del videojuego.

Verifique el funcionamiento de los controles originales:

| Tecla | Acción |
|---|---|
| Flecha izquierda | Desplazar la raqueta hacia la izquierda. |
| Flecha derecha | Desplazar la raqueta hacia la derecha. |
| Barra espaciadora | Pausar el juego. |
| `R` | Reanudar el juego. |

Implemente las modificaciones necesarias para garantizar que:

1. La raqueta no pueda desplazarse fuera del tablero.
2. Los controles respondan correctamente durante la ejecución.
3. La pausa y la reanudación no generen múltiples ciclos de animación.
4. Los eventos del teclado no dependan de un enfoque de ventana innecesariamente frágil.
5. El comportamiento de los controles sea consistente con el estado actual del juego.

Se recomienda investigar y considerar el uso de *Key Bindings* de Swing como alternativa a `KeyListener`.

### Actividad 7. Pruebas de funcionamiento

**Objetivo:** comprobar que las modificaciones corrigen los problemas identificados sin introducir nuevas fallas.

Diseñe y ejecute al menos los siguientes casos de prueba:

| ID | Caso de prueba | Resultado esperado |
|---|---|---|
| CP-01 | Iniciar el juego | La pelota comienza a moverse correctamente. |
| CP-02 | Pausar el juego | La pelota deja de moverse. |
| CP-03 | Reanudar el juego | La pelota continúa su movimiento sin crear ciclos adicionales. |
| CP-04 | Cerrar la aplicación | La ejecución termina de forma controlada. |
| CP-05 | Colisión con pared izquierda | La pelota invierte su velocidad horizontal. |
| CP-06 | Colisión con pared derecha | La pelota invierte su velocidad horizontal. |
| CP-07 | Colisión con límite superior | La pelota invierte su velocidad vertical. |
| CP-08 | Colisión con la raqueta | La pelota rebota correctamente. |
| CP-09 | Movimiento de la raqueta | La raqueta permanece dentro del tablero. |
| CP-10 | Pelota perdida | Se ejecuta la acción definida para esa condición. |
| CP-11 | Pausar y reanudar repetidamente | No se crean ciclos adicionales ni se altera la velocidad inesperadamente. |
| CP-12 | Colisión cercana al borde de la raqueta | La detección funciona considerando las dimensiones de ambos objetos. |

Implemente al menos cuatro pruebas automatizadas con JUnit, preferentemente para verificar la lógica de movimiento, las colisiones y las transiciones de estado.

Las pruebas automatizadas deberán poder ejecutarse sin necesidad de mostrar una ventana Swing, siempre que sea técnicamente posible.

## 5. Requisitos técnicos

La solución deberá cumplir las siguientes condiciones:

1. Utilizar Java y conservar Swing y Java 2D para la interfaz gráfica.
2. Mantener la funcionalidad básica del proyecto original.
3. No incorporar bibliotecas externas para implementar la lógica del videojuego.
4. Utilizar JUnit para las pruebas automatizadas. (*opcional*)
5. Aplicar encapsulamiento y separación de responsabilidades.
6. Evitar esperas activas (*busy waiting*).
7. Administrar correctamente el ciclo de vida de la animación.
8. Documentar los métodos y decisiones de diseño relevantes.
9. Mantener el proyecto compilable y ejecutable desde su configuración de construcción.

## 6. Entregables

Cada estudiante o equipo deberá entregar:

1. **Código fuente refactorizado:** repositorio Git con el proyecto completo y las modificaciones realizadas.
2. **Reporte de diagnóstico:** problemas identificados, impacto y soluciones implementadas.
3. **Evidencias de pruebas:** resultados automatizados y registro de pruebas funcionales.
4. **Reporte técnico de mejoras:** cambios, justificación, comparación entre versiones, dificultades y limitaciones.
5. **Historial Git:** commits que permitan identificar las principales etapas de refactorización.

## 7. Rúbrica de evaluación

La práctica se evaluará mediante seis criterios, con cuatro niveles de desempeño.

### Criterio 1. Análisis y diagnóstico del código — 15 puntos

| Nivel | Descriptor | Puntos |
|---|---|---:|
| Excelente | Identifica al menos cinco problemas relevantes, explica sus causas y consecuencias, y propone soluciones técnicamente fundamentadas. | 15 |
| Bueno | Identifica al menos cuatro problemas y propone soluciones adecuadas, aunque algunas justificaciones son incompletas. | 12 |
| Satisfactorio | Identifica al menos tres problemas, pero presenta explicaciones superficiales o soluciones parcialmente adecuadas. | 9 |
| Necesita mejorar | Identifica menos de tres problemas o no demuestra comprensión de sus causas. | 0–6 |

### Criterio 2. Control de ejecución y concurrencia — 25 puntos

| Nivel | Descriptor | Puntos |
|---|---|---:|
| Excelente | Implementa correctamente inicio, pausa, reanudación y terminación; elimina la espera activa y garantiza un acceso seguro al estado compartido. | 25 |
| Bueno | Implementa los controles y elimina la espera activa, con detalles menores pendientes en la administración del ciclo de vida. | 20 |
| Satisfactorio | Los controles funcionan parcialmente, pero persisten problemas de concurrencia o terminación. | 15 |
| Necesita mejorar | Mantiene esperas activas, condiciones de carrera importantes o errores que impiden controlar la ejecución. | 0–10 |

### Criterio 3. Movimiento y detección de colisiones — 20 puntos

| Nivel | Descriptor | Puntos |
|---|---|---:|
| Excelente | Implementa movimiento consistente y detecta correctamente colisiones con paredes y raqueta, incluyendo casos límite. | 20 |
| Bueno | Las colisiones principales funcionan correctamente, con errores menores en situaciones extremas. | 16 |
| Satisfactorio | Implementa las colisiones básicas, pero persisten errores de rebote o límites. | 12 |
| Necesita mejorar | Las colisiones presentan errores frecuentes o el movimiento no funciona correctamente. | 0–8 |

### Criterio 4. Refactorización y calidad del código — 20 puntos

| Nivel | Descriptor | Puntos |
|---|---|---:|
| Excelente | Presenta una arquitectura modular, responsabilidades claramente separadas, encapsulamiento adecuado y código legible sin duplicación significativa. | 20 |
| Bueno | Mejora claramente la organización del código, aunque conserva algunos problemas menores de acoplamiento o duplicación. | 16 |
| Satisfactorio | Realiza una separación parcial de responsabilidades, pero mantiene dependencias innecesarias. | 12 |
| Necesita mejorar | Conserva una estructura altamente acoplada o introduce complejidad innecesaria. | 0–8 |

### Criterio 5. Pruebas y verificación — 15 puntos

| Nivel | Descriptor | Puntos |
|---|---|---:|
| Excelente | Ejecuta los 12 casos funcionales, implementa al menos cuatro pruebas JUnit pertinentes y presenta evidencias claras de los resultados. | 15 |
| Bueno | Ejecuta al menos diez casos funcionales e implementa tres pruebas automatizadas pertinentes. | 12 |
| Satisfactorio | Ejecuta al menos ocho casos funcionales e implementa una o dos pruebas automatizadas. | 9 |
| Necesita mejorar | Presenta pruebas insuficientes, sin resultados verificables o con fallas importantes no documentadas. | 0–6 |

### Criterio 6. Documentación y entregables — 5 puntos

| Nivel | Descriptor | Puntos |
|---|---|---:|
| Excelente | Entrega todos los productos, documentación clara, UML actualizado e historial Git que evidencia el proceso de mejora. | 5 |
| Bueno | Entrega los productos principales, con omisiones menores de documentación. | 4 |
| Satisfactorio | Entrega parcialmente la documentación o presenta inconsistencias con el código. | 3 |
| Necesita mejorar | Faltan varios entregables o la documentación no corresponde a la implementación. | 0–2 |

### Resumen de ponderaciones

| Criterio | Ponderación |
|---|---:|
| Análisis y diagnóstico | 15% |
| Control de ejecución y concurrencia | 25% |
| Movimiento y colisiones | 20% |
| Refactorización y calidad del código | 20% |
| Pruebas y verificación | 15% |
| Documentación y entregables | 5% |
| **Total** | **100%** |

La calificación final será la suma de los puntos obtenidos en cada criterio.

## 8. Preguntas de reflexión

Incluya en el reporte técnico respuestas a las siguientes preguntas:

1. ¿Qué problemas puede ocasionar modificar el estado de un componente Swing desde un hilo diferente al EDT?
2. ¿Por qué `Thread.yield()` no constituye una solución adecuada para implementar una pausa?
3. ¿Qué ventajas y limitaciones ofrece `javax.swing.Timer` frente a una implementación basada en `Thread`?
4. ¿Cómo influye la magnitud del desplazamiento de la pelota en la precisión de la detección de colisiones?
5. ¿Qué beneficios aporta separar la lógica del juego de su representación gráfica?
6. ¿Cómo ayudan las pruebas automatizadas a reducir el riesgo de introducir defectos durante una refactorización?
7. ¿Qué aspectos del diseño actual dificultarían agregar un segundo jugador, un marcador o diferentes niveles de dificultad?

## 9. Resultado esperado

Al finalizar la práctica, los estudiantes deberán presentar una versión mejorada del videojuego Pong que conserve su funcionalidad original y demuestre avances verificables en:

- Seguridad y control de la ejecución.
- Precisión del movimiento y las colisiones.
- Organización y mantenibilidad del código.
- Confiabilidad de los controles.
- Calidad de las pruebas y documentación.


