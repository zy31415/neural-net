package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseVector

class InputLayer(override val numNodes: Int) extends BaseLayer {
  var _in: DenseVector[Double] = _
  def in = _in
  def in_= (value: DenseVector[Double]):Unit = _in = value

  override var _activation: DenseVector[Double] = _
  override def activation: DenseVector[Double] = {
    _activation = _in
    _in
  }
}
