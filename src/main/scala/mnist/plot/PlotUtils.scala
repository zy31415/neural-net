package mnist.plot

import java.awt.image.{BufferedImage, ByteLookupTable, LookupOp}
import mnist.data.MyMnistReader

object PlotUtils {

  def asBufferedImage(imageData: Array[Int]): BufferedImage = {
    val bufferedImage = new BufferedImage(
      MyMnistReader.imageWidth, MyMnistReader.imageHeight, BufferedImage.TYPE_BYTE_GRAY)
    setValue(bufferedImage, imageData)
    revertGrayScale(bufferedImage)
  }

  def asBufferedImage(imageData: Array[Double]): BufferedImage =
    asBufferedImage(imageData.map(Math.round(_).toInt))

  private def setValue(image: BufferedImage, imageData: Array[Int]): Unit =
    for ((ele, i) <- imageData.view.zipWithIndex)
      image.getRaster.getDataBuffer.setElem(i, ele)

  private def revertGrayScale(image: BufferedImage): BufferedImage =
    new LookupOp(
      new ByteLookupTable(0, (255 to 0 by -1).map(_.toByte).toArray),
      null)
      .filter(image, null)

}
