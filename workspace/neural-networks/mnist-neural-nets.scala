import breeze.linalg.DenseMatrix
import mnist.classification.neuralnetwork.MnistNeuralNet
import mnist.data.MyMnistReader

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

val neuralNets = new MnistNeuralNet()

val data = MyMnistReader.trainImageData
val labels = MyMnistReader.trainLabels


def convertData(data: Array[Array[Int]]): List[DenseMatrix[Double]] = {
  val results = ListBuffer[DenseMatrix[Double]]()
  for (arr <- data) {
    results.append(new DenseMatrix(arr.length ,1 , arr.map(_.toDouble)))
  }
  results.toList
}

def convertLabels(labels: Array[Int]):List[DenseMatrix[Double]] = {
  val results = ListBuffer[DenseMatrix[Double]]()

  for (l <- labels) {
    val out = DenseMatrix.zeros[Double](10, 1)
    out(l, 0) = 1.0
    results.append(out)
  }
  results.toList
}


neuralNets.build()

neuralNets.train(
  convertData(data),
  convertLabels(labels)
)

val testData = MyMnistReader.testImageData
val testLabels = MyMnistReader.testLabels

for (d <- convertData(testData)) {
  neuralNets.inputLayer.in = d
  println(neuralNets.outputLayer.out.t)
}