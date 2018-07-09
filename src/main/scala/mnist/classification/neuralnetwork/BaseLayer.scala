package mnist.classification.neuralnetwork

import breeze.linalg.DenseMatrix

trait BaseLayer {
  val numNodes: Int
  def in: DenseMatrix[Double]
  def out: DenseMatrix[Double]
  var a: DenseMatrix[Double]

  var nextLayer: ForwardLayer = _
}
