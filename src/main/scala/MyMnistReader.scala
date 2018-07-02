import java.awt.image.{BufferedImage, ByteLookupTable, LookupOp}

import mnist.MnistReader

import scala.collection.JavaConverters._


object MyMnistReader {

  def imagePath = getClass.getResource("train-images-idx3-ubyte").getPath
  def labelPath = getClass.getResource("train-labels-idx1-ubyte").getPath

  def numImages = imageData.length
  def imageHeight = 28
  def imageWidth = 28

  private var _images: List[Array[Int]] = _
  private var _labels: Array[Int] = _

  def imageData: List[Array[Int]] = {
    if (_images == null) {
      _images = MnistReader.getImages(imagePath).asScala.toList.map(_.flatten)
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

  private def setValue(image: BufferedImage, nth: Int): Unit =
    for ((ele, i) <- MyMnistReader.imageData(nth).view.zipWithIndex)
      image.getRaster.getDataBuffer.setElem(i, ele)

  private def revertGrayScale(image: BufferedImage): BufferedImage =
    new LookupOp(
      new ByteLookupTable(0, (255 to 0 by -1).map(_.toByte).toArray),
      null)
      .filter(image, null)

  def getImageData(label: Int): List[Array[Int]] = {
    var result: List[Array[Int]] = null
    for((d, l) <- imageData zip labels if l == label)
      result = d :: result
    result
  }
}
