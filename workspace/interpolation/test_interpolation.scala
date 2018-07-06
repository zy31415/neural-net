import java.awt.image.{BufferedImage, ByteLookupTable, LookupOp}
import java.io.File

import javax.imageio.ImageIO
import mnist.ProjectConstant
import mnist.data.{DataReader, MyMnistReader}
import mnist.features.ImageRotator
import org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolator

import scala.collection.mutable.ArrayBuffer

val interpolator = new BicubicSplineInterpolator()

val nth = 100
val imageData = DataReader.getAllImageDataMatrix()(nth)

val grids = (-13 to 14).map(_.toDouble).toArray

val func = interpolator.interpolate(
  grids, grids,
  imageData.map(_.toDouble).grouped(ProjectConstant.ImageWidth).toArray)


val newImageData = ArrayBuffer[Double]()

val denseGrids = BigDecimal(-13) to 14 by 0.5 map(_.toDouble)

for(x <- denseGrids)
  for(y <- denseGrids)
    newImageData += func.value(x, y)

newImageData.foreach(println(_))

var image = new BufferedImage(
  55, 55, BufferedImage.TYPE_BYTE_GRAY)


for ((ele, i) <- newImageData.view.zipWithIndex)
  image.getRaster.getDataBuffer.setElem(i, Math.round(ele).toInt)

image = new LookupOp(
  new ByteLookupTable(0, (255 to 0 by -1).map(_.toByte).toArray),
  null)
  .filter(image, null)

ImageIO.write(image,"png",
  new File("workspace/interpolation/dense.png"))

