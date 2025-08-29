Análisis de Requerimientos
==========================

# Requerimientos Entrega 1

- Como persona administradora, deseo crear una colección.
- Como persona administradora, deseo poder importar hechos desde un archivo CSV. 
- Como persona visualizadora, deseo navegar todos los hechos disponibles de una colección.
- Como persona visualizadora, deseo navegar los hechos disponibles de una colección, aplicando filtros.
- Como persona contribuyente, deseo poder solicitar la eliminación de un hecho.
- Como persona administradora, deseo poder aceptar o rechazar la solicitud de eliminación de un hecho.

# Requerimientos Entrega 2

- Como persona contribuyente, deseo poder crear un hecho a partir de una fuente dinámica. 
- Como persona usuaria, quiero poder obtener todos los hechos de una fuente proxy demo configurada en una colección, con un nivel de antigüedad máximo de una hora.
- Como persona usuaria, quiero poder obtener todos los hechos de las fuentes MetaMapa configuradas en cada colección, en tiempo real.
- El Sistema debe permitir el rechazo de solicitudes de eliminación en forma automática cuando se detecta que se trata de spam. 

# Preguntas

## ¿A quienes rinde cuenta este sistema? ¿A quienes no? 

Teniendo en cuenta el contexto del sistema expuesto en la 1er entrega, "el sistema debe rendir cuentas a la sociedad toda, maximizando la disponibilidad y veracidad de la información, protegiendo la identidad de quienes lo visitan y suben información". Sin embargo, haciendo un análisis más específico del tema, llegamos a las siguientes conclusiones:

Analizandolo desde una perspectiva centrada en la "System Accountability" de Paul Dourish, el sistema rinde cuentas ante sus usuarios a través de las representaciones computacionales de la información presente en el sistema, como por ejemplo las solicitudes de eliminación y cambio, las cuales quedan registradas junto con el administrador responsable de su aprobación; esto permite tener una trazabilidad sobre decisiones importantes como la eliminación de un hecho del sistema.

Por otro lado, también existe una rendición de cuentas frente a las organizaciones que mantengan una instancia de MetaMapa a través de la información disponible a los administradores, la documentación de las decisiones de diseño que tomamos durante el desarrollo, la trazabilidad del código a través del sistema de versionado del proyecto o el origen de los requerimientos en los enunciados del trabajo práctico.

Respecto a las personas involucradas en los hechos presentes en el sistema, éste no rinde cuentas respecto del origen de los hechos que podrían contener información sensible, pero provee un mecanismo para solicitar la baja de los mismos en caso de que eso suceda.

## ¿Tiene este sistema problemas desde el punto de vista de rendición de cuentas? 

Para nosotros, el principal problema respecto de la rendición de cuentas en nuestro esta relacionado con la trazbilidad del origen de los hechos que figuran en el sistema. Debido a que éste se basa en la ideoloǵia de construcción de conocimiento colectivo, se vuelve complicado hacer un seguimiento preciso del origen de cada hehco, ya que pueden ser publicados por cualquier persona (incluso de manera anónima), y provenir de diferentes fuentes.

## ¿Hay partes interesadas que no hayan sido tenidas en cuenta en el diseño de este sistema?

Entendiendo como posibles partes interesadas en el sistema a las organizaciones que adminsitren sus instancias de MetaMapa y los usuarios que utilicen esas instancias creemos que ambas fueron tenidas en cuenta durante el proceso de diseño del sistema.

## En base a las preguntas anteriores, ¿hay requerimientos que convendría modificar o incorporar?

Si, para nosotros los siguientes requerimientos podrían mejorar la rendición de cuentas del sistema:

- El sistema debe almacenar el administrador responsable de aceptar la solicitud de eliminación de un hecho.
- El sistema debe almacenar el administrador responsable de aceptar la solicitud de cambio de un hecho.
- El sistema debe almacenar el administrador responsable de eliminar un hecho de una fuente dinámica.
- El sistema no debe compartir información personal sobre sus usuarios y sus solicitudes sin su consentimiento explícito.
- Como usuario de MetaMapa quiero poder eliminar todo registro de mis datos personales dentro de la aplicación.
- Como usuario de MetaMapa quiero poder saber el motivo de rechazo de mis solicitudes