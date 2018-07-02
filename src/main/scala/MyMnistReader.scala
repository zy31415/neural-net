import java.awt.image.{BufferedImage, ByteLookupTable, LookupOp, ShortLookupTable}

import mnist.MnistReader

import scala.collection.JavaConverters._

object MyMnistReader {

  def imagePath = getClass.getResource("train-images-idx3-ubyte").getPath
  def labelPath = getClass.getResource("train-labels-idx1-ubyte").getPath

  def numImages = imageData.length
  def imageHeight = imageData.head.length
  def imageWidth = imageData.head.head.length

  private var _images: List[Array[Array[Int]]] = _
  private var _labels: Array[Int] = _

  def imageData: List[Array[Array[Int]]] = {
    if (_images == null) {
      _images = MnistReader.getImages(imagePath).asScala.toList
    }
    _images
  }

  def labels: Array[Int] = {
    if (_labels == null) {
      _labels = MnistReader.getLabels(labelPath)
    }
    _labels
  }

  def images (nth: Int): BufferedImage = {
    val bufferedImage = new BufferedImage(
      MyMnistReader.imageWidth, MyMnistReader.imageHeight, BufferedImage.TYPE_BYTE_GRAY)

    setValue(bufferedImage, nth)
    revertGrayScale(bufferedImage)
  }

  private def setValue(image: BufferedImage, nth: Int) = {
    var i = 0
    for {
      row <- MyMnistReader.imageData(nth)
      ele <- row
    } {
      image.getRaster.getDataBuffer.setElem(i, ele)
      i += 1
    }
  }

  private def revertGrayScale(image: BufferedImage): BufferedImage =
    new LookupOp(
      new ByteLookupTable(0, (255 to 0 by -1).map(_.toByte).toArray),
      null)
      .filter(image, null)

  def getImageData(label: Int): List[Array[Array[Int]]] = {
    var result: List[Array[Array[Int]]] = null
    for((d, l) <- imageData zip labels if l == label)
      result = d :: result
    result
  }
}
