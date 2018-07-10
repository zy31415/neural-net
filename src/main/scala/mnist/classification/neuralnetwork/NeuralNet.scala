package mnist.classification.neuralnetwork

import breeze.linalg.DenseMatrix
import mnist.data.MnistImage

class NeuralNet {

  val rho = 1.0
  var inputLayer: InputLayer = _
  var outputLayer: OutputLayer = _

  def build(): Unit = {
    inputLayer = new InputLayer(MnistImage.numPixels)
    val layer1 = new HiddenLayer(inputLayer, 15)
    outputLayer = new OutputLayer(layer1, 10)
  }

  def train(Xs: List[DenseMatrix[Double]], Ys: List[DenseMatrix[Double]]): Unit = {
    for ((x, y) <- Xs zip Ys) {
      inputLayer.in = x
      outputLayer.y = y
      // Forwarding calculation
      outputLayer.out

      // train
      backPropagate()
      update()
    }
    println("Stop")
  }

  private def backPropagate(): Unit = {
    var layer:ForwardLayer = outputLayer

    while(layer != null) {
      layer.backPropagate()
      if (layer.preLayer != inputLayer)
        layer = layer.preLayer.asInstanceOf[ForwardLayer]
      else
        layer = null
    }
  }

  private def update(): Unit = {
    var layer:ForwardLayer = outputLayer

    while(layer != null) {
      layer.update(rho)
      if (layer.preLayer != inputLayer)
        layer = layer.preLayer.asInstanceOf[ForwardLayer]
      else
        layer = null
    }
  }
}
