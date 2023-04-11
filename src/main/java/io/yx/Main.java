package io.yx;

import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * @author YX
 * @date 2023/4/11 14:19
 */
public class Main {


    public static void main(String[] args) throws Exception {

        Rectangle allScreenBounds = getRectangle();
        Robot robot = new Robot();

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
            BufferedImage bufferedImage = robot.createScreenCapture(allScreenBounds);
            exchange.getResponseHeaders().add("content-type", "image/jpeg");
            exchange.getResponseHeaders().set("cache-control", "no-store");
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream responseBody = exchange.getResponseBody(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "jpeg", baos);
                responseBody.write(baos.toByteArray());
                exchange.close();
            }
        });

        httpServer.start();

    }

    /**
     * 获取所有显示器的宽高
     * 对于多显示器会拼接
     */
    private static Rectangle getRectangle() {
        // 获取多屏幕窗口大小
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        Rectangle allScreenBounds = new Rectangle();
        for (GraphicsDevice screen : screens) {
            Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
            allScreenBounds.width += screenBounds.width;
            allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);
        }
        return allScreenBounds;
    }


}
