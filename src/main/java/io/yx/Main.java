package io.yx;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * @author YX
 * @date 2023/4/11 14:19
 */
public class Main {


    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rectangle = new Rectangle(screenSize);
        int port = 12030;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception ignore) {
            }
        }
        HttpServer httpServer = HttpServer.create();
        System.out.println("use port: " + port);
        httpServer.bind(new InetSocketAddress("0.0.0.0", port), 0);
        httpServer.createContext("/", exchange -> {
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            exchange.getResponseHeaders().add("content-type", "image/jpeg");
            exchange.getResponseHeaders().set("cache-control", "no-store");
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream responseBody = exchange.getResponseBody()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpeg", baos);
                responseBody.write(baos.toByteArray());
                exchange.close();
            }
        });

        httpServer.start();

    }


}
