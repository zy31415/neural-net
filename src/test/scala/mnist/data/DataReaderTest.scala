package mnist.data

import org.scalatest.FunSuite

class DataReaderTest extends FunSuite {
  test("read") {
    val imageData = DataReader.getAllImageDataAsMatrix()
  }

  test("getImage") {
    println(DataReader.getImage(10))
  }

  test("train data") {
    val nth = 546
    val imageData = MyMnistReader.trainImageData(nth)
    val label = MyMnistReader.trainLabels(nth)
    val image = new MnistImage(imageData, label, nth, nth)
    println(image)
  }

  test("test data") {
    val nth = 10
    val imageData = MyMnistReader.testImageData(nth)
    val label = MyMnistReader.testLabels(nth)
    val image = new MnistImage(imageData, label, nth, nth)
    println(image)
  }

}
