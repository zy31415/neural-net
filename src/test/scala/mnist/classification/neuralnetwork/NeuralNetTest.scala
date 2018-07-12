package mnist.classification.neuralnetwork

import breeze.linalg.{DenseMatrix, DenseVector, Transpose, argmax}
import hdf.`object`.{Dataset, FileFormat}
import mnist.classification.neuralnetwork.layer.ForwardLayer
import mnist.data.{MnistImage, MyMnistReader, Utils}
import org.scalatest.FunSuite

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class NeuralNetTest extends FunSuite {
  test("test") {

    ForwardLayer.isRandomInitialization = true

    val neuralNets = new NeuralNet(
      Array(MnistImage.numPixels, 30, 10),
      learningRate = 3.0,
      ifRandomShuffle = true,
      miniBatchSize = 100
    )

    val data = MyMnistReader.trainImageData
    val labels = MyMnistReader.trainLabels

    neuralNets.train(
      convertData(data),
      convertLabels(labels),
      1
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

  def convertData(data: Array[Array[Int]]): Array[DenseVector[Double]] = {
    val results = ArrayBuffer[DenseVector[Double]]()
    for (arr <- data) {
      results.append(DenseVector(arr.map(_.toDouble):_*) /:/ 255.0)
    }
    results.toArray
  }

  def convertLabels(labels: Array[Int]): Array[DenseVector[Double]] = {
    val results = ArrayBuffer[DenseVector[Double]]()

    for (l <- labels) {
      results.append(Utils.labelEncoder(l, 10))
    }
    results.toArray
  }

  test("neuralnets creation") {
    ForwardLayer.isRandomInitialization = false
    val neuralNet = new NeuralNet(Array(2, 1))

    val Xs = Array(
      DenseVector(1.0, 1.0),
      DenseVector(-1.0, -1.0)
    )

    val Ys = Array(
      DenseVector(1.0),
      DenseVector(1.0)
    )

    neuralNet.train(Xs, Ys, 1)

    println(neuralNet)

  }

//  test("sin") {
//    ForwardLayer.isRandomInitialization = false
//    val neuralNet = new NeuralNet(
//      Array(1, 1, 1),
//      ifRandomShuffle = false,
//      miniBatchSize = 1,
//      learningRate = 1.0
//    )
//
//    val _x = (BigDecimal(0.0) to Math.PI by 0.5).map(_.toDouble)
//    val _y = _x.map(Math.sin(_))
//
//    val x = _x.map(i => new DenseMatrix[Double](1, 1, Array(i))).toList
//    val y = _y.map(i => new DenseMatrix[Double](1, 1, Array(i))).toList
//
//    println(x)
//    println(y)
//
//    neuralNet.train(x, y)
//
//    val x_test = (BigDecimal(0.0) to Math.PI by 0.1).map(_.toDouble).toArray
//    val y_test_buffer = new ArrayBuffer[Double]()
//
//
//    for (xi <- x_test) {
//      neuralNet.inputLayer.in = new DenseMatrix[Double](1, 1, Array(xi))
//      y_test_buffer += neuralNet.outputLayer.out(0, 0)
//    }
//    val y_test = y_test_buffer.toArray
//
//    x_test.foreach(i => print(s"$i "))
//    println("")
//    y_test.foreach(i => print(s"$i "))
//
//  }

  test("model Neural Net and Deep Learning") {
    val weights = readWeights()
    val biases = readBiases()

    val neuralNets = new NeuralNet(
      Array(784, 30, 10),
      learningRate = 3.0,
      ifRandomShuffle = false,
      miniBatchSize = 1,
      weights = weights,
      biases = biases
    )

    val imageData = readTrainImageData
    val labels = readTrainLabelData

    val testImageData = readTestImageData
    val testLabelData = readTestLabelData

    neuralNets.train(imageData, labels, 1)

    neuralNets.evaluate(testImageData, testLabelData)


  }

  val initDatafile = "/Users/zy/Documents/workspace/neural-networks-and-deep-learning/workspace/init.h5"

  private def readWeights() =
    Array(readDataset(initDatafile, "/init/weights/layer1").asInstanceOf[DenseMatrix[Double]],
      readDataset(initDatafile, "/init/weights/layer2").asInstanceOf[DenseMatrix[Double]])

  private def readBiases() =
    Array(readDataset(initDatafile, "/init/biases/layer1").asInstanceOf[DenseMatrix[Double]](::,0),
      readDataset(initDatafile, "/init/biases/layer2").asInstanceOf[DenseMatrix[Double]](::,0))

  val datafile = "/Users/zy/Documents/workspace/neural-networks-and-deep-learning/workspace/dataset.h5"

  private def readTrainImageData =
    readDataset(datafile, "/training/images").asInstanceOf[DenseMatrix[Double]]

  private def readTrainLabelData =
    readDataset(datafile, "/training/labels").asInstanceOf[DenseMatrix[Double]]

  private def readTestImageData =
    readDataset(datafile, "/testing/images").asInstanceOf[DenseMatrix[Double]]

  private def readTestLabelData =
    readDataset(datafile, "/testing/labels").asInstanceOf[DenseVector[Int]]

  private def readDataset(datafile: String, path: String) = {
//    val datafile = "/Users/zy/Documents/workspace/neural-networks-and-deep-learning/workspace/init.h5"
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    val hdf5File = fileFormat.createInstance(datafile, FileFormat.READ)
    hdf5File.open
    val dataset = hdf5File.get(path).asInstanceOf[Dataset]
    dataset.init()

    val data = dataset.getData

    val dims = dataset.getDims

    dims.foreach(a => print(s"$a "))
    println("")

    /* TODO: This is a hell. Fix this.
     1. Why some times 3 dimension, sometimes, 2 dimensions and sometimes 1 dimension?
     2. How to handle different datatype casting?
      */
    if (dims.length >= 2) {
      val numRow = dataset.getDims()(0).toInt
      val numCol = dataset.getDims()(1).toInt

      val result = data.asInstanceOf[Array[Double]]

      hdf5File.close()

      new DenseMatrix(numRow, numCol, result)
    } else if (dims.length == 1) {
      val numRow = dataset.getDims()(0).toInt

      val result = data.asInstanceOf[Array[Long]].map(_.toInt)

      hdf5File.close()

      DenseVector(result:_*)
    } else {
      throw new IllegalAccessError()
    }
  }
}
