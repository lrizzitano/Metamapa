# Notas correccion entrega 1 TPA

## Solicitudes

- Renombrar `solicitud` a `solicictudDeEliminacion` para que sea mas expresivo

### Singleton de solicitudes

- A las entidades que almacenan objetos de una cierta familia se los suele llamar repository
=> Renombrar el singleton a, por ejemplo, repositorioSolicitudes
(**Nombres en un solo idioma**, ingles solo si todos lo manejamos muy bien)
- Al no haber solicitud pidiendo guardar en colas la solucion de listas separadas suma un juego de colas extra
- La cola de rachazadas es innecesaria para los requerimientos actuales
(podría ser reemplazada por un conteo de solicitudes rechazadas?),
ademas, puede generar errores ante pedidos de solicitudes excesivos

## Usuario

Como no sabemos como se ejecuta el codigo, nos basamos en el modelo de dominio (paradigmas) 
- Aprovechar la delegacion de paradigmas y dejar la responsabilidad de cada entidad a su objeto
- No tomar decisiones por "la esencia" de las entidades (justificacion ontológica),
asignar responsabilidades estructurales en cada caso concreto segun:
  - Estado
  - Comportamiento
  - Polimorfismo

Separar las entidades segun estos criterios ayuda a la cohesion y disminuye el acoplamiento, 
las responsabilidades asignadas sin estos criterios terminan siendo arbitrarias. 

Si un metodo sigue funcionando al ser `static`, no depende del estado de la clase que lo ejecuta
y por lo tanto no tiene por que ser su responasbilidad (misplaced method).
Los objetos deben ser pequeños y solo hacer cosas vinculadas con su estado

## Administrador

- Metodos aceptar y rechazar solicitud redundantes
- Los roles son generalizaciones de personas humanas que usan el sistema,
- no tienen porque ser traducidos a objetos en el dominio (el dominio representa objetos, no actores)

### Objetos probablemente erroneos
- Objetos rol
- Objeto sistema (metamapa)

- ## No tiene que haber console logs durante la ejecución

## Formateador CSV

- Parametrizar los paths harcodeados en el codigo

## Tests

- El test integrador no corre con maven
- Poner las precondicones y postcondiciones en un before y un after
- Aserciones por mayor y menor poco específicas,
hay muchas maneras de pasar el test sin conseguir el resultado esperado

## filtros
- Usando parseo de variables perdemos checkeo de tipos, todo se convierte en string (compila filtros sin sentido),
se pueden usar tipos genericos pero no con enum. Se justifica por lo conciso de la solucoin actual
- Agregar filtros compuestos por `or` y `not`