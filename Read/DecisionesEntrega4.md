# Decisiones


## Cambios en clases por`Persistencia`

Debido a la necesidad de pasar nuestro modelo de clases a uno persistible,
nos vimos en la necesidad de realizar cambios en el mismo

Para empezar, tuvimos que cambiar la Interfaz Fuente, Consenso y Filtro a una clase abstracta.

En los tres casos decidimos implementar una estrategia de Single table, debido a que
este metodo favorece enormemente el uso de consultas polimorficas, las cuales son las
que principalmente hace nuestro sistema


## Hechos
En todas nuestras entregas pasadas, el Hecho se mantuvo como un Record, esto nos
facilitaba la comparacion de dos hechos iguales atributo por atributo.

Lamentablemente tuvimos que cambiar este Hecho para que sea una clase como las demas. De
este modo es posible para JPA poder persistirlo

### Identidad de un Hecho
En las entregas pasadas, para comprar un Hecho con otro en igualdad, se realizada mediante todos sus atributos, por lo tanto dos hechos eran iguales si todos sus atributos lo eran.

En esta entrega decidimos que lo que define a un Hecho (lo identifica en todo el sistema) es su titulo. Entonces dos hechos son iguales si tienen el mismo titulo. Lo cual es principalmente util para las solicitudes, las cuales pueden guardar simplemente el Titulo del Hecho.

Tambien consideramos que esta igualdad sea dada por Titulo + Provincia en una entrega posterior

### Ubicacion del Hecho
Anteriormente el Hecho poseia dos atributos que definian su ubicacion: su longitud y latitud. Esta entrega, se agrego la nocion de Provincia.

Por lo tanto, decidimos abstraer estos tres atributos en una clase Ubicacion, la cual contiene latitud, longitud y Provincia (que es un enum).

Otro problema surgido por el atributo Provincia es que esta, podia ser calculada mediante su latitud y longitud, lo cual era crucial para la generaciond e estadisticas por ejemplo.
Para resolver este dilema y utilizar la latitud y longitud se determino que el atributo Provincia podia:
- Ser ingresado por el usuario
- Ser calculado mediante el uso de una API

En el caso de que la persona que cree el Hecho provea una latitud y longitud, pero NO una Provincia, se usa una API localizadora de Google para determinar a que Provincia pertenecen estas coordenadas.
Para cubrir el caso de error, podria setearse Provincia desconocida.

Esta abstraccion Ubicacion nos permite asi desacoplar los Hechos de su Ubicacion y la forma de obtener esta misma mediante la API.

Para esta API definimos una interfaz comun en caso de tener que cambiar la misma o en el futuro definir la determinacion de la Provinicia de forma estatica.


## Solicitud de eliminacion
Tuvimos un dilema generado por la necesidad de persistir las solicitudes de eliminacion
debido a que estas, en un principio tenían un atributo Hecho, lo generaba una inconsistencia en la
base de datos debido a que SOLO se persisten los hechos pertenecientes a las Fuentes dinamicas

Finalmente, la solicitud de eliminacion ahora contiene simplemente un atributo Titulo,
que es el Titulo del hecho que fue solicitado para eliminarse


## Rechazo de solicitud
Para manejar aquellas solicitudes que fueron Rechazadas y no persistir estas solicitudes que tal vez fueron rechazadas por spam y estarian ocupando gran espacio innecesariamente creamos las clases RechazosDeEliminacion y RechazosDeCambio

- RechazosDeEliminacion

Esta clase guarda en su interior simplemente el titulo del Hecho, y dos contadores con la cantidad de veces que fue solicitada su Eliminacion y la cantidad de veces que esta solicitud fue Rechazada por el detector de spam

- RechazosDeCambio

En cambio, esta clase guardara un Hecho completo, ya que no existe inconsistencia en Apuntar un Hecho de la fuente dinamica. A su vez guardara un atributo que sera un contador para determinar la cantidad de veces que se rechazo la solicitud de cambio de ese Hecho


