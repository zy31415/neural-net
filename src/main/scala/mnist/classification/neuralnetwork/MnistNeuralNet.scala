package mnist.classification.neuralnetwork

import breeze.linalg.DenseMatrix
import mnist.data.MnistImage

class MnistNeuralNet {
  var inputLayer: InputLayer = _
  var outputLayer: OutputLayer = _

  def build(): Unit = {
    inputLayer = new InputLayer(MnistImage.numPixels)
    val layer1 = new HiddenLayer(inputLayer, 15)
    outputLayer = new OutputLayer(layer1, 10)
  }

  def train(Xs: List[DenseMatrix[Double]], Ys: List[DenseMatrix[Double]]): Unit = {
    for((x, y) <- Xs zip Ys) {
      inputLayer.in = x
      outputLayer.y = y

      // Forwarding calculation
      outputLayer.out

      // Backward propagation
      var layer = outputLayer

      while(layer.preLayer.isInstanceOf[ForwardLayer]) {
        layer.backPropagate()
        layer = layer.preLayer
      }

    }

  }
}
