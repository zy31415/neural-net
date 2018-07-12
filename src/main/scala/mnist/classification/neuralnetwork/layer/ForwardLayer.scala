package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseMatrix

import scala.collection.mutable.ListBuffer

abstract class ForwardLayer (val previous: BaseLayer,
                             override val numNodes: Int,
                             var weight: DenseMatrix[Double] = null,
                             var bias: DenseMatrix[Double] = null) extends BaseLayer {
  val activation: ActivationFunction = new SigmoidFunction()

  previous.next = this

  if (weight != null) {
    assert(weight.rows == numNodes)
    assert(weight.cols == previous.numNodes)
  } else if (ForwardLayer.isRandomInitialization)
    DenseMatrix.rand(numNodes, previous.numNodes, breeze.stats.distributions.Gaussian(0, 1))
  else
    DenseMatrix.zeros[Double](numNodes, previous.numNodes)

  if (bias != null) {
    assert(bias.rows == numNodes)
    assert(bias.cols == 1)
  } else if (ForwardLayer.isRandomInitialization)
    DenseMatrix.rand(numNodes, 1, breeze.stats.distributions.Gaussian(0, 1))
  else
    DenseMatrix.zeros[Double](numNodes, 1)

  var z: DenseMatrix[Double] = _
  var a: DenseMatrix[Double] = _

  def in = previous.out
  def out = {
    z = weight * in + bias
    a = z.map(activation(_))
    a
  }

  val C_ws = ListBuffer[DenseMatrix[Double]]()
  val C_bs = ListBuffer[DenseMatrix[Double]]()

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
    C_ws.append(_delta * previous.a.t)
    C_bs.append(_delta)
  }

  def update(rho: Double):Unit = {
    weight -= rho * ForwardLayer.average(C_ws.toList)
    bias -= rho * ForwardLayer.average(C_bs.toList)

    C_ws.clear()
    C_bs.clear()
  }
}

object ForwardLayer {
  // TODO: Change this to control weights and biases separately
  var isRandomInitialization = true

  def average(list: List[DenseMatrix[Double]]): DenseMatrix[Double] = {

    var result = DenseMatrix.zeros[Double](list.head.rows, list.head.cols)
    val size = list.length.toDouble

    for (a <- list)
      result += a/size

    result
  }
}

