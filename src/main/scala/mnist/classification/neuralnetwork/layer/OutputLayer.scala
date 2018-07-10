package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseMatrix

class OutputLayer(preLayer: BaseLayer, numNodes: Int) extends ForwardLayer (preLayer, numNodes) {
  next = null
  var y: DenseMatrix[Double] = _
  override def delta = (a - y) *:* z.map(activation.d)
}
