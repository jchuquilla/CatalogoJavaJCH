package academy.aws;

import java.util.Map;

public final class DemoLocal {
    public static void main(String[] args) {
        var handler = new CatalogoHandler();
        String[] ids = args.length == 0 ? new String[]{"1", "99", "abc"} : args;

        for (String id : ids) {
            var evento = Map.<String, Object>of(
                "routeKey", "GET /productos/{id}",
                "pathParameters", Map.of("id", id)
            );
            var respuesta = handler.handleRequest(evento);
            System.out.printf("GET /productos/%s → HTTP %s%n%s%n%n",
                id, respuesta.get("statusCode"), respuesta.get("body"));
        }
    }
}
