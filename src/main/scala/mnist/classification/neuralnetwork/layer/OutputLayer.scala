package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseMatrix

class OutputLayer(preLayer: BaseLayer,
                  numNodes: Int,
                  weight: DenseMatrix[Double] = null,
                  bias: DenseMatrix[Double] = null
                 ) extends ForwardLayer (preLayer, numNodes, weight, bias) {
  next = null
  var y: DenseMatrix[Double] = _
  override def delta = (a - y) *:* z.map(activation.d)
}
