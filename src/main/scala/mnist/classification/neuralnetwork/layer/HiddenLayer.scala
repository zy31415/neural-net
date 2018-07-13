package mnist.classification.neuralnetwork.layer

import breeze.linalg.{DenseMatrix, DenseVector}

class HiddenLayer(previous: BaseLayer,
                  numNodes: Int,
                  weight: DenseMatrix[Double] = null,
                  bias: DenseVector[Double] = null
                 ) extends ForwardLayer(previous, numNodes, weight0 = weight, bias0 = bias) {
  override def delta = next.weight.t * next._delta *:* z.map(activation.d(_))
}