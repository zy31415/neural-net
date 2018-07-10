package mnist.classification.neuralnetwork

import breeze.linalg.{DenseMatrix, shuffle}
import mnist.classification.neuralnetwork.layer._

class NeuralNet(sizes: Array[Int],
               val ifRandomShuffle: Boolean = true,
               val learningRate: Double = 0.1,
                val miniBatchSize: Int = 100) {

  val inputLayer = new InputLayer(sizes(0))

  val outputLayer = {
    var preLayer:BaseLayer = inputLayer
    for (nth <- 1 until sizes.length -1)
      preLayer = new HiddenLayer(preLayer, sizes(nth))
    new OutputLayer(preLayer, sizes.last)
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
