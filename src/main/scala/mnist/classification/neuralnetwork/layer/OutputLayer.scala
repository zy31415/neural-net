package mnist.classification.neuralnetwork.layer

import breeze.linalg.{DenseMatrix, DenseVector}

class OutputLayer(preLayer: BaseLayer,
                  numNodes: Int,
                  weight: DenseMatrix[Double] = null,
                  bias: DenseVector[Double] = null
                 ) extends ForwardLayer (preLayer, numNodes, weight, bias) {
  next = null
  var y: DenseVector[Double] = _
  override def delta = (a - y) *:* z.map(activation.d)
}
