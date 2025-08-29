Decisiones de Diseño
====================

# Índice

- [Usuario y Administrador](#usuario-y-administrador)
- [Hecho](#hecho)
- [Colección](#colección)
- [Solicitudes](#solicitudes-de-eliminación)
- [Filtros](#filtros)
- [Fuente Estática](#fuente-estática)
- [Fuente Proxy](#fuente-proxy)
- [Fuente Demo](#fuente-demo)
- [Detector de Spam](#bonus-detector-de-spam)
- [Calendarización](#calendarizacion)
- [Algoritmos de Consenso](#algoritmos-de-consenso)
- [Agregadores](#agregadores)
- [Propuesta de Requerimiento](#propuesta-de-requerimiento)


# Usuario y Administrador

La clase base usuario define el comportamiento que todo usuario de la aplicación debe tener disponible, aun sin necesidad de registrarse, y es por eso que los campos asociados a ese registro son marcados como opcionales. \
La clase administrador hereda de este y además añade comportamiento único de los usuarios administradores como crear Colecciones o aceptar Solicitudes.

## Contribuyente

El hecho de ser contribuyente es una diferencia conceptual y se representa únicamente como un atributo booleano, puesto que no hay comportamiento diferenciado para los contribuyentes. No tiene comportamiento extra, lo incluimos pensando en meta-data interesante de recolectar.

# Hecho

Los hechos cuentan con 8 atributos:

- Titulo : String
- Descripción : String
- Categoría : String
- Latitud : Double
- Longitud : Double
- FechaAcontecimiento : LocalDate
- FechaCarga : LocalDate
- Origen : ENUM (Dataset, Manual, Contribuyente)
- contribuyente: Usuario
- multimedia: Path

Todos ellos son constantes, y por ahora obligatorios aunque en futuras entregas creemos que eso va a cambiar. \
Pensamos en añadir un atributo referenciado a la fuente de la que se extrajo con el fin de poder reconocer mismos hechos provenientes de diferentes fuentes, finalmente desistimos porque no nos pareció importante, al menos para esta entrega.

## Origen

Aunque no exista comportamiento asociado al Origen, decidimos representarlo como ENUM para limitar su dominio, ya que todo Hecho proviene de: DataSet, Manual (Subido por administrador), Contribuyente (Aporte de usuario)

## Implementación

Los hechos son definidos como record, esto simplifica los getters y constructor, además de realizar un Override interesante a Equals. \
Cómo los hechos no permanecerán en el sistema, sino que cada llamado a la fuente los generará devuelta, comparar por referencia no tiene sentido, por lo que un valor a valor como lo hace el record es lo más lógico.

# Colección

En las colecciones se define un criterio de pertenencia que se aplica como filtro a los hechos devueltos por las fuentes asociadas a la misma. Este filtro de pertenencia se “compone” con los filtros especificados por el usuario al hacer la consulta y con los hechos eliminados. Más información en la sección de Filtros \
No existe almacenamiento para estas colecciones, la referencia debe ser guardada manualmente

# Solicitudes de Eliminación

El usuario emite una solicitud sobre un hecho, la misma guarda una referencia al hecho, y tiene un estado (pendiente, aceptado o rechazado) aunque veremos que eso es más conceptual. \
La solicitud se acuerda qué administrador la aceptó/rechazó, con propósito de rendición de cuentas.|
Será el administrador quien rechazara o aceptara las solicitudes generadas por el usuario, quedando registrado y cambiando el estado de la solicitud según sea la decision tomada.

## Implementación del repository

Las solicitudes son almacenadas en un Singleton, este cuenta con 3 sets, uno para cada estado. Optamos por esta solución por la simpleza de tener las solicitudes separadas por listas e inferir su estado según en que lista esté. \
También consideramos que cada lista tenga su propia lista de solicitudes, solo teniéndolas en cuenta las que le son relevantes lo que agilizaría el proceso de búsqueda de hechos eliminados, pero esto es complejo de mantener, especialmente frente a un cambio de Criterio. \
Otra alternativa sería un único set con una variable estado para la Solicitud, descartamos esta opción en favor del singleton por el momento, aunque en el futuro tal vez sea la adecuada.\
Entendemos que esta solución dada por un singleton es temporal, ya que cambiará con la implementación de la Base de Datos, pero mientras tanto esto nos pareció lo más práctico, porque es necesario persistir las solicitudes asi como los hechos asociados a las mismas.

# Filtros

La Intefaz de filtro entiende 2 métodos, uno que la pasa a Predicate `<Hecho>` y otro a `Map<String, String>` para una query http. Este segundo método retornará un map vació si ese filtro no debe ser pasado, como por ejemplo un fitro que siempre es verdadero.

Los diferentes filtros que soportamos son:
- Por categoria: recibe string de categoria y filtra por igualdad
- Por hecho eliminado: Filtra por los hechos que han sido eliminados del sistema
- Por fecha: Filtra por la fecha de acontecimiento del hecho, pudiendo ser desde o hasta la fecha especificada
- Filtro null: Representa la ausencia de filtros
- filtro compuesto: Representa la concatenación de múltiples filtros de los tipos mencionados. 

# Fuente Estática

Ante cada consulta de hechos, se lee desde 0 el CSV, actualmente línea a línea aunque existe la posibilidad de hacerlo todo de una, lo que consumiría más memoria a cambio de una ejecución más rápida. Actualmente, priorizamos no traer todo a memoria de una, ya que las optimizaciones del Sistema Operativo y la biblioteca de lectura CSV optimizan bastante la ejecución. \
La fecha de carga se toma como la fecha de última modificación del archivo CSV, lo que resuelve las inconsistencias que podría dejar usar el LocalDate.now() al cargar (nos da siempre la fecha de la consulta, pues él parseo se hace cada vez que se requiere) y el hecho de usar una fecha fija de subida original del archivo (esto podía generar que al modificar el csv pudieran existir hechos con una fecha posterior a la de carga).

## Lectura del CSV

Una de las dificultades más grandes presentadas fue el esquema de los CSV, el cual rara vez seá ideal. Ante esto se nos presentan 2 opciones. Por un lado, cargar a la fuente estática con una función de parseo (que tomaría una línea del csv y devolvería un hecho armado), única a cada CSV. \
La otra opción fue pre-procesar los archivos para que tengan el esquema deseado. Actualmente, implementamos la segunda opción, ya que esto permite desacoplar el código de formateo del CSV dado y no le impone a la fuente estática la responsabilidad de encargarse de los diferentes esquemas de CSV. \
Este pre-proceso es una precondición para la fuente estática. Para cumplirlo, hay un componente que es el formateador, un script que crea un nuevo CSV y lo deja en el esquema que requiere la fuente estática.
Este script solo ha de ser corrido una vez y ya deja el CSV listo para la fuente estática (a diferencia de la primera opción que requiere de parsear el csv original ante cada consulta), y podría volver a correrse cada cierto tiempo para incorporar eventuales cambios en el CSV. \
Sin importar la opción elegida, los campos no serán 1 a 1 con el CSV, varios se fusionan para crear, por ejemplo, el título y descripción, mientras que las coordenadas podrían ser estimadas con otra información presente (una Api de Google Maps para ir de localidad a coordenadas).

### Ejemplo de Procesamiento

Para hacer una demostración de funcionamiento en la primera entrega, se eligió el dataset de incendios forestales en España. Se le realizara un preprocesamiento con un programa en java que lo adaptara al esquema de la fuente. Los paréntesis indican opcionalidad, si una fila no cumple con los campos obligatorios será eliminada.

- Título:  "id"  Incendio forestal en “ciudad” (de “superficie”)
- Descripción: En la fecha “fecha” ´sucedió un incendio forestal de “superficie” mts2 en la ciudad de “ciudad”. (La causa (supuesta, según el campo causa_supuesta) es “causa”, (“causa desc”). Este hecho dejo “muertos” muertos y “heridos” heridos)
- Categoría: Incendio Forestal
- Latitud: "latitud”
- Longitud:  “longitud”
- Fecha del hecho: “fecha”

# Fuente Proxy

Como existen fuertes diferencias entre cada fuente proxy, ya sea por que protocolo de comunicación usan, si son sincrónicas o no, etc. no existe una interfaz común que las unifique. Sin embargo, se diseñaron abstracciones que buscan reducir al mínimo la lógica que debe implementarse en cada adapter específico.

## Fuente Proxy Calendarizada

Esta clase representa una fuente proxy genérica que se actualiza de forma periódica y automática. Mantiene internamente un `Set<Hecho>` que va enriqueciendo con nuevos datos obtenidos desde la fuente remota.

Actua de observer frente al `ActualizadorCalendarizables`, cuando es notificada por el actualizador, actualiza sus hechos trayendo aquellos que no fueron cargados desde la ultima llamada.

Se define como una clase abstracta, para permitir extender este comportamiento a nuevas fuentes que se actualicen periódicamente, teniendo en cuenta que la única implementación concreta actual es una demo. Para utilizarla se debe implementar un único método abstracto:

``` java
protected abstract List<Hecho> getNewHechos(Instant ultimaLlamada);
```

Este método debe retornar una lista de hechos nuevos desde el momento de la última consulta. Si no hay más elementos, debe retornar null o una lista vacía.

### Fuente Demo

Simula una fuente conectada a una API ficticia llamada Conexión. Esta API devuelve hechos en forma de `Map<String, Object>`, los cuales son transformados a objetos Hecho. \
Esta clase implemente la interfaz `Calendarizable`, por lo que es suceptible de ser notificada por el `ActualizadorCalendarizables` y esta configurada para actualizar las fuentes una vez por hora.\

## Fuente MetaMapa

Para consumir la API REST de otra fuente MetaMapa, utilizamos la biblioteca Retrofit, la cual se encarga de gestionar de manera declarativa en el envío y recibimiento de HTTP request y HTTP response respectivamente. Para ello, fue necesario crear la interfaz `IMetaMapa`, la cual define los endpoints a consumir con sus respectivos verbos, paths y query parameters (los cuales se envían con `Map<String,String>` para aplicar múltiples filtros), siendo la clase `FuenteMetaMapa` quien utiliza la instancia de Retrofit para consumir la API. A su vez, esta biblioteca utiliza GSON para crear los JSON. GSON no puede parsear campos privados de clases internas como LocalDate y Path, por lo tanto tuvimos que inyectarle a nuestra instancia de Retrofit dentro de `ServicioMetaMapa` un `gson` especial, que utiliza las clases `PathAdapter` y `LocalDateAdapter` para manejar esos tipos de datos. 
Dotamos a la clase `ServicioMetaMapa` del parametro `urlAPI` para poder diferenciar las diferentes instancias de MetaMapa a las que estamos conectados.

Para el testeo utilizamos la biblioteca WireMock, la cual mockea una API REST levantando un servidor en puerto propio, con el fin de testear la funcionalidad del modulo completo.

# Fuente Dinamica

En esta entrega se solicito la implementacion de una fuente dinamica en la cual los usuarios podran cargar sus hechos.

Para ello creamos una clase que sera un SINGLETON ya que solo existira una fuente dinamica en todo el sistema.

La fuente dinamica necesitaba poseer una lista de hechos propia y una coleccion de solicitudes de cambio propia. 
Para resolver este requerimiento podriamos haber tenido en la propia clase la lista de Hechos y sus listas de solicitudes de cambio segun estado.
Sin embargo optamos por la utilizacion de un patron Repositorio para la implementacion de ambas. Por ello existen las interfaces: `SolicitudDeCambioRepository`
y `HechoRepository`. De esta forma la fuente dinamica se desacopla y desconoce el lugar donde se almacenan realmente las cosas, 
esto nos brinda mucha mas extensibilidad ya que a la hora de pasar al uso de la base de datos no habra que cambiar nada del codigo de la FuenteDinamica que ni se 
enterara de este cambio. 
Estos repositorios son inyectados por setters, debido a que la fuente es un singleton. Son intercambiables obviamente, por cualquier repositorio que implemente la interfaz requerida.

Existen por lo tanto la clase `HechosFuenteDinamica` que implementa la interfaz de `HechosRepository`, y que por el momento es donde se almacenan los hechos de la fuente dinamica.
Siguiendo esta logica, existe la clase `SolicitudesFuenteDinamica` que implementa la interfaz `SolicitudesDeCambioRepository`, alli se guardaran las solicitudes de cambio correspondientes a la fuente dinamica en memoria.

## Solicitud de cambio

Se requirio la posibilidad de solicitar cambios en un hecho subido anteriormente dentro de una fuente dinamica.

Para cumplir este requerimiento se creo la clase SolicitudDeCambio, la cual para ser creada, se debe llamar con un Usuario, un Hecho a modificar y una
modificacion que se representa como un nuevo Hecho. Para ser creado se chequea que no hayan pasado 7 dias, que el usuario este registrado y que quien solicita el cambio sea quien creo el hecho en primer lugar.

Esta solicitud puede ser aceptada, aceptada con sugerencias o rechazada por un administrador, el cual quedará ligado a ella como responsable.

Las sugerencias son modeladas con un String que es guardado en la misma solicitud, la solicitud tambien almacena el Usuario que la creo, asi que de ser necesario
se podria llegar facilmente a las solicitudes de un usuario especifico y ver las sugerencias que se le realizaron al mismo.

Las solicitudes tienen como atributo a la fuente dinamica que por el momento es la unica fuente que acepta estas solicitudes. Aceptar una solicitud de cambio, eliminara el Hecho a modificar y agregara
el nuevo. Rechazar la solicitud no realizara el cambio. En cualquier caso las solicitudes se almacenan.

# BONUS: Detector de Spam

Implementamos un detector de spam basico propio, sin recurrir a ningun servicio ni biblioteca externa. Este detector toma mensajes que se escriben como justificacion para una solicitud de eliminacion de un hecho \
y analiza si es spam o es una solicitud genuina. Para lograrlo, se aplica un proceso de vectorizacion de texto (llevar el texto a una repesentacion numerica dentro de un espacio vectorial) siguiendo el algoritmo TF-IDF \
para asignar a cada palabra dentro del texto un puntaje numerico que mide su importancia dentro del mismo. Luego, tras el proceso de vectorizacion se pasa a una etapa de clasificacion, para la cual previamente se tiene vectorizado \
un corpus de datos de ejemplo que viene etiquetado. De esa forma, podemos inferir si un mensaje es spam si "se parece" a otros mensajes que ya conocemos como spam. Para hacer esta clasficacion se utiliza el metodo KNN \
que consiste en, dado un conjunto de puntos categorizados en un espacio vectorial, asignarle a un nuevo punto, que queremos clasificar, la categoria correspondiente a la mayoria de los k puntos (vecinos) mas cercanos.

La decision de implementar un detector propio se basa principalmente en la seguridad de los datos y en la especificidad de la tarea, ademas de evitar dependencias externas que pueden no ser necesarias y que atarian \
el correcto funcionamiento de nuestro sistema al correcto funcionamiento y mantencion de sistemas externos.
Primeramente, utilizar un servicio completamente externo implicaria exponer todos los mensajes de las solicitudes de los usuarios lo que podria romper un vinculo de privacidad entre Metamapa y los mismos. Ademas, la \
tarea que se nos propone es muy especifica (eliminar spam de solicitudes de eliminacion de un hecho), diferentes a las mas clasicas filtraciones de spam en emails o SMS, y al estar aplicando tecnicas de aprendizaje supervisado \
la pertinencia de los datos que se usan como ejemplo es clave en el correcto funcionamiento del modelo; la utilizacion de un modelo propia permite ajustar estos datos de ejemplo como se quiera, actualmente usando uno de demostracion.

A nivel tecnico, se decidio usar el metodo TF-IDF para ponderar la importancia de las palabras en un texto ya que evalua la frecuencia de las mismas en el texto pero compensa penalizando a las palabras que son comunes en todos los textos \
dando un buen resultado para diferenciar palabras importantes de palabras que no lo son. Para la parte de clasificacion se decidio usar el metodo KNN por una cuestion de simplicidad de implementacion ya que es un detector basico, pero podria \
ser cambiado por otro mas efectivo en proximas iteraciones. Adicionalmente, podria pensarse en otro tipo de clasificadores, como uno probabilitisco, que no solo categorice sino que de un estimado de la probabilidad de cada categoria.
Si se decidiera a futuro mantener el detector propio, la mayor mejoria vendria de ampliar y curar el corpus de datos de ejemplo pudiendo incluso cruzar ejemplos curados (etiquetados por un administrador, por ejemplo) con otras instancias de Metamapa,
para generar una suerte de "repositorio de ejemplos de spam en solicitudes a metamapa" general, aplicando la idea de inteligencia colectiva ahora entre instancias de Metamapa.

# Calendarizacion

Decidimos implementar toda calendarizacion del sistema de manera externa, aprovechando el administrador de procesos Cron de los sistemas tipo Unix. Para lograr esto, decidimos declarar una interfaz ```Calendarizable``` que implementan todos los objetos que requieren ser actualizados, debiendo estos saber responder en que ```LocalDateTime``` es su proxima actualizacion y entender el mensaje ```actualizar()```. De esta manera, tendremos un punto central de acceso a los objetos que han de ser actualizados (```CalendarizablesRepository```) que conoce a todos los objetos calendarizables y puede retornar los objetos pendientes de ser actualizados en el momento en que se lo consulta. Este punto central de acceso es utilizado por un programa especial llamado ```ActualizadorCalendarizables``` que es ejecutado en intervalos regulares por Cron y les dice que se actualicen a todos los objetos que lo requieran. \
Decidimos hacerlo de esta manera para que tanto el Repositorio de objetos calendarizables como el actualizador puedan tratar de forma polimorfica a los objetos calendarizables, lo que permite tener un solo programa actualizador que no cambiara ante la incorporacion de nuevos eventos calendarizados. Esta solucion es muy flexible y permite agregar nuevos objetos que han de ser actualizados ante el paso del tiempo, haciendo que implementen la interfaz Calendarizable y agregandolos al repositorio ya mencionado. Incluso funcionaria tener objetos cuyos intervalos de tiempo no sean regulares, que calculen su momento de proxima actualizacion de forma dinamica cada vez que se actualiza. La restriccion que impone este diseño es que, al tener un solo programa actualizador, se vuelve necesario que el intervalo de tiempo con el que se calendariza este programa sea igual o menor al minimo de los intervalos de actualizacion de los objetos que actualiza. Si bien somos conscientes de esta restriccion, actualmente no hay grandes discrepancias entre los intervalos de los objetos que calendarizamos y nos parece razonable y mas simple hacerlo de esta manera, entendiendo que si a futuro hubiera un evento con una frecuencia de actualizacion muy diferente a las actuales, podria tener sentido hacer un programa actualizador aparte.

# Algoritmos de Consenso

Para incorporar la idea de hechos consensuados y un modo de navegacion curada, hicimos que las colecciones ahora puedan incorporar un ```CriterioConsenso``` y puedan devolver hechos en forma generica (tomando todos los de su fuente y aplicando su criterio de pertenencia) o de forma curada/consensuada (lo mismo pero agregando el criterio de consenso). El criterio de consenso se modela como un objeto inmutable que tiene un cache de hechos consensuados y un algoritmo en base al cual los determina; como este algoritmo se hace en base a las fuentes presentes en el nodo y los hechos que proveen, los hechos considerados consensuados varian en el tiempo, por lo que se calendariza esta actualizacion. De esta forma, cada coleccion tendra su criterio de consenso que entiende el mensaje ```esConsensuado(hecho)``` y le permite a la fuente filtrar sus hechos. \
Decidimos combinar el uso de una cache de hechos + calendarizar la actualizacion de la misma debido a que, como la aplicacion del algoritmo se hace en base a todas las fuentes del nodo, implica un gran costo computacional que no puede permitirse cada vez que se requiera ver los hechos curados de una coleccion. Al hacerlo de esta forma, se tiene todo el tiempo cacheado los hechos que se consideran consensuados, permitiendo filtrar rapidamente, y se actualizan en un momento de baja carga del sistema (3am). Ademas, el hecho de abstraer al criterio como un objeto en si mismo y hacerlo inmutable, permite que el nodo tenga una sola instancia de Criterio por cada algoritmo que se desee contemplar, de forma que todas las fuentes que decidan usar ese algoritmo para categorizar a sus hechos como consensuados tengan acceso al mismo cache y no tengan que tener un cache propio (donde estarian repetidos todos los hechos que se consideran consensuados, una vez por cada fuente que utilice ese algoritmo). De esta forma, logramos una eficiencia computacional (lo que se traduce en una rapida busqueda y navegacion en los hechos para la **persona visualizadora**) y una eficiencia en cuanto al uso de memoria, sin sacrificar flexibilidad, ya que las colecciones utilizan de forma indistinta el criterio que se le entregue (pudiendo este ser cambiado en tiempo de ejecucion) y un criterio puede crearse para un nuevo algoritmo que se incorpore al sistema, siempre y cuando este algoritmo implemente la interfaz ```AlgoritmoConsenso```.

# Agregadores

El servicio de agregacion (o en concreto, los agregadores, ya que podran instanciarse tantos como se quiera) fue modelado como una implementacion mas de ```Fuente```. Lo particular de esta implementacion de fuente es que tiene, a su vez, muchas ```Fuente```s, y lo que hace es, justamente, agregarlas. Ademas, como el agregador puede estar realizando operaciones sobre varias fuentes que se conectan sobre servicios externos y no se desea que cada consulta sobre el agregador las genere nuevamente, se cuenta con **copias locales de los hechos** que provienen de sus fuentes, que son actualizadas siguiendo la mecanica de calendarizacion descrita anteriormente, motivo por el cual los agregadores implementan, ademas de la interfaz fuente, la interfaz de calendarizable.
Se decidio hacerlo de esta manera ya que nos parecia deseable que las colecciones y las fuentes que teniamos hasta ahora siguieran funcionando sin tener que tocar su codigo y los agregadores sean un tipo de fuente mas, que puede ser tratado polimorficamente por las colecciones, y que modelan la idea de poder tomar hechos de varias fuentes a la vez. Ademas, tambien modelan la idea de poder contar con copias locales de hecho y calendarizar el pedido a las fuentes, lo que puede ser util si el **administrador de la infraestructura del sistema** quisiera darle a una coleccion una fuente que se conecta con un servicio externo pero que no le pida los hechos ante cada consulta a la coleccion, sino que pueda tener copias locales y calendarizar estos pedidos; el administrador podria envolver a la fuente que se conecta con el servicio externa en un agregador y resultar asi calendarizable.

# Propuesta de requerimiento:
- como **persona externa al sistema** quiero estar tranquilo que ninguna informacion personal sobre mi sera publicada en MetaMapa, esto incluye datos personales como mi nombre, edad, direccion o contacto, ademas de mi presencia en contendio multimedia como fotos o videos de mi cara sin censura.

se define como persona externa al sistema a toda aquella persona que no es ni visualizadora, ni contribuidora ni administradora de alguna parte del sistema.

Como propuesta de implementacion pensamos en utilizar un analizador de texto que detecte la presencia de datos personales en la descripcion de todo hecho que se ingrese al sistema y permita eliminarla. Esto en principio podria hacerse recurriendo a un sistema externo como Watson NLP de IBM (https://share.google/J3bG83v6b1TwmFJ47), que tiene funciones personalizadas para detectar este tipo de datos en texto. Esta herramienta permite usar modelos pre-entrenados con campos clasicos como email, numeros de telefono o direcciones, o fine-tunearlo para detectar campos mas especificos. En futuras iteraciones, si se decide seguir con este analizador, podria pensarse en una implementacion propia en lugar de recurrir a un servicio externo, de forma similar a lo que se hace con el tratamiento de Spam. Esta implementacion se basaria en tecnicas de procesamiento de lenguaje para detectar la presencia de, principalmente, nombres y direcciones.
De forma similar podria avanzarse y tambien pensar en _blurrear_ caras de los contenidos multimedia, usando bibliotecas especializadas como OpenCV (open source computer vision library).