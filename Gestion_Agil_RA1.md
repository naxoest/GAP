# UNIVERSIDAD DE LOS LAGOS
## DEPARTAMENTO DE CIENCIAS DE LA INGENIERÍA
### INGENIERÍA CIVIL EN INFORMÁTICA
**GESTIÓN ÁGIL DE PROYECTOS**
**CAMPUS OSORNO, CHILE**

---

# HISTORIAS DE USUARIO Y GESTIÓN ÁGIL DE REQUISITOS
### APLICACIÓN DE DOR, INVEST, CRITERIOS DE ACEPTACIÓN, KANBAN Y ESTIMACIÓN RELATIVA

**Autores:**
* Niska Silva - Franco Comas
* Luis Rios - Ignacio Soto
* Felipe Vera - Antoine Briones
* Sebastian Adriazola - Ignacio Vega

**Fecha:** 2 de julio de 2026

*Universidad Acreditada - Comisión Nacional de Acreditación (Acreditación Avanzada)*

---

## Resumen
El presente informe restablece el contenido institucional del documento y organiza los elementos principales de la gestión ágil de requisitos para un proyecto de software. Se describen las historias de usuario como mecanismo para representar necesidades desde la perspectiva de quienes utilizarán el sistema; se incorpora una *Definition of Ready* (DoR) basada en el criterio INVEST y en la estructura *"Como... quiero... para..."*; se formulan criterios de aceptación mediante el patrón *"Dado... cuándo... entonces..."*; se propone un tablero Kanban con estados que permiten visualizar el flujo de trabajo; y se explica la estimación relativa en puntos de historia usando la secuencia de Fibonacci modificada mediante *Planning Poker*. Con ello, el documento entrega una base ordenada para planificar, priorizar, validar y controlar el avance de un equipo de desarrollo.

**Palabras Clave:** Historias de usuario, DoR, INVEST, Kanban, Planning Poker

---

## Capítulo 1: Requerimientos
En base a lo mínimo que necesitábamos para crear un producto mínimo viable (PMV), trabajaremos con estos requerimientos para el desarrollo del presente informe:
1. Pagar el pasaje con la tarjeta.
2. Permite recargar en un punto físico.
3. Ver el saldo de la tarjeta.

---

## Capítulo 2: Historias de usuario
A partir de la Onda 1 (MVP) definida previamente en el Secuenciador de Features de *Lean Inception*, el equipo seleccionó las tres features críticas indicadas en el capítulo anterior. Cada una de ellas fue rebanada (*sliced*) en tres Historias de Usuario más pequeñas, negociables y estimables, evitando en todo momento historias técnicas o autorreferenciales, de modo que cada tarjeta represente valor percibido por un usuario real del sistema.

### 2.1. Feature 1: Pagar el pasaje con la tarjeta
Esta feature agrupa la necesidad de reemplazar el pago en efectivo por un medio electrónico dentro de la micro. Se identificaron tres roles con intereses distintos sobre la misma funcionalidad:
1. **HU1:** El pasajero, que busca pagar sin depender de efectivo.
2. **HU2:** El chofer, que busca reducir el tiempo destinado a entregar vueltos.
3. **HU3:** El jefe de línea de buses, que busca contar con un historial contable digital.

### 2.2. Feature 2: Permite recargar en un punto físico
Esta feature responde a la necesidad de mantener un canal presencial de recarga para quienes no cuentan con medios digitales o prefieren la atención directa:
1. **HU4:** El usuario de tercera edad, que busca recargar sin depender de tecnologías avanzadas.
2. **HU5:** El usuario promedio, que busca recargar en efectivo cuando no cuenta con un medio digital en momentos críticos.
3. **HU6:** El dueño de un negocio, que busca instalar un punto de carga en su local para aumentar el flujo de clientes.

### 2.3. Feature 3: Ver el saldo de la tarjeta
Esta feature cubre la necesidad de transparencia sobre los fondos disponibles antes, durante y después de un viaje:
1. **HU7:** El usuario promedio, que busca verificar si tiene saldo suficiente para tomar el siguiente bus.
2. **HU8:** El usuario promedio, que busca decidir si es necesario recargar.
3. **HU9:** El usuario promedio, que busca mantener un control sobre sus finanzas de transporte.

### 2.4. Matriz de trazabilidad
La Tabla 2.1 resume la trazabilidad entre las features de la Onda 1 y las nueve Historias de Usuario resultantes del rebanado, cumpliendo con el mínimo de tres historias por feature exigido en la actividad.

