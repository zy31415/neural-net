package mnist.data

import org.scalatest.FunSuite

class DataPreprocessorTest extends FunSuite {
  test("Generate data") {
    DataPreprocessor.selectOnesAndFives("/Users/zy/Documents/workspace/mnist/workspace/data/data.h5")
  }
}
