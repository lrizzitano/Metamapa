# Metamapa: Sistema de gestión de mapeos colaborativos
Este proyecto grupal se realizó en el contexto de un trabajo práctico para la materia Diseño de Sistemas de Información de la UTN FRBA.

### Integrantes
- Juan Manuel Rama
- Pablo Gabarini
- Lautaro Melchiori
- Tadeo Vazquez Cañoto
- Lautaro Rizzitano

## Descripción
Metamapa es un sistema descentralizado que permite la recopilación, visibilización y mapeo colaborativo de información, rindiendo cuentas a la sociedad toda, maximizando la disponibilidad y veracidad de la información y protegiendo la identidad de quienes lo visitan y suben información.

Este está diseñado para que diferentes ONG, universidades u organismos estatales puedan instalarlo en sus servidores, gestionarlo y ofrecerlo para sus comunidades, en forma de instancias particulares.

Cada instancia de MetaMapa puede contar con una o más fuentes de datos, servidas desde diferentes nodos, en las que se almacenará la información disponible en sí. Esta información puede ser redundante, por lo que posee herramientas de etiquetado que permitan vincular la información y generar consenso sobre la misma.

Algunas de estas fuentes de datos son estáticas, es decir, proveerán información obtenida directamente de datasets conocidos. Sin embargo, MetaMapa también permite que cualquier persona mayor de edad pueda subir piezas de información (llamadas hechos), ya sean descripciones textuales, imágenes o videos. Para esto, también existen fuentes de datos dinámicas, que permitirán que las personas carguen colaborativamente nuevas piezas de información. Además, existen otro tipo de fuentes llamadas proxy, encargadas de vincularse con servicios externos u otras instancias de Metamapa para exponer los hechos que estas proveen a través de una API.

También se generan periódicamente estadísticas sobre el uso del sistema. Cada cierto intervalo de tiempo, se recalculan estas estadísticas y se actualizan sus resultados en la interfaz. Algunas de estas estadísticas son visibles para el público en general, y otras sólo para las personas administradoras.

## Deploy
Para la entrega del TP se realizó un deploy usando Amazon Web Services.

Actualmente solo se puede acceder a otro deploy realizado sobre un Home Server en [metamapa.lrizzitano.com.ar](https://metamapa.lrizzitano.com.ar)

## Tecnologías Utilizadas
### Backend

* **Java 17**
* Javalin – framework web para manejo de rutas HTTP
* Handlebars – renderizado de vistas server-side
* java-jwt – autenticación basada en tokens

### Persistencia

* **JPA**
* HSQLDB – base embebida para desarrollo/testing

### APIs y procesamiento de datos

* Retrofit – consumo de APIs externas
* Gson – serialización/deserialización JSON
* OpenCSV – procesamiento de archivos CSV

### Frontend

* **HTML / CSS / JAVASCRIPT**
* **HTMX** – interacción dinámica con el backend sin SPA

### Mapas

* MapLibre – visualización de mapas interactivos
* MapTiler – estilos y tiles del mapa

### Testing

* JUnit
* Mockito
* WireMock – simulación de servicios HTTP

## Capturas de Pantalla
<img width="1900" height="992" alt="Screenshot 2026-03-10 at 00-41-37 " src="https://github.com/user-attachments/assets/c4fe06fd-4b34-4ef8-8f1c-aa51034fa466" />

<img width="1900" height="992" alt="Screenshot 2026-03-10 at 00-42-55 " src="https://github.com/user-attachments/assets/c33456c3-d638-4c5d-8947-8219a4036376" />

<img width="1900" height="992" alt="Screenshot 2026-03-10 at 00-43-03 " src="https://github.com/user-attachments/assets/93a5a989-63af-497b-a68a-8e196840c66a" />

## Diagramas
### Clases - Modelo de Dominio
<img width="1698" height="954" alt="diagrama-clases" src="https://github.com/user-attachments/assets/48e9aef7-a666-4cea-8fe1-4f3d8549c390" />

### Relacional - Modelo de Persistencia
<img width="1340" height="918" alt="diagrama-relacional" src="https://github.com/user-attachments/assets/6b0388db-839a-4c12-aa91-77166747ee8f" />

### Despliegue
<img width="734" height="375" alt="diagrama-despliegue" src="https://github.com/user-attachments/assets/2868b401-e0e6-4efd-8937-a73a8bab1924" />
