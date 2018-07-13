package mnist.classification.neuralnetwork.layer

import breeze.linalg.{DenseMatrix, DenseVector}

import scala.collection.mutable.ListBuffer

abstract class ForwardLayer (val previous: BaseLayer,
                             override val numNodes: Int,
                             weight0: DenseMatrix[Double] = null,
                             bias0: DenseVector[Double] = null) extends BaseLayer{

  val activationFunction: ActivationFunction = new SigmoidFunction()

  previous.next = this

  val weight: DenseMatrix[Double] =
    if (weight0 != null) {
      assert(weight0.rows == numNodes)
      assert(weight0.cols == previous.numNodes)

      // Don't refer to but make a copy of weight0, because weight will be updated.
      weight0.copy
    } else if (ForwardLayer.isRandomInitializationWeight)
      DenseMatrix.rand(numNodes, previous.numNodes, breeze.stats.distributions.Gaussian(0, 1))
    else
      DenseMatrix.zeros[Double](numNodes, previous.numNodes)

  val bias: DenseVector[Double] =
    if (bias0 != null) {
      assert(bias0.length == numNodes)

      // Don't refer to but make a copy of bias0, because bias will be updated during fitting.
      bias0.copy
    } else if (ForwardLayer.isRandomInitializationBias)
      DenseVector.rand(numNodes, breeze.stats.distributions.Gaussian(0, 1))
    else
      DenseVector.zeros[Double](numNodes)

  var z: DenseVector[Double] = _

  /**
    * Store calculated activation.
    */
  override var _activation: DenseVector[Double] = _

  /**
    * Calculate activation and return.
    * @return activation
    */
  override def activation = {
    z = weight * previous.activation + bias
    _activation = activationFunction(z)
    _activation
  }

  val C_ws = ListBuffer[DenseMatrix[Double]]()
  val C_bs = ListBuffer[DenseVector[Double]]()

  /**
    * Store calculated delta.
    */
  var _delta : DenseVector[Double] = _

  /**
    * Method to calculate delta.
    * @return
    */
  def delta: DenseVector[Double]

  def backPropagate(): Unit = {
    _delta = delta
    C_ws.append(_delta * previous._activation.t)
    C_bs.append(_delta)
  }

  def update(rho: Double):Unit = {
    weight -= (rho * ForwardLayer.averageMat(C_ws.toList))
    bias -= (rho * ForwardLayer.averageVec(C_bs.toList))

    C_ws.clear()
    C_bs.clear()
  }
}

object ForwardLayer {
  /**
    * Note that you should always randomize the initialization of initial values of weights and biases.
    * Reset these to false only for experiment purposes.
    */
  var isRandomInitializationWeight = true
  var isRandomInitializationBias = true

  // TODO: You perhaps could replace the following two functions with one UFunc.
  def averageVec(list: List[DenseVector[Double]]): DenseVector[Double] = {
    var result = DenseVector.zeros[Double](list.head.length)
    for (a <- list)
      result += a
    result/list.length.toDouble
  }

  def averageMat(list: List[DenseMatrix[Double]]): DenseMatrix[Double] = {
    var result = DenseMatrix.zeros[Double](list.head.rows, list.head.cols)
    for (a <- list)
      result += a
    result/list.length.toDouble
  }
}

