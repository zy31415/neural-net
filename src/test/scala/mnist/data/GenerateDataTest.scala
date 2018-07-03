package mnist.data

import org.scalatest.FunSuite

class GenerateDataTest extends FunSuite {
  test("Generate data") {
    GenerateData.selectOnesAndFives("/Users/zy/Documents/workspace/mnist/workspace/data/data.h5")
  }
}
