package academy.aws;

import java.util.Map;

public final class CatalogoHandler {
    private static final Map<String, String> PRODUCTOS = Map.of(
        "1", """
            {"id":1,"nombre":"Teclado","precio":25}
            """.strip(),
        "2", """
            {"id":2,"nombre":"Mouse","precio":15}
            """.strip()
    );

    public Map<String, Object> handleRequest(Map<String, Object> evento) {
        // API Gateway coloca {id} en pathParameters; la consola conserva
        // eventos simples y puede enviar el ID en queryStringParameters.
        String id = leerId(evento, "pathParameters");
        if (id == null) {
            id = leerId(evento, "queryStringParameters");
        }

        if (id == null || !id.matches("[0-9]{1,10}")) {
            return respuesta(400, """
                {"error":"El ID debe contener de 1 a 10 dígitos"}
                """.strip());
        }

        String producto = PRODUCTOS.get(id);
        if (producto == null) {
            return respuesta(404, """
                {"error":"Producto no encontrado"}
                """.strip());
        }

        return respuesta(200, producto);
    }

    private static String leerId(Map<String, Object> evento, String nombreParametros) {
        if (evento == null || !(evento.get(nombreParametros) instanceof Map<?, ?> parametros)) {
            return null;
        }

        Object valor = parametros.get("id");
        return valor instanceof String id && !id.isEmpty() ? id : null;
    }

    private static Map<String, Object> respuesta(int estado, String cuerpo) {
        // En una integración proxy, body contiene el JSON en forma de texto.
        return Map.of(
            "statusCode", estado,
            "headers", Map.of("Content-Type", "application/json; charset=utf-8"),
            "isBase64Encoded", false,
            "body", cuerpo
        );
    }
}