#### Tabla 2.1: Trazabilidad Feature → Historia de Usuario
| Feature | Historia de Usuario | ID |
| :--- | :--- | :--- |
| **Pagar el pasaje con la tarjeta** | Pasajero paga con tarjeta<br>Chofer ahorra tiempo en vueltos<br>Jefe de línea con historial digital | HU1<br>HU2<br>HU3 |
| **Recargar en un punto físico** | Tercera edad recarga presencial<br>Usuario promedio recarga en efectivo<br>Dueño de negocio como punto de carga | HU4<br>HU5<br>HU6 |
| **Ver el saldo de la tarjeta** | Usuario verifica saldo antes de abordar<br>Usuario decide si recargar<br>Usuario controla sus finanzas | HU7<br>HU8<br>HU9 |

---

## Capítulo 3: DoR con INVEST: Como ... quiero ... para ...

A continuación se presentan las estructuras narrativas base y se detalla la evaluación INVEST aplicada individualmente a cada una de las nueve historias, evidenciando el análisis de Independencia, Negociabilidad, Valor, Estimabilidad, Tamaño y Testeabilidad que sustenta su aprobación bajo la *Definition of Ready*.

### Enunciados Base:
1. **HU1:** Como pasajero quiero poder pagar el pasaje con la tarjeta para no depender del efectivo.
2. **HU2:** Como chofer quiero que paguen con tarjeta para ahorrar tiempo en la entrega de vueltos.
3. **HU3:** Como jefe de línea de buses quiero que los pagos sean digitales para poder tener un historial contable digital por sobre uno físico.
4. **HU4:** Como usuario de tercera edad quiero poder cargar en un punto físico para no dificultarme con tecnologías avanzadas.
5. **HU5:** Como usuario promedio quiero poder cargar en un punto físico para cuando no cuente con un medio digital en momentos críticos.
6. **HU6:** Como dueño de un negocio quiero que se pueda cargar en un punto físico para tenerlo en mi local y así aumentar el flujo de clientes, tentándolos a que me compren o adquieran algún servicio.
7. **HU7:** Como usuario promedio quiero ver el saldo de la tarjeta para ver si puedo tomar el siguiente bus.
8. **HU8:** Como usuario promedio quiero ver el saldo de la tarjeta para decidir si es necesario recargar.
9. **HU9:** Como usuario promedio quiero ver el saldo de la tarjeta para mantener un control sobre mis finanzas.

### Evaluación INVEST Detallada:

#### HU1 - Como pasajero quiero poder pagar el pasaje con la tarjeta para no depender del efectivo.
* **Independiente:** No depende de otras tarjetas.
* **Negociable:** En la parte técnica, no necesariamente se puede implementar de una sola manera.
* **Valiosa:** Indiscutiblemente valiosa para el usuario final.
* **Estimable:** Sí, se tiene reconocimiento y claridad técnica para dimensionarla.
* **Pequeña:** Sí, debe completarse en pocos días dentro del Sprint, ya que es uno de los requerimientos base.
* **Testeable:** Sí, posee reglas binarias; funciona o no funciona.

#### HU2 - Como chofer quiero que paguen con tarjeta para ahorrar tiempo en la entrega de vueltos.
* **Independiente:** No es independiente; depende de que HU1 esté implementada, ya que el ahorro de tiempo del chofer es consecuencia directa de que el pago con tarjeta ya funcione.
* **Negociable:** Es negociable porque la forma en que se realiza el pago puede cambiar según las necesidades del usuario.
* **Valiosa:** Sí, tiene valor operativo claro: reduce los tiempos de detención de la micro y mejora la puntualidad del recorrido.
* **Estimable:** Sí, es estimable porque no agrega lógica nueva de pago, solo aprovecha lo construido en HU1, por lo que el esfuerzo es bajo y conocido.
* **Pequeña:** Sí, no requiere desarrollo adicional más allá de validar tiempos de transacción en el validador.
* **Testeable:** Sí, se puede comparar el tiempo promedio de detención con tarjeta versus con efectivo, además de los escenarios BDD ya definidos.

#### HU3 - Como jefe de línea de buses quiero que los pagos sean digitales para tener un historial contable digital.
* **Independiente:** Depende parcialmente de HU1, ya que necesita que existan transacciones digitales para poder registrarlas.
* **Negociable:** Es negociable; el formato del historial (dashboard, reporte descargable, panel web) puede ajustarse con el jefe de línea.
* **Valiosa:** Sí, aporta valor directo al negocio al reemplazar el control contable físico por uno digital, reduciendo errores y tiempo administrativo.
* **Estimable:** Sí, aunque requiere más esfuerzo que HU1 o HU2 por incluir persistencia y consulta de datos.
* **Pequeña:** Moderada; no es tan pequeña como HU1/HU2 porque involucra un módulo de reportería, pero sigue acotada a un solo flujo (consultar historial).
* **Testeable:** Sí, se valida verificando que cada pago aprobado quede registrado y sea visible en el módulo de historial.

#### HU4 - Como usuario de tercera edad quiero poder cargar en un punto físico para no dificultarme con tecnologías avanzadas.
* **Independiente:** Sí, es independiente de las historias de pago con tarjeta (HU1-HU3), solo depende de que exista la tarjeta física.
* **Negociable:** Es negociable; el canal físico puede ser un almacén, una caja, un kiosco, etc.
* **Valiosa:** Sí, es de alto valor social: garantiza la inclusión de usuarios sin acceso o manejo de medios digitales.
* **Estimable:** Sí, se puede dimensionar considerando la integración con el punto de venta del comercio autorizado.
* **Pequeña:** Es una historia mediana, ya que incluye tanto el caso exitoso como el de falla de conexión, pero se mantiene acotada a un solo flujo de recarga presencial.
* **Testeable:** Sí, se valida con los dos escenarios BDD ya definidos (recarga exitosa con comprobante, y rechazo por falta de conexión sin cobrar).

#### HU5 - Como usuario promedio quiero poder cargar en un punto físico para cuando no cuente con un medio digital en momentos críticos.
* **Independiente:** Sí, es independiente; reutiliza el mismo canal físico de HU4 pero no depende de ella para funcionar.
* **Negociable:** Es negociable en cuanto al monto mínimo de recarga permitido.
* **Valiosa:** Sí, da continuidad de servicio al usuario en situaciones de emergencia o falta de saldo digital.
* **Estimable:** Sí, es estimable porque reutiliza la misma infraestructura de recarga física que HU4.
* **Pequeña:** Sí, solo agrega la validación del monto mínimo sobre un flujo ya existente.
* **Testeable:** Sí, mediante los escenarios de recarga exitosa y rechazo por monto insuficiente ya definidos.

#### HU6 - Como dueño de un negocio quiero que se pueda cargar en un punto físico para aumentar el flujo de clientes.
* **Independiente:** No es del todo independiente; requiere que exista el flujo base de recarga física (HU4/HU5) antes de sumarle comisiones y promociones.
* **Negociable:** Es negociable; el modelo de comisión y el tipo de promoción pueden variar según el negocio.
* **Valiosa:** Sí, incentiva la adopción de nuevos puntos de carga al generar un beneficio económico directo para el comercio.
* **Estimable:** Es la más compleja de estimar dentro de la Feature 2, porque suma un módulo nuevo (comisiones y promociones) fuera del alcance transaccional básico.
* **Pequeña:** No es pequeña; es la historia más grande del set (por eso quedó en 8 puntos), podría beneficiarse de un rebanado adicional en una futura iteración.
* **Testeable:** Sí, se valida verificando que la comisión se genere correctamente y que la promoción se despliegue tras la recarga.

#### HU7 - Como usuario promedio quiero ver el saldo de la tarjeta para ver si puedo tomar el siguiente bus.
* **Independiente:** Sí, es completamente independiente; es solo una consulta de lectura sobre el saldo.
* **Negociable:** Es negociable en el canal de consulta (app, validador, sitio web).
* **Valiosa:** Sí, entrega información crítica en el momento justo antes de abordar.
* **Estimable:** Sí, es la historia más simple y sirvió como pivote en la sesión de Planning Poker.
* **Pequeña:** Sí, es la más pequeña de todo el backlog; es solo una consulta puntual.
* **Testeable:** Sí, mediante los dos escenarios ya definidos (consulta con conexión y consulta con último saldo local sin conexión).

#### HU8 - Como usuario promedio quiero ver el saldo de la tarjeta para decidir si es necesario recargar.
* **Independiente:** Depende parcialmente de HU7, ya que reutiliza la misma consulta de saldo, sumando la lógica de alerta por umbral.
* **Negociable:** Es negociable en cuanto al valor exacto del umbral mínimo de aviso.
* **Valiosa:** Sí, ayuda al usuario a anticiparse y evitar quedarse sin saldo al momento de viajar.
* **Estimable:** Sí, es estimable dado que se apoya en la consulta ya definida in HU7.
* **Pequeña:** Sí, solo agrega una regla de notificación sobre una función existente.
* **Testeable:** Sí, mediante los escenarios de estimación de pasajes disponibles y de aviso por saldo bajo el umbral.

#### HU9 - Como usuario promedio quiero ver el saldo de la tarjeta para mantener un control sobre mis finanzas.
* **Independiente:** Depende parcialmente de HU3, ya que reutiliza la lógica de historial de movimientos construida para el jefe de línea, pero orientada al usuario final.
* **Negociable:** Es negociable en el formato de entrega (historial en pantalla o reporte descargable).
* **Valiosa:** Sí, entrega transparencia y trazabilidad de gastos al usuario final.
* **Estimable:** Sí, aunque de mayor esfuerzo por incluir generación de reportes.
* **Pequeña:** Es moderada; no tan pequeña como HU7/HU8 por la generación de reportes descargables.
* **Testeable:** Sí, mediante los escenarios de consulta de historial y de generación de resumen mensual ya definidos.

---

## Capítulo 4: Criterios de aceptación: Dado... cuándo... entonces

Dado que hemos definido historias de usuario y luego haber normalizado utilizando DoR con INVEST, ahora podemos realizar los criterios de aceptación de cada una siguiendo los pasos de **DADO**, **CUANDO** y **ENTONCES**, estructurando escenarios específicos por cada historia de usuario.

### 4.1. Escenarios de la Feature 1 (Pagar el pasaje)

#### HU1: Pagar pasaje con tarjeta (Pasajero)
* **Escenario 1:** Dado que me dispongo a pagar el pasaje, cuando realizo el pago, el sistema lo toma correctamente y entonces puedo tomar el recorrido de la micro.
* **Escenario 2:** Dado que no tengo suficiente saldo en la tarjeta, cuando intento pagar con ella, entonces el sistema rechaza el pago y muestra un mensaje de saldo insuficiente.
* **Escenario 3:** Dado que realizo el pago, cuando carga el pago el sistema se cae, entonces no puedo pagar por la aplicación.

#### HU2: Ahorro de tiempo en vueltos (Chofer)
* **Escenario 1:** Dado que el pasajero tiene saldo suficiente en su tarjeta, cuando la acerca al validador del bus, entonces el pago se realiza automáticamente por lo que el chofer no necesita entregar vuelto.
* **Escenario 2:** Dado que varios pasajeros utilizan su tarjeta para pagar el pasaje cuando abordan el bus durante un recorrido, entonces el pago se realiza más rápido que con el efectivo y se reduce el tiempo de la micro detenida.
* **Escenario 3:** Dado que un pasajero se dispone a pagar con la tarjeta, cuando sucede que no tiene saldo, entonces paga en efectivo o se baja de la micro.

#### HU3: Historial contable digital (Jefe de línea)
* **Escenario 1:** Dado que el pasajero realiza su pago con la tarjeta, cuando la transacción es aprobada por el sistema, entonces el pago quedará registrado automáticamente en el historial digital.
* **Escenario 2:** Dado que ya existen pagos realizados y registrados en el sistema, cuando el jefe de la línea accede al módulo del historial, entonces puede visualizar todos los pagos que se han hecho de forma digital.

### 4.2. Escenarios de la Feature 2 (Recargar en punto físico)

#### HU4: Recarga presencial (Tercera edad)
* **Escenario 1:** Dado que el adulto mayor asiste a un almacén de barrio autorizado con su tarjeta de transporte y dinero en efectivo, cuando le solicita al almacenero una carga de 2000 pesos y le hace entrega del dinero, entonces el sistema del negocio procesa la transacción, suma los 2000 pesos al saldo de la tarjeta y emite un comprobante impreso para tranquilidad del usuario.
* **Escenario 2:** Dado que el adulto mayor se encuentra en la caja del punto de carga para añadir saldo a su tarjeta, cuando el operador intenta deslizar o acercar la tarjeta pero la máquina de recargas se encuentra sin señal o internet, entonces el sistema rechaza la operación, no realiza ningún cobro y el operador le notifica verbalmente al adulto mayor que intente en otro punto, manteniendo el saldo previo intacto.

#### HU5: Recarga en momentos críticos (Usuario promedio)
* **Escenario 1:** Dado que como usuario que no dispone de un medio digital para pago, cuando quiera solicitar una recarga en efectivo en un punto físico, entonces el saldo será abonado correctamente a la tarjeta.
* **Escenario 2:** Dado que el usuario desea realizar una recarga, cuando intenta cargar un monto inferior al mínimo permitido, entonces el sistema rechaza la operación e informa el monto mínimo aceptado.

#### HU6: Punto de carga en negocio comercial (Dueño de negocio)
* **Escenario 1:** Dado que el negocio se encuentra registrado como punto de carga autorizado y cuenta con el sistema habilitado, cuando un cliente ingresa al local para recargar su tarjeta de transporte, entonces el sistema procesa la recarga correctamente y registra al negocio como responsable de la transacción, generando la comisión correspondiente a su favor.
* **Escenario 2:** Dado que un cliente realiza una recarga en el punto físico del negocio, cuando finaliza la operación y recibe su comprobante, entonces el sistema despliega o el vendedor exhibe promociones del local, incentivando una compra adicional durante la visita.

### 4.3. Escenarios de la Feature 3 (Ver el saldo)

#### HU7: Verificar saldo antes de abordar (Usuario promedio)
* **Escenario 1:** Dado que el usuario se encuentra en el paradero y desea abordar el bus, cuando consulta el saldo de su tarjeta desde la aplicación móvil o acercándola al validador, entonces el sistema muestra el saldo actualizado y el usuario puede decidir si aborda el bus o gestiona una recarga previa.
* **Escenario 2:** Dado que el usuario intenta consultar el saldo de su tarjeta, cuando el sistema no logra conectarse a la red del servidor central, entonces se muestra el último saldo registrado localmente junto con una advertencia de que la información podría no estar actualizada.

#### HU8: Decidir si recargar según umbral (Usuario promedio)
* **Escenario 1:** Dado que el usuario desea planificar sus próximos viajes, cuando consulta el saldo de su tarjeta desde la aplicación, entonces el sistema despliega el monto disponible junto con una estimación de cuántos pasajes puede pagar con ese saldo.
* **Escenario 2:** Dado que el usuario consulta su saldo, cuando este se encuentra por debajo de un umbral mínimo definido, como el valor de un pasaje, entonces el sistema le notifica de forma visible que necesita recargar antes de su próximo viaje.

#### HU9: Control de finanzas y reportes (Usuario promedio)
* **Escenario 1:** Dado que el usuario quiere revisar sus gastos en transporte, cuando accede al historial de movimientos de su tarjeta, entonces el sistema muestra el saldo actual junto con el detalle de las últimas recargas y pagos realizados.
* **Escenario 2:** Dado que el usuario desea respaldar su información de gastos, cuando solicita un resumen mensual de movimientos de su tarjeta, entonces el sistema genera un reporte descargable con el saldo y los consumos del periodo seleccionado.

---

## Capítulo 5: Tablero Kanban: Product Backlog, Sprint Backlog, In Progress, Testing/Review y Done

Para dar gobernanza al proyecto se configuró un tablero Kanban digital con las cinco columnas normativas solicitadas: *Product Backlog*, *Sprint Backlog*, *In Progress*, *Testing/Review* y *Done*. Antes de cargar una historia en el *Sprint Backlog*, el equipo verifica que cumpla la política de *Definition of Ready* (DoR): sintaxis *Como... quiero... para...* correcta, checklist INVEST aprobado y al menos dos escenarios BDD redactados. Las nueve historias del capítulo 2 cumplen ya con esta política, por lo que se encuentran habilitadas para ser priorizadas.

Para el arranque del primer Sprint, el equipo priorizó las historias con mayor valor de negocio y menor complejidad relativa dentro de la Feature 1 (pago con tarjeta) y la atención al adulto mayor de la Feature 2, dejando el resto en el *Product Backlog* a la espera de futuras iteraciones. La Tabla 5.1 resume el estado inicial del tablero.

#### Tabla 5.1: Estado inicial del tablero Kanban
| Columna | Historias | Observación |
| :--- | :--- | :--- |
| **Product Backlog** | HU3, HU5, HU6, HU8, HU9 | DoR aprobado, en espera de priorización |
| **Sprint Backlog** | HU2, HU4, HU7 | Comprometidas para el Sprint 1 |
| **In Progress** | HU1 | En desarrollo por el equipo de pagos |
| **Testing/Review** | *Sin historias* | Sin historias en revisión aún |
| **Done** | *Sin historias* | Sin historias finalizadas aún |

El vínculo público al tablero digital (Trello/Jira/GitHub Projects) del equipo, con las nueve tarjetas cargadas y priorizadas, se encuentra disponible en: [https://trello.com/b/1WOVHA9n/tablero-kanban](https://trello.com/b/1WOVHA9n/tablero-kanban) *(reemplazar por el enlace real del espacio de trabajo del equipo antes de la entrega).*

---

## Capítulo 6: Estimación relativa en Puntos de Historia usando la Secuencia de Fibonacci modificada (Planning Poker)

Una vez que las historias superaron el filtro de DoR, el equipo realizó una sesión de *Planning Poker* para estimar su tamaño relativo en Puntos de Historia, utilizando la secuencia de Fibonacci modificada (1, 2, 3, 5, 8, 13, 20, 40, 100). Se definió como **historia pivote a HU7** (ver saldo antes de abordar el bus), por tratarse de la funcionalidad más simple y acotada, asignándole un valor de referencia de **2 puntos**. El resto de las historias fue comparado por analogía contra este pivote, discutiendo en rondas de votación simultánea hasta alcanzar consenso en cada carta.

La Tabla 6.1 resume el resultado final de la estimación para las nueve historias.

#### Tabla 6.1: Estimación relativa en Puntos de Historia (Planning Poker)
| ID | Historia de Usuario | Puntos |
| :--- | :--- | :--- |
| **HU1** | Pasajero paga el pasaje con la tarjeta | 3 |
| **HU2** | Chofer ahorra tiempo en la entrega de vueltos | 2 |
| **HU3** | Jefe de línea con historial contable digital | 5 |
| **HU4** | Tercera edad recarga en punto físico con efectivo | 5 |
| **HU5** | Usuario promedio recarga en efectivo en momentos críticos | 3 |
| **HU6** | Dueño de negocio como punto de carga con comisión y promociones | 8 |
| **HU7** | Usuario verifica saldo antes de abordar (pivote) | 2 |
| **HU8** | Usuario decide si recargar según umbral de saldo | 3 |
| **HU9** | Usuario controla sus finanzas con historial y reportes | 5 |
| | **Total** | **36** |

### Justificación de las estimaciones:
* **HU2 y HU7 (2 puntos):** Obtuvieron el puntaje más bajo por ser consecuencias directas o consultas simples sobre funcionalidad ya existente (HU7 funciona como pivote de comparación).
* **HU1, HU5 y HU8 (3 puntos):** Se estimaron en un nivel intermedio bajo por requerir validaciones adicionales sencillas (como saldos insuficientes o comparación con umbrales mínimos).
* **HU3, HU4 y HU9 (5 puntos):** Subieron a 5 puntos por integrar persistencia, manipulación de estados financieros complejos, emisión de comprobantes o reportes detallados, lo que añade complejidad en la capa de datos y presentación.
* **HU6 (8 puntos):** Se estimó como la más costosa al requerir el diseño de un nuevo módulo integral de comisiones y despliegue de promociones para el negocio comercial, quedando fuera del alcance transaccional básico del sistema. Las historias con DoR aprobado y puntaje asignado quedan listas para el Product Backlog.

---

## Capítulo 7: Conclusión

El desarrollo de esta actividad permitió al equipo transitar desde el Norte estratégico del Secuenciador de *Lean Inception* hacia un *Product Backlog* operativo, rebanando los tres requerimientos iniciales en nueve Historias de Usuario centradas siempre en el valor percibido por un rol real, sin caer en el sesgo técnico autorreferencial. La validación INVEST y la redacción de criterios de aceptación bajo la sintaxis Dado/Cuando/Entonces (BDD) permitieron transformar afirmaciones de negocio ambiguas en escenarios verificables, cubriendo tanto los flujos exitosos como los casos de excepción más relevantes.

Finalmente, el tablero Kanban y la estimación relativa mediante *Planning Poker* dieron gobernanza visual al backlog y evidenciaron que las historias con módulos de negocio adicionales, como la de comisiones y promociones, concentran una complejidad mayor que las simples consultas de saldo. En conjunto, el ejercicio demuestra que la gestión ágil de requisitos exige un proceso disciplinado de *slicing*, calidad (INVEST), criterios automatizables (BDD) y priorización visual, dejando al equipo listo para comprometer su primer Sprint.
