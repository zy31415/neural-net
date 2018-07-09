package mnist.classification.neuralnetwork

import breeze.linalg.DenseMatrix

abstract class ForwardLayer (val preLayer: BaseLayer, override val numNodes: Int) extends BaseLayer {
  val activation: ActivationFunction = new SigmoidFunction()

  preLayer.nextLayer = this

  val weights = new DenseMatrix[Double](numNodes, preLayer.numNodes)
  val bias = new DenseMatrix[Double](numNodes, 1)

  var z: DenseMatrix[Double] = _
  var a: DenseMatrix[Double] = _

  def in = preLayer.out
  def out = {
    z = weights * in + bias
    a = z.map(activation(_))
    a
  }

  var C_b: DenseMatrix[Double] = _
  var C_w: DenseMatrix[Double] = _

  /**
    * Store calculated delta.
    */
  var _delta : DenseMatrix[Double] = _

  /**
    * Method to calculate delta.
    * @return
    */
  def delta: DenseMatrix[Double]

  def backPropagate(): Unit = {
    _delta = delta
    C_w = _delta * preLayer.a.t
    C_b = _delta
  }

  def update(rho: Double):Unit = {
    weights -= rho * C_w
    bias -= rho * C_b
  }

}
