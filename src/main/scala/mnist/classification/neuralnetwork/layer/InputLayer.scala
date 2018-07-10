package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseMatrix

class InputLayer(val numNodes: Int) extends BaseLayer {
  var in: DenseMatrix[Double] = _
  var a: DenseMatrix[Double] = _
  def out = {
    a = in
    in
  }
}
