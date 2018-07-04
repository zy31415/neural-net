import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.io.File

import javax.imageio.ImageIO
import mnist.data.DataReader
import mnist.plot.PlotUtils

val nth = 100
val imageData = DataReader.getImageData()(nth)

val image = PlotUtils.asBufferedImage(imageData)

// The required drawing location// The required drawing location

val drawLocationX = 300
val drawLocationY = 300

// Rotation information

val rotationRequired = Math.toRadians(45)
val locationX = image.getWidth / 2
val locationY = image.getHeight / 2
val tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY)
val op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR)

ImageIO.write(op.filter(image, null),"png",
  new File("workspace/image_rotation/rot.png"))

// Drawing the rotated image at the required drawing locations
//g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null)
