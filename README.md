# Getting Started

### Cómo lanzar el "bot" de marcadores del Among Us
En primer lugar generar el jar de la versión utilizando "mvn clean package".

En la carpeta target se generará un jar, en la raíz del proyecto hay un .cmd que al poner el jar en la misma carpeta y hacerle doble click, se abrirá una ventana de comandos con la que habrá que interactuar siguiendo las preguntas.

### Base de datos
docker run --name jaronesBD -p 3312:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7.26

Una vez creado el docker hay que crear la tabla jarones-bd



De Jarones para Jarones