package mnist.classification.neuralnetwork.layer

import breeze.linalg.DenseMatrix

import scala.collection.mutable.ListBuffer

abstract class ForwardLayer (val previous: BaseLayer, override val numNodes: Int) extends BaseLayer {
  val activation: ActivationFunction = new SigmoidFunction()

  previous.next = this

  val weights =
    if (ForwardLayer.isRandomInitialization)
      DenseMatrix.rand(numNodes, previous.numNodes, breeze.stats.distributions.Gaussian(0, 1))
    else
      DenseMatrix.zeros[Double](numNodes, previous.numNodes)

  val bias =
    if (ForwardLayer.isRandomInitialization)
      DenseMatrix.rand(numNodes, 1, breeze.stats.distributions.Gaussian(0, 1))
    else
      DenseMatrix.zeros[Double](numNodes, 1)

  var z: DenseMatrix[Double] = _
  var a: DenseMatrix[Double] = _

  def in = previous.out
  def out = {
    z = weights * in + bias
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
    weights -= rho * ForwardLayer.average(C_ws.toList)
    bias -= rho * ForwardLayer.average(C_bs.toList)

    C_ws.clear()
    C_bs.clear()
  }
}

object ForwardLayer {
  var isRandomInitialization = true

  def average(list: List[DenseMatrix[Double]]): DenseMatrix[Double] = {
    var result: DenseMatrix[Double] = null

    for (a <- list) {
      if(result == null)
        result = a
      else
        result += a
    }
    result/list.length
  }
}

