package mnist.data

import org.scalatest.FunSuite

class MnistToHDF5Test extends FunSuite {

  test("run a conversion") {
    MnistToHDF5.genrate()
  }
}
