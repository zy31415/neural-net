package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseVector

trait BaseLayer {
  val numNodes: Int
  def activation: DenseVector[Double]
  var _activation: DenseVector[Double]

  var next: ForwardLayer = _
}
