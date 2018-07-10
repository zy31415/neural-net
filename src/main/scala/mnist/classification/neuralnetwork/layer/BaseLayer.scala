package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseMatrix

trait BaseLayer {
  val numNodes: Int
  def in: DenseMatrix[Double]
  def out: DenseMatrix[Double]
  var a: DenseMatrix[Double]

  var next: ForwardLayer = _
}
