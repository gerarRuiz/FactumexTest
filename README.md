# FactumexText

Aplicación desarrollada para la consultora Factumex, con propósito de obtener una nueva oportunidad laboral.

- ✨Kotlin
✨Importante, usar dispositivos con Android 11 o inferior para el correcto funcionamiento del deeplink

## Pantalla SplashScreen

- Creada para dar una vista más amigable
- Uso de la librería lottie para tener una animación más agradable.

![alt text](https://i.imgur.com/26Yl3thl.png)

## Pantalla Películas

- Creada para mostrar el listado de películas mejor votadas, próximos lanzamientos y las mas populares.
- Uso de remote mediator para paginar la información y realizar el almacenamiento offline.
- Al seleccionar una película serás llevado al detalle de la misma.

![alt text](https://i.imgur.com/qT9peZol.png)

## Pantalla Detalle Película

- Creada para mostrar el detalle de la información de la película.
- Se obtiene la información desde la base de datos.
- Al presionar el corazón será marcada como favorita y viceversa.
- Al presionar el botón de trailer cargar el trailer oficial en youtube, si es que cuenta con esta información.

![alt text](https://i.imgur.com/9sy5o3sl.png)

## Pantalla Trailer

- Creada para poder visualizar desde la app el trailer oficial de la pelicula, de otra manera no reproducira nada.

![alt text](https://i.imgur.com/SLuR5M7l.png)

## Pantalla Ubicaciones

- Creada para mostrar el historial de la ubicación del dispositivo.
- Al seleccionar una ubicación será enviado a un mapa con un marker en la dirección exacta.
- Los datos se actualizan en tiempo real.

![alt text](https://i.imgur.com/tx4q6K4l.png)
![alt text](https://i.imgur.com/2WzEaJwl.png)

## Pantalla Galería

- Creada para mostrar las imágenes que el usuario haya subido a cloud fire store, además de que puede subir más fotos desde ahí.
- Al hacer un long click dará la opción de eliminar la imagen.

![alt text](https://i.imgur.com/kvtaMAbl.png)
![alt text](https://i.imgur.com/7rV43s1l.png)

## Pantalla Perfil

- Creada para hacer login en el api de themoviedb.
- Poder visualizar la información de la cuenta.
- Cargar las películas que tenga guardadas en su lista de favoritos.
- Uso de deeplinks.

![alt text](https://i.imgur.com/iKeJvwil.png)

## Firebase cloud storage

- Evidencia de la información almacenada.

![alt text](https://i.imgur.com/BsgUt9yl.png)
![alt text](https://i.imgur.com/AhveEpql.png)

## Firebase fire store

- Evidencia de la información almacenada.

![alt text](https://i.imgur.com/O0jNTVMl.png)

## Mejoras

- Sin duda mejoraría la parte de los permisos, quitarlos del activity y dejarlos en mi base fragment.
- Arreglar los deeplinks para que funcione en Android 12+.
- Agregar deeplinks para las notificaciones y que así nos envié al mapa de google.
- Agregaría un broadcaste receiver para escuchar cuando el dispositivo cuando se reinicia y así poder volver a arrancar el servicio de actualización de ubicación.

![image](https://user-images.githubusercontent.com/20269109/203863634-7414836f-7401-4367-9b4a-2a69924cdbc7.png)
