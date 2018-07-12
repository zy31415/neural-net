package mnist.classification.neuralnetwork.layer

import breeze.linalg.{DenseMatrix, DenseVector}

class InputLayer(val numNodes: Int) extends BaseLayer {
  var in: DenseVector[Double] = _
  var a: DenseVector[Double] = _
  def out = {
    a = in
    in
  }
}
