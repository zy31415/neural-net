package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseMatrix

class HiddenLayer(previous: BaseLayer,
                  numNodes: Int,
                  weight: DenseMatrix[Double] = null,
                  bias: DenseMatrix[Double] = null
                 ) extends ForwardLayer(previous, numNodes, weight = weight, bias = bias) {
  override def delta = next.weight.t * next._delta *:* z.map(activation.d(_))
}