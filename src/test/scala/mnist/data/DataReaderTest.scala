package mnist.data

import org.scalatest.FunSuite

class DataReaderTest extends FunSuite {
  test("read") {
    val imageData = DataReader.getAllImageDataAsMatrix()
  }

  test("getImage") {
    println(DataReader.getImage(10))
  }

}
