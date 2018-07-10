package mnist.classification.neuralnetwork

import breeze.linalg.{DenseMatrix, argmax}
import mnist.classification.neuralnetwork.layer.ForwardLayer
import mnist.data.{MnistImage, MyMnistReader}
import org.scalatest.FunSuite

import scala.collection.mutable.ListBuffer

class NeuralNetTest extends FunSuite {
  test("test") {

    ForwardLayer.isRandomInitialization = true

    val neuralNets = new NeuralNet(
      Array(MnistImage.numPixels, 100, 30, 15, 10),
      learningRate = 0.01,
      ifRandomShuffle = true
    )

    val data = MyMnistReader.trainImageData
    val labels = MyMnistReader.trainLabels

    neuralNets.train(
      convertData(data),
      convertLabels(labels)
    )

    val testData = MyMnistReader.testImageData
    val testLabels = MyMnistReader.testLabels
    val numTestSamples = testLabels.length

    var correctPrediction = 0

    for ((d, l) <- convertData(testData) zip testLabels) {
      neuralNets.inputLayer.in = d
      val out = neuralNets.outputLayer.out.toDenseVector

      if (l == argmax(out))
        correctPrediction += 1
    }

    val rate = 1.0 * correctPrediction / numTestSamples

    println(s"Correct Rate:$rate ($correctPrediction/$numTestSamples)")
  }

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

  test("neuralnets creation") {
    ForwardLayer.isRandomInitialization = false
    val neuralNet = new NeuralNet(Array(2, 1))

    val Xs = List(
      new DenseMatrix(2, 1, Array(1.0, 1.0)),
      new DenseMatrix(2, 1, Array(-1.0, -1.0))
    )

    val Ys = List(
      new DenseMatrix[Double](1,1, Array(1.0)),
      new DenseMatrix[Double](1,1, Array(-1.0))
    )

    neuralNet.train(Xs, Ys)

    println(neuralNet)

  }
}
