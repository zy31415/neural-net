package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseMatrix
import org.scalatest.FunSuite

class ForwardLayerTest extends FunSuite {
  test("test average") {
    val x = ForwardLayer.average(
      List(
        DenseMatrix.ones[Double](3, 2),
        DenseMatrix.ones[Double](3, 2) * 2.0
      )
    )
    assert(x == DenseMatrix.ones[Double](3, 2) * 1.5)
  }
}
