package mnist.classification.neuralnetwork.layer

import breeze.linalg.{DenseMatrix, DenseVector}

class OutputLayer(preLayer: BaseLayer,
                  numNodes: Int,
                  weight: DenseMatrix[Double] = null,
                  bias: DenseVector[Double] = null
                 ) extends ForwardLayer (preLayer, numNodes, weight, bias) {
  /**
    * Calculate output of an input (assigned by InputLayer.in). This will trigger forward calculation.
    * @return
    */
  def out = activation
  next = null

  var _y: DenseVector[Double] = _
  def y_= (value:DenseVector[Double]):Unit = _y = value
  def y = _y

  override def delta = (_activation - _y) *:* activationFunction.d(z)
}
