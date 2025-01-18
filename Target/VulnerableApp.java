import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class VulnerableApp {
    private static final Logger logger = LogManager.getLogger(VulnerableApp.class);

    public static void main(String[] args) throws Exception {
        System.out.println("Vulnerable Java Application is running on port 8080...");

        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Define a handler for incoming requests
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) {
                try {
                    String response;

                    // Check if the request method is POST
                    if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
    // Log headers for debugging
    StringBuilder headersLog = new StringBuilder("Request Headers:\n");
    exchange.getRequestHeaders().forEach((key, values) -> {
        headersLog.append(key).append(": ").append(String.join(", ", values)).append("\n");
    });
    logger.info(headersLog.toString());

    // Parse and log the POST body
    InputStream is = exchange.getRequestBody();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    StringBuilder body = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
        body.append(line);
    }
    reader.close();

    // Log the raw request body
    logger.info("Raw Request Body: " + body.toString());

    // Extract the email field
    String requestBody = body.toString();
    String email = null;
    if (requestBody.contains("email=")) {
        email = requestBody.split("email=")[1].split("&")[0];
        email = java.net.URLDecoder.decode(email, "UTF-8");
        logger.info("Email Field Logged: " + email);
    }

                        // Log the email field (if it exists)
                        if (email != null) {
                            logger.info("Email Field Logged: " + email);
                        }

                        // Respond with a confirmation
                        response = "Login processed. Check logs for email.";
                    } else { // Handle GET requests (and others)
                        // Log the User-Agent header
                        String userAgent = exchange.getRequestHeaders().getFirst("User-Agent");
                        if (userAgent != null) {
                            logger.info("User-Agent: " + userAgent);
                        }

                        // Send a simple response for GET requests
                        response = "HTTP server is running. Send a POST request with an email field or test User-Agent logging.";
                    }

                    // Send the response
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (Exception e) {
                    logger.error("Error handling request: ", e);
                    try {
                        exchange.sendResponseHeaders(500, 0);
                        exchange.getResponseBody().close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Start the server
        server.start();
    }
}
