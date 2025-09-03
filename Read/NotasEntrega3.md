Notas Entrega 3 TPA
===================

#  Calendarizable

No es recomendable harcodear el intervalo de tiempo de 5 minutos para actualizar.

Loguear en vez de usar println para poder mostrar por jeararquia.

Limitar la minima frecuencia de actualizacion al instanciar un objeto calendarizable para evitar configuraciones invalidas del sistema.

Documentar la granularidad de actualizaición del main (horas en este caso).

Importante recordar la responsabilidad de agregar las intancias calendarizables al repositorio, el acoplamiento existe implicita (como reglas de configuración que pueden documentarse) o explicitamente (se hace la lógica automáticamente en el constructor).

# Agregador

El set de hechos se llama copia local en vez de una cache.

# CriterioConsenso

El nombre de la clase debería ser consenso en vez de criterio de concenso.

# Fuente dinamica

Hacerlo singleton te limita a tener una sola instancia en toda implementación del sistema.

# Diagrama de despliegue

No se represetan las interfaces, solo las conexiones ente nodos y el protocolo con el que se comunican.

No hay una relacion uno a uno entre los nodos y los precesos, solo se grafica lo que se quiere comunicar.