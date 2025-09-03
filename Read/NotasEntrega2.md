Notas Correccion Entrega 2
==========================

# General

No hay una estructura **consistente** para inyectar dependencias (constructor, setter, etc).

# Fuente proxy calendarizada

El "*proxy*" en el nombre es redundante.

# Fuente Demo

No dejar de testear porque una conexion no es algo instanciable, se puede mockear.

# Filtros

Hay mucho acoplamiento entre los filtros y cada implementacion de fuentes proxy, cuidado con nuevas implementaciones de fuentes con otros diccionarios.

Se podría desarrollar un adapter a las nuevas interfaces de las fuentes.

Tambien se podría definir un record con los atributos concretos que definen la interfaz.

# Fuente Dinamica

Evitar la logica encadenada `hechos.contribuyente.contribuir()`

En vez de `agregarhecho()` se puede trasladar la logica semántica de la publicacion del hecho en un `hecho.publicar()`

Un objeto puede conocer su repositorio de forma "debil", por ejemplo a través de un singleton. Puede usarse para evitar una cadena de mensajes.

## Solicitud de cambio

Falta testear los metodos de solicitudes de cambio.

Crear una solicitud de cambio impacta en el sistema general, eso produce que no se puedan crear los objetos libremente (por ejemplo para testear), se podría separar la lógica compleja del constructor en un metodo más explícito.

# Detector de spam

No perder tiempo inyectando repositories que no tienen multiples implementaciones.

Evitar checkeos por null salvo buena justificacion.

Se podría guardar el corpus de datos ya vectorizado y quizas serializado para no tener que procesarlos ante cada instanciación del detector de spam.