package mnist.data

import org.scalatest.FunSuite

class DataReaderTest extends FunSuite {
  test("read") {
    val imageData = DataReader.getAllImageDataMatrix()
  }

  test("getImage") {
    println(DataReader.getImage(10))
  }

}