Estas clases nos permiten guardar una sola entidad con contadores en lugar de una solicitud diferente por cada solicitud rechazada.


## Repositorios
Anteriormente poseiamos repositorios en memoria, representados por listas de elementos, de Fuentes, Hechos, Solicitudes de eliminacion y Solicitudes de cambio

Para esta entrega cambiamos estos repositorios por lo pertinentes en Base de datos. Estos repositorios ya no son mas singletons y sus metodos comunicacion directamente a la base de datos definida.

Se sumaron a esta entrega la existencia de los repositorios de Colecciones y de Usuarios y de los Datos generados por el Estadistico.

Para manejar estos repositorios se definieron interfazes que los mismos deben cumplir. Para quellos repositorios que solo realizaban operaciones CRUDE, definimos un Repositorio generico


## Componente estadistico
La generacion de estadisticas se hace mediante un componente calendarizable llamado RecolectorDeInformacion. Este componente tiene dos listas polimorficas: una de de objetos de estudio y otra de publicadores de resultados. Los objetos de estudio son objetos capaces de generar estadisticas sobre algun eje particular; en principio, solicitudes, colecciones y categorias. Estos objetos de estudio devuelven una lista de objetos de tipo Resultado Estadistico, que saben como exportarse su informacion en formato Map<String, String> y que pueden ser persistidos. Esto permite exportarlo a una base de datos o a un archivo, ya sea tipo CSV o JSON. Esta exportacion se realiza a traves de los componentes de tipo PublicadorDeResultados, que son polimorficos y saben exportar ResultadosEstadisticos. Para hacer uso de esta informacion se cuenta con un componente Estadistico que usa la informacion persistida en base de datos para responder algunas preguntas interesantes.

## Full text search

Para esta entrega implementamos la funcinalidad de buscar hechos por similitud del título y descripción respecto a un término de búsqueda, tokenizando el texto presente en el título y descripción de cada hecho e indexando los hechos presentes en el sistema por estos tokens para poder hacer una búsqueda de hechos a partir de una palabra (Full-Text Search). Para esto, implementamos dos estrategias diferentes según el origen del cual la fuente recibe los hechos:

- Para la fuente dinámica decidimos utilizar la funcionalidad de Full-Text search nativa de MySQL. Para esto, agregamos índices de tipo FULLTEXT en las columnas de la tabla Hecho sobre las que querremos hacer la búsquedas (título y descripción). Luego, implementamos un método `fullTextSearch(String texto)` en el repositorio de hechos de la fuente dinámica para traer los hechos mediante el operador MATCH-AGAINST, que realiza la búsqueda sobre la tabla por el término pasado por parámetro.
  
- Para el resto de las fuentes, en cambio, decidimos implementar un mecanismo similar pero en memoria usando la librería Lucene, ya que el resto de las fuentes (Estática, MetaMapa y Proxy) traen sus hechos de diferentes orígenes y deben realizar la búsqueda por texto sobre la lista de hechos presente en memoria. Para esto, implementamos un método `fullTextSearch(String busqueda, Set<Hecho> hechos)` que dado el set de hechos proveniente de `obtenerHechos(Filtro filtro)` (método abstracto que cada fuente implementa según su origen), usa los módulos de Lucene para generar un índice de todos los hechos del set por su título y descripción y luego lee ese índice para buscar los 10 hechos que coincidan en mayor medida con el término de búsqueda usado. Decidimos no reutilizar este índice creado y volver a crearlo cada vez que se le hace un nuevo pedido de hechos a la fuente ya que éste es totalmente dependiente de los hechos que vengan del método `obtenerHechos(Filtro filtro)`, que cada subtipo de fuente implementa de manera diferente y no hay una manera sencilla y eficiente de distinguir qué hechos se mantienen respecto de la petición anterior. 