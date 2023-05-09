# Actividad de Aprendizaje de Programación de Servicios y Procesos
### SteamInfo
- Con esta aplicación podrás ver cuanto tiempo has dedicado en total a los juegos que tienes en Steam.
- Se muestra una lista con todos los juegos en propiedad con las horas jugadas a el. Puedes buscar un juego en especifico en el cuadro de busqueda.
- Al dar doble click a algun juego de la lista, se abre en detalle la informacion del mismo, junto a una lista de los logros si los tuviera.

### Requisitos obligatorios
- [x] La aplicación deberá utilizar técnicas de programación reactiva utilizando la librería RxJava en algún momento.
- [x] Se mostrará un listado de datos utilizando dos operaciones diferentes de la API.
- [x] Se mostrará información detallada de los items de los dos listados anteriores.
- [x] Todas las operaciones de carga de datos se harán en segundo plano y se mostrará una barra de progreso o similar al usuario.
- [x] Incorporar alguna operación de búsqueda o filtrado sobre los datos cargados de la API (búsqueda o filtrado que se hará desde la aplicación JavaFX, diferentes a las opciones de filtrado que permita la API).

### Otras funcionalidades
- [x] Cargar algún tipo de contenido gráfico a partir de información dada por la API (una foto, por ejemplo).
- [x] Permitir la exportación del contenido a un fichero CSV.
- [x] Implementar una funcionalidad que permite exportar algún listado (devuelto por alguna operación de la API) a un CSV y se comprima en zip (La idea es implementarlo usando CompletableFuture). Teneis aqui un tutorial sobre cómo comprimir en ZIP con Java.
- [ ] Crea, utilizando WebFlux, un pequeño servicio web relacionado con la API seleccionada y consúmelo desde alguna zona de la aplicación JavaFX utilizando WebClient.
- [x] Utiliza correctamente la clase ObservableList de JavaFX para la visualización de los contenidos en los diferentes controles de JavaFX que decidas utilizar (ComboBox, TableView, ListView, . . .).
- [x] Realizar el seguimiento del proyecto utilizando la plataforma GitHub para almacenar el código y gestionando las issues (bug, mejoras, . . .) a medida que se vaya trabajando en él.

### Como arrancar la app
- Necesitas tener instalado Maven y Java.
- Descarga todo el codigo desde Github y abrelo desde tu IDE favorito.
- Crea en la carpeta resources un archivo config.properties.
- Ejecuta la aplicacion desde Maven con javafx:run y añade "api.key=" y tu API_KEY de Steam.
- Introduce tu ID de Steam o URL personaliza y conoce cuantas horas has tirado de tu vida.