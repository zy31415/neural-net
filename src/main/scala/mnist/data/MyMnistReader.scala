package mnist.data

import java.awt.image.{BufferedImage, ByteLookupTable, LookupOp}

import mnist.MnistReader

import scala.collection.JavaConverters._


object MyMnistReader {

  def numImages = trainImageData.length
  def imageHeight = 28
  def imageWidth = 28

  // Train dataset
  def trainImagePath = getClass.getResource("/train-images-idx3-ubyte").getPath
  def trainLabelPath = getClass.getResource("/train-labels-idx1-ubyte").getPath

  private var _train_images: Array[Array[Int]] = _
  private var _train_labels: Array[Int] = _

  def trainImageData: Array[Array[Int]] = {
    if (_train_images == null) {
      _train_images = MnistReader.getImages(trainImagePath).asScala.toArray.map(_.flatten)
    }
    _train_images
  }

  def trainLabels: Array[Int] = {
    if (_train_labels == null) {
      _train_labels = MnistReader.getLabels(trainLabelPath)
    }
    _train_labels
  }


  // Testing dataset
  def testImagePath = getClass.getResource("/t10k-images-idx3-ubyte").getPath
  def testLabelPath = getClass.getResource("/t10k-labels-idx1-ubyte").getPath

  private var _test_images: Array[Array[Int]] = _
  private var _test_labels: Array[Int] = _

  def testImageData: Array[Array[Int]] = {
    if (_test_images == null) {
      _test_images = MnistReader.getImages(testImagePath).asScala.toArray.map(_.flatten)
    }
    _test_images
  }

  def testLabels: Array[Int] = {
    if (_test_labels == null) {
      _test_labels = MnistReader.getLabels(testLabelPath)
    }
    _test_labels
  }

  @deprecated
  def images (nth: Int): BufferedImage = asBufferedImage(trainImageData(nth))

  @deprecated
  def asBufferedImage(imageData: Array[Int]): BufferedImage = {
    val bufferedImage = new BufferedImage(
      MyMnistReader.imageWidth, MyMnistReader.imageHeight, BufferedImage.TYPE_BYTE_GRAY)
    setValue(bufferedImage, imageData)
    revertGrayScale(bufferedImage)
  }

  @deprecated
  private def setValue(image: BufferedImage, nth: Int): Unit = setValue(image, trainImageData(nth))

  @deprecated
  private def setValue(image: BufferedImage, imageData: Array[Int]): Unit =
    for ((ele, i) <- imageData.view.zipWithIndex)
      image.getRaster.getDataBuffer.setElem(i, ele)

  @deprecated
  private def revertGrayScale(image: BufferedImage): BufferedImage =
    new LookupOp(
      new ByteLookupTable(0, (255 to 0 by -1).map(_.toByte).toArray),
      null)
      .filter(image, null)

  @deprecated
  def getImageData(label: Int): List[Array[Int]] = {
    var result: List[Array[Int]] = Nil
    for((d, l) <- trainImageData zip trainLabels if l == label)
      result = d :: result
    result
  }
}
