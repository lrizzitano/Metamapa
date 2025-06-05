Decisiones de Diseño
====================

# Índice

- [Usuario y Administrador](#usuario-y-administrador)
- [Hecho](#hecho)
- [Colección](#colección)
- [Solicitudes](#solicitudes)
- [Filtros](#filtros)
- [Fuente Estática](#fuente-estática)

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
- FechaCarga : LocalDate
- FechaAcontecimiento : LocalDate
- Origen : ENUM (Dataset, Manual, Contribuyente)

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

# Solicitudes

El usuario emite una solicitud sobre un hecho, la misma guarda una referencia al hecho, y tiene un estado (pendiente, aceptado o rechazado) aunque veremos que eso es más conceptual. \
La solicitud se acuerda qué administrador la aceptó/rechazó, con propósito de rendición de cuentas.|
Será el administrador quien rechazara o aceptara las solicitudes generadas por el usuario, quedando registrado y cambiando el estado de la solicitud según sea la decision tomada.

## Singleton

Las solicitudes son almacenadas en un Singleton, este cuenta con 3 sets, uno para cada estado. Optamos por esta solución por la simpleza de tener las solicitudes separadas por listas e inferir su estado según en que lista esté. \
También consideramos que cada lista tenga su propia lista de solicitudes, solo teniéndolas en cuenta las que le son relevantes lo que agilizaría el proceso de búsqueda de hechos eliminados, pero esto es complejo de mantener, especialmente frente a un cambio de Criterio. \
Otra alternativa sería un único set con una variable estado para la Solicitud, descartamos esta opción en favor del singleton por el momento, aunque en el futuro tal vez sea la adecuada.\
Entendemos que esta solución dada por un singleton es temporal, ya que cambiará con la implementación de la Base de Datos, pero mientras tanto esto nos pareció lo más práctico, porque es necesario persistir las solicitudes asi como los hechos asociados a las mismas.

# Filtros

Definimos un Filtro como un Predicate \<Hecho>, funcionalidad provista por el lenguaje. Su uso se basa en 2 auxiliares.\
Esto permite que todo filtro tome un hecho y retorne un valor booleano que determina si ese hecho en particular "pasa" o no el filtro.

## Fábrica de filtros

Se creó un ENUM con ~~casi~~ todos los filtros que se pueden querer a utilizar, simplificando el proceso a un llamado con un único parámetro.
Cada opción de filtro tiene asociada una ```funcionCreadora``` que es una función que toma un String (que representa el valor a comparar) y devuelve
(a través del llamado a una función auxiliar) el Filtro. \
El método ```crearFiltro``` recibe la String con el valor a utilizar y aplica la función creadora con la misma para armar el Filtro \
con la siguiente sintaxis: \
    ```Predicate<Hecho> filtro = Filtro.TIPO_DE_FILTRO.crearFiltro(valorComparado);``` \
    Ej: \
    ```Predicate<Hecho> filtro = Filtro.LONGITUD_MENOR.crearFiltro("-50");```

## Builder de Filtros Compuestos

Para poder mezclar filtros también tenemos un builder que va acumulando filtros a una lista para después reducirlos con AND. \
Este builder representa la posibilidad del usuario de crear sus filtros compuestos paso por paso, mediante la conjunción de filtros simples (provenientes del ENUM), para luego aplicarlos en su búsqueda.

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

Como existen fuertes diferencias entre cada fuente proxy, ya sea porque protocolo de comunicación usan, si son sincrónicas o no, etc. no existe una interfaz común que las unifique. Sin embargo, se diseñaron abstracciones que buscan reducir al mínimo la lógica que debe implementarse en cada adapter específico.

## Fuente Proxy Calendarizada

Esta clase representa una fuente proxy genérica que se actualiza de forma periódica y automática. Mantiene internamente un `Set<Hecho>` que va enriqueciendo con nuevos datos obtenidos desde la fuente remota.

Actua de observer frente al `ActualizadorFuentesCalendarizadas`, cuando es notificada por el actualizador, actualiza sus hechos trayendo aquellos que no fueron cargados desde la ultima llamada.

Se define como una clase abstracta, y para utilizarla se debe implementar un único método abstracto:

``` java
protected abstract List<Hecho> getNewHechos(Instant ultimaLlamada);
```

Este método debe retornar una lista de hechos nuevos desde el momento de la última consulta. Si no hay más elementos, debe retornar null o una lista vacía.

### Actualizador de Fuentes Calendarizadas

Oficia de _"subject"_ frente a las fuentes calendarizadas a través de una estructura similar al patrón observer mediante la cual el actualizador se encarga de llevar el control del tiempo transcurrido y notificar a todas las fuentes suscriptas a él cada vez que se cumple el intervalo de tiempo connfigurado.\
De esta manera, se delega la responsabilidad del manejo de las actualizaciones de las fuentes a un objeto central sin acoplamiento innecesario, ya que éste expone la interfaz necesaria para suscribir y desuscribir fuentes y opera polimorficamente sobre la lista de fuentes suscriptas a la hora de notificar el evento.\
Además, permite tener un manejo centralizado de los tiempos de actualización de las fuentes, permitiendo cambiarlo en tiempo de ejecución (reiniciando el timer interno pero manteniendo la lista de fuentes suscriptas) y evitando el procesamiento innecesario de tener un timer activo por cada fuente calendarizada del sistema.

De momento, ya que solo tenemos un tipo de fuente calendarizada, el sistema posee un solo actualizador como singleton que actualiza a todas las fuentes con una misma frecuencia configurable. Pero si en el futuro hubiera un requerimiento de actualizar diferentes fuentes con diferentes frecuencias se puede modificar para abandonar el patrón singleton y poder tener múltiples instancias de actualizadores con difetentes intervalos de tiempo.

### Fuente Demo

Simula una fuente conectada a una API ficticia llamada Conexión. Esta API devuelve hechos en forma de `Map<String, Object>`, los cuales son transformados a objetos Hecho. \
Esta clase hereda de `FuenteProxyCalendarizada`, por lo que es suceptible de ser notificada por el actualizador de fuentes calendarizadas que por defecto esta configurado para actualizar las fuentes una vez por hora.\
Sin embargo, sí recibe por inyección la dependencia Conexión, lo que permite desacoplar la lógica de obtención de datos del mecanismo de actualización automática.

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

A nivel tecnico, se decidio usar el metodo TF-IDF para ponderar la importancia de las palabras en un texto ya que evalua la frecuencia de las mismas en el texto pero comprensa penalizando a las palabras que son comunes en todos los textos \
dando un buen resultado para diferenciar palabras importantes de palabras que no lo son. Para la parte de clasificacion se decidio usar el metodo KNN por una cuestion de simplicidad de implementacion ya que es un detector basico, pero podria \
ser cambiado por otro mas efectivo en proximas iteraciones. Adicionalmente, podria pensarte en otro tipo de clasificadores, como uno probabilitisco, que no solo categorice sino que de un estimado de la probabilidad de cada categoria.
Si se decidiera a futuro mantener el detector propio, la mayor mejoria vendria de ampliar y curar el corpus de datos de ejemplo pudiendo incluso cruzar ejemplos curados (etiquetados por un administrador, por ejemplo) con otras instancias de Metamapa,
para generar una suerte de "repositorio de ejemplos de spam en solicitudes a metamapa" general, aplicando la idea de inteligencia colectiva ahora entre instancias de Metamapa.