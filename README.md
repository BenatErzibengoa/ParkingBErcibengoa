# Descripción
Esta aplicación ha sido desarrollada en el aula de empresa de LKS NEXT en colaboración con la UPV/EHU, dentro de la Facultad de Informática.
Su propósito es permitir a los usuarios reservar plazas en un parking.

El usuario deberá registrarse para poder realizar cualquier acción. Una vez registrado, puede añadir  sus vehículos y realizar reservas a nombre de cada uno de ellos.
La interfaz gráfica de la aplicación muestra visualmente las plazas disponibles dentro del aparcamiento, clasificadas por tipo:
- Plaza para coche
- Plaza para vehículo eléctrico
- Plazas reservadas para personas con discapacidad
- Plaza para moto

La aplicación permite al usuario consultar sus reservas activas y revisar el historial de reservas realizadas en los últimos 30 días. 
Además, la aplicación envía notificaciones al usuario 30 minutos y 15 minutos antes del inicio de cada reserva.

# Tecnología
La aplicación fue diseñada inicialmente en Figma, donde se definió la interfaz de usuario con un enfoque centrado en la experiencia del usuario y la claridad visual. Una vez creado el diseño en Figma, se ha utilizado Android Studio como entorno de desarrollo, Java como lenguaje principal y XML para la interfaz gráfica. 

La autenticación de usuarios se gestiona con Firebase Authentication, permitiendo un inicio de sesión seguro y confiable. Toda la información de usuarios, vehículos, reservas y plazas se almacena de forma segura en la nube mediante Cloud Firestore de Firebase. 

Para garantizar la calidad del código, se ha integrado SonarQube, lo que permite realizar un análisis estático del código y detectar posibles errores, duplicaciones o malas prácticas. Además, se han creado tests unitarios con JUnit para asegurar el correcto funcionamiento de la lógica de negocio.


 
# Capturas de pantalla
Estas son algunas capturas de pantalla de las funcionalidades principales de la aplicación:

## Login
![Screenshot_20250706_221342](https://github.com/user-attachments/assets/841f5e76-c7a2-4698-b5e1-9d3cbd5ae7f6)

## Mis Reservas
![Screenshot_20250706_221743 (1)](https://github.com/user-attachments/assets/5ab5d1c1-7949-4817-8937-b4c155dce031)


## Seleccionar Plaza
![Screenshot_20250706_223027](https://github.com/user-attachments/assets/d6560fea-2c06-4dce-8219-a2ad32eac74a)

# Acceso de prueba y configuración

Esta aplicación guarda los datos en **Cloud Firestore**. Para facilitar que otras personas puedan probar la app sin necesidad de realizar configuraciones adicionales, se ha dejado la **API key** incluida en el proyecto.

Además, se proporciona una **cuenta de prueba** para que puedas comprobar el funcionamiento de la sección del historial de reservas:

- **Correo electrónico:** `bengoaerzi@gmail.com`  
- **Contraseña:** `admin1234`
