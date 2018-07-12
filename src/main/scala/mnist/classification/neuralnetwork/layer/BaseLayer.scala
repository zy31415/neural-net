package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseVector

trait BaseLayer {
  val numNodes: Int
  def in: DenseVector[Double]
  def out: DenseVector[Double]
  var a: DenseVector[Double]

  var next: ForwardLayer = _
}
