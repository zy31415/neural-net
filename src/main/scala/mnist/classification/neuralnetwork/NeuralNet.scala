package mnist.classification.neuralnetwork

import breeze.linalg.{DenseMatrix, shuffle}
import mnist.classification.neuralnetwork.layer._

class NeuralNet(val sizes: Array[Int],
                val ifRandomShuffle: Boolean = true,
                val learningRate: Double = 0.1,
                val miniBatchSize: Int = 100,
                weights: Array[DenseMatrix[Double]] = null,
                biases: Array[DenseMatrix[Double]] = null) {

  val inputLayer = new InputLayer(sizes(0))

  val outputLayer = {
    var preLayer:BaseLayer = inputLayer

    assert(sizes.length - 1 == weights.length && sizes.length - 1 == biases.length)

    var weight = if (weights == null) null else weights.head
    var bias = if (biases == null) null else biases.head

    for (nth <- 1 until sizes.length -1) {
      preLayer = new HiddenLayer(preLayer, sizes(nth), weight, bias)
      if (weight != null) weight = weights(nth)
      if (bias != null) bias = biases(nth)
    }

    new OutputLayer(preLayer, sizes.last, weights.last, biases.last)
  }

  def train(Xs: List[DenseMatrix[Double]], Ys: List[DenseMatrix[Double]]): Unit = {

    val numSamples = Xs.length
    assert(numSamples == Ys.length)

    val order =
      if (ifRandomShuffle)
        shuffle(0 until numSamples)
      else
        0 until numSamples

    for ((nth, count) <- order.zipWithIndex) {
      inputLayer.in = Xs(nth)
      outputLayer.y = Ys(nth)
      // Forwarding calculation
      outputLayer.out
      // train
      backPropagate()
      if (count % miniBatchSize == 0)
        update()
    }

    if (outputLayer.C_ws.nonEmpty)
      update()

  }

  private def backPropagate(): Unit = {
    var layer:ForwardLayer = outputLayer

    while(layer != null) {
      layer.backPropagate()
      if (layer.previous != inputLayer)
        layer = layer.previous.asInstanceOf[ForwardLayer]
      else
        layer = null
    }
  }

  private def update(): Unit = {
    var layer:ForwardLayer = outputLayer

    while(layer != null) {
      layer.update(learningRate)
      if (layer.previous != inputLayer)
        layer = layer.previous.asInstanceOf[ForwardLayer]
      else
        layer = null
    }
  }
}
