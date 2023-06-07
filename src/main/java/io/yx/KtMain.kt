package io.yx

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.awt.GraphicsEnvironment
import java.awt.Rectangle
import java.awt.Robot
import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    var keMain = KtMain()
    keMain.main(args)
}

class KtMain {

    fun main(args: Array<String>) {

        // 处理缩放
        System.setProperty("sun.java2d.uiScale", "1")

        val allScreenBounds = getRectangle()
        val robot = Robot()


        val port = args.getOrElse(0) { 12030 } as Int;

        val httpServer = HttpServer.create()
        println("use port: $port")
        httpServer.bind(InetSocketAddress("0.0.0.0", port), 0)
        httpServer.createContext("/") { exchange: HttpExchange ->
//            Image image = robot.createMultiResolutionScreenCapture(allScreenBounds).getResolutionVariant(allScreenBounds.width, allScreenBounds.height);
            val bufferedImage = robot.createScreenCapture(allScreenBounds)
            exchange.responseHeaders.add("content-type", "image/png")
            exchange.responseHeaders["cache-control"] = "no-store"
            exchange.sendResponseHeaders(200, 0)
            exchange.responseBody.use { responseBody ->
                ByteArrayOutputStream().use { baos ->
//                ImageIO.write((BufferedImage) image, "png", baos);
                    ImageIO.write(bufferedImage, "png", baos)
                    responseBody.write(baos.toByteArray())
                    exchange.close()
                }
            }
        }

        httpServer.start()
    }

    private fun getRectangle(): Rectangle {
        var ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        var screens = ge.screenDevices
        val allScreenBounds = Rectangle()
        for (screen in screens) {
            val screenBounds = screen.defaultConfiguration.bounds
            allScreenBounds.x = Math.min(allScreenBounds.x, screenBounds.x)
            allScreenBounds.width += screenBounds.width
            allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height)
        }
        return allScreenBounds

    }

}