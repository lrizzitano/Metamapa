# Desiciones de Diseño

## Índice

- [Usuario y Administrador](#usuario-y-administrador)
- [Hecho](#hecho)
- [Coleccion](#coleccion)
- [Solicitudes](#solicitudes)
- [Filtros](#filtros)
- [Fuente Estática](#fuente-estática)

## Usuario y Administrador

La clase base usuario define el comportamiento que todo usuario de la aplicación debe tener disponible, aún sin necesidad de registrarse, y es por eso que los campos asociados a ese registro son marcados como opcionales. \
La clase administrador hereda de éste y además añade comportamiento único de los usuarios administradores como crear Colecciones o aceptar Solicitudes.

### Contribuyente

El hecho de ser contribuyente es una diferencia conceptual y se representa únicamente como un atributo booleano, puesto que no hay comportamiento diferenciado para los contribuyentes. No tiene comportamiento extra, lo incluimos pensando en meta-data interesante de recolectar.

## Hecho

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
Pensamos en añadir un atributo referenciado a la fuente de la que se extrajo con el fin de poder reconocer mismos hechos provinientes de diferentes fuentes, finalmente desistimos porque no nos pareció importante, al menos para esta entrega.

### Origen

Aunque no exista comportamiento asociado al Origen, decidimos representarlo como ENUM para limitar su dominio ya que todo Hecho proviene de: DataSet , Manual (Subido pora administrador) , Contribuyente (Aporte de usuario)

### Implementación

Los hechos son definidos como record, esto simplifica los getters y constructor, además de realizar un Override interesante a Equals. \
Cómo los hechos no permaneceran en el sistema, sino que cada llamado a la fuente los generará devuelta, comparar por referencia no tiene sentido, por lo que un valor a valor como lo hace el record es lo más lógico.

## Coleccion

En las colecciones se define un criterio de pertenencia que se aplica como filtro a los hechos devueltos por las fuentes asociadas a la misma. Este filtro de pertenencia se “compone” con los filtros especificados por el usuario al hacer la consulta y con los hechos eliminados. Más información en la sección de Filtros  \
No existe almacenamiento para estas colecciones, la referencia debe ser guardada manualmente

## Solicitudes

El usuario emite una solicitud sobre un hecho, la misma guarda una referencia al hecho, y tiene un estado (pendiente, aceptado o rechazado) aunque veremos que eso es más conceptual. \
La solicitud se acuerda qué administrador la aceptó/rechazó, con propósito de rendición de cuentas.|
Sera el administrador quien rechazara o aceptara las solicitudes generadas por el usuario, quedando registrado y cambiando el estado de la solicitud segun sea la decision tomada.

### Singleton

Las solicitudes son almacenadas en un Singleton, este cuenta con 3 sets, uno para cada estado. Optamos por está solucion por la simpleza de tener las solicitudes separadas por listas e inferir su estado según en que lista esté. \
También consideramos que cada lista tenga su propia lista de solicitudes, solo teniendolas encuenta las que le son relevantes lo que agilizaría el proceso de búsqueda de hechos eliminados, pero esto es complejo de mantener, especialmente frente a un cambio de Criterio. \
Otra alternativa sería un único set con una variable estado para la Solicitud,descartamos esta opcion en favor del singleton por el momento, aunque en el futuro tal vez sea la adecuada.\
Entendemos que esta solución dada por un singleton es temporal, ya que cambiará con la implementación de la Base de Datos, pero mientras tanto esto nos pareció lo más práctico ya que es necesario persistir las solicitudes asi como los hechos asociados a las mismas.

## Filtros

Se utiliza el Predicate para los filtros, proveido por el lenguaje. Su uso se basa en 2 auxiliares.\
Esto permite que todo filtro tome un hecho y retorne un valor booleano que determina si ese hecho en particular "pasa" o no el filtro.

### Fábrica

Se creó un ENUM con ~~casi~~ todos los filtros que se pueden querer a utilizar, simplificando el proceso a un llamado con un único parámetro.

### Builder

Para poder mezclar filtros también tenemos un builder que va acumulando filtros a una lista para después reducirlos en AND u OR. Aunque no lo estamos utilizando directamente en ningún lado, y creemos que puede ser implementada directamente en el usuario.\
Este builder representa la posibilidad del usuario de crear sus propios filtro paso por paso, mediante el uso de los ENUMS, para luego aplicarlos en su busqueda.

## Fuente Estática

Ante cada consulta de hechos, se lee desde 0 el CSV, actualmente línea a línea aunque existiría la posibilidad de hacerlo todo de una, lo que consumiría más memoria a cambio de una ejecución más rápida. \
La fecha de carga del hecho siempre va a quedara con alguna inconsistencia ante las diferentes opciones que nos planteamos. Si le asignamos su fecha de carga como la fecha en la que se cargó la fuente al sistema, la misma quedará mal asignada para aquellos hechos que sean cargados posteriormente en una actualizacion del csv. Por otro lado, si la fecha toma la del momento en que se extrae de la fuente, esto no tendrá mucho sentido ya que podria generar inconsistencias entre un suceso pasado pero "cargado hoy". Fuimos por la segunda porque al menos siempre es un resultado posible, ya que la primera podría darnos un hecho de 2025 cargado en 2024 si actualizamos el CSV.

### Lectura del CSV

Una de las dificultades más grandes presentadas fue el formato de los CSV, el cual rara vez seá ideal. Ante esto se nos presentan 2 opciones. Por un lado, cargar a la fuente estática con una función de parseo, única a cada CSV. La otra opcion fue pre-procesar los archivos para que tengan el formato deseado. Actualmente implementamos la segunda opción. \
Sin importar la opcion elegida, los campos no serán 1 a 1 con el CSV, varios se fusionan para crear, por ejemplo, el título y descripción, mientras que las coordenadas podrían ser estimadas con otra información presente (una Api de google maps para ir de localidad a coordenadas).

#### Ejempo de Procesamiento

Para hacer una demostración de funcionamiento en la primera entrega, se eligió el dataset de incendios forestales en España. Se le realizara un preprocesamiento con un programa en java que lo adaptara al esquema de la fuente. Los paréntesis indican opcionalidad, si una fila no cumple con los campos oblitorios será eliminada.

- Título:  "id"  Incendio forestal en “ciudad” (de “superficie”)
- Descripcion: En la fecha “fecha” sucedio un incendio forestal de “superficie” mts2 en la ciudad de “ciudad”. (La causa (supuesta, segun el campo causa_supuesta) es “causa”, (“causa desc”). Este hecho dejo “muertos” muertos y “heridos” heridos)
- Categoria: Incendio Forestal
- Latitud: "latitud”
- Longitud:  “longitud”
- Fecha del hecho: “fecha”
