package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseVector

class InputLayer(override val numNodes: Int) extends BaseLayer {
  var in: DenseVector[Double] = _
  override var _activation: DenseVector[Double] = _
  override def activation: DenseVector[Double] = {
    _activation = in
    in
  }
}
