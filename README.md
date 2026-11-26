# Catálogo de productos en Java 21

En el Día 1 vas a descargar, compilar y ejecutar el catálogo en tu computadora. La demo consulta dos productos de ejemplo en memoria. Mañana usarás la misma función en AWS Lambda.

## 1. Descarga y extrae el ZIP

Descarga [catalogo-java.zip](https://aws-developer-academy-liard.vercel.app/materiales/catalogo-java.zip).

- **Windows.** Haz clic derecho sobre el ZIP en Descargas, elige **Extraer todo** y selecciona una carpeta llamada `catalogo-java`.
- **macOS.** Haz doble clic en el ZIP desde Finder y abre la carpeta extraída.
- **Linux.** Usa **Extraer aquí** o ejecuta `unzip catalogo-java.zip -d catalogo-java` desde la carpeta de la descarga.

En la raíz del proyecto extraído debes ver `README.md` y la carpeta `src`.

## 2. Verifica el JDK 21

Abre PowerShell en Windows o Terminal en macOS/Linux. Ejecuta una línea a la vez.

```sh
java -version
javac -version
```

Ambos deben mostrar una versión `21.x`. Si falta un comando o sale otra versión, instala el [JDK 21 de Eclipse Temurin](https://adoptium.net/temurin/releases/?version=21). Selecciona **JDK**, tu sistema operativo y la arquitectura de tu equipo. En Windows usa el instalador `.msi` cuando esté disponible y conserva la opción de añadir Java al `PATH`. En macOS usa el `.pkg` para Apple Silicon (AArch64) o Intel (x64), según tu Mac. En Linux sigue las [instrucciones para tu distribución](https://adoptium.net/es/installation). Cierra y vuelve a abrir la terminal y repite los dos comandos.

## 3. Abre la terminal en la carpeta del proyecto

En **Windows**, abre la carpeta extraída en el Explorador, escribe `powershell` en la barra de direcciones y presiona Enter. El último comando debe devolver `True`.

```powershell
Get-Location
Test-Path ./src/academy/aws/CatalogoHandler.java
```

En **macOS/Linux**, abre la terminal y usa `cd` con la ruta de tu carpeta extraída. Ajusta el ejemplo a tu equipo. El último comando debe mostrar `CatalogoHandler.java` y `DemoLocal.java`.

```sh
cd "$HOME/Downloads/catalogo-java"
pwd
ls src/academy/aws
```

## 4. Compila y ejecuta

Desde la raíz del proyecto, ejecuta estos mismos dos comandos en Windows, macOS o Linux.

```sh
javac -encoding UTF-8 -d target/classes src/academy/aws/CatalogoHandler.java src/academy/aws/DemoLocal.java
java -cp target/classes academy.aws.DemoLocal 1 99 abc
```

La compilación crea `target/classes`. Si sale bien, la terminal vuelve al prompt sin imprimir nada. La ejecución muestra este resultado:

```text
GET /productos/1 → HTTP 200
{"id":1,"nombre":"Teclado","precio":25}

GET /productos/99 → HTTP 404
{"error":"Producto no encontrado"}

GET /productos/abc → HTTP 400
{"error":"El ID debe contener de 1 a 10 dígitos"}
```

| Si ves este mensaje | Comprueba lo siguiente |
|---|---|
| `file not found` | Vuelve al paso 3 y confirma que la terminal está en la carpeta extraída que contiene `src`. |
| `Could not find or load main class` | Revisa que el primer comando terminó correctamente y ejecuta el segundo desde la raíz del proyecto. |
| `UnsupportedClassVersionError` | Comprueba que `java -version` y `javac -version` muestran 21. Cierra y vuelve a abrir la terminal después de instalar el JDK 21. |

## 5. Lee el código y prueba otro ID

1. Abre `src/academy/aws/DemoLocal.java` en tu editor. Identifica dónde se prepara el evento para cada ID.
2. En la misma carpeta, abre `CatalogoHandler.java`. Sigue el ID desde `pathParameters` hasta la búsqueda en `PRODUCTOS` y el JSON de salida.
3. Ejecuta `java -cp target/classes academy.aws.DemoLocal 2` y comprueba que devuelve `HTTP 200` con la ficha del Mouse. Conserva la carpeta para la práctica de mañana.

## Desplegar en AWS en la práctica posterior

1. Crea una función Lambda con runtime **Java 21**, tipo de paquete **ZIP** y un rol de ejecución básico autorizado para escribir registros en CloudWatch.
2. En la función, abre **Código → Cargar desde → Archivo .zip o .jar** y selecciona `catalogo-lambda.jar`. En la configuración del runtime, establece el handler `academy.aws.CatalogoHandler::handleRequest`.
3. Desde **Probar** en Lambda, copia el contenido de los archivos de la carpeta `eventos` y ejecuta los tres casos. Comprueba `statusCode` y `body` en el resultado. Esta prueba funciona antes de crear API Gateway.
4. Cuando se habilite la práctica de API Gateway, crea una **HTTP API** con una integración Lambda proxy de formato **2.0**. Asocia la ruta `GET /productos/{id}` a la función y prueba la URL del stage añadiendo `/productos/1`, `/productos/99` y `/productos/abc`.

El ZIP del material contiene el proyecto y el JAR compilado. Para Lambda se carga el JAR que está dentro del ZIP.

- [Empaquetado y carga de funciones Java](https://docs.aws.amazon.com/es_es/lambda/latest/dg/java-package.html)
- [Configuración del handler Java](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)
- [Integración de Lambda con HTTP API](https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-develop-integrations-lambda.html)
