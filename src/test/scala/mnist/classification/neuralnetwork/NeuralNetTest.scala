package mnist.classification.neuralnetwork

import breeze.linalg.{DenseMatrix, DenseVector, argmax}
import hdf.`object`.{Dataset, FileFormat}
import mnist.classification.neuralnetwork.layer.ForwardLayer
import mnist.data.{MnistImage, MyMnistReader, Utils => dataUtils}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class NeuralNetTest extends FunSuite {
  test("test") {

    ForwardLayer.isRandomInitializationWeight = true

    val neuralNets = new NeuralNet(
      Array(MnistImage.numPixels, 30, 10)
    )

    val data = MyMnistReader.trainImageData
    val labels = MyMnistReader.trainLabels

    neuralNets.learningRate = 3.0
    neuralNets.ifRandomShuffle = true
    neuralNets.miniBatchSize = 100

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
      val out = neuralNets.outputLayer.activation.toDenseVector

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
      results.append(dataUtils.labelEncoder(l, 10))
    }
    results.toArray
  }

  test("neuralnets creation") {
    ForwardLayer.isRandomInitializationWeight = false
    val neuralNet = new NeuralNet(Array(2, 1))

    val Xs = Array(
      DenseVector(1.0, 1.0),
      DenseVector(-1.0, -1.0)
    )

    val Ys = Array(
      DenseVector(1.0),
      DenseVector(1.0)
    )

    neuralNet.train(Xs, Ys)

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
      weights = weights,
      biases = biases
    )

    val imageData = readTrainImageData
    val labels = readTrainLabelData

    val testImageData = readTestImageData
    val testLabelData = readTestLabelData

    neuralNets.learningRate = 3.0
    neuralNets.miniBatchSize = 10
    neuralNets.ifRandomShuffle = false

    neuralNets.train(imageData, labels)
    neuralNets.evaluate(testImageData, testLabelData)
  }

  test("Neural Net test - repeat 30 times") {
    val neuralNets = new NeuralNet(
      Array(784, 30, 10),
      weights = null,
      biases = null
    )

    val imageData = readTrainImageData
    val labels = readTrainLabelData

    val testImageData = readTestImageData
    val testLabelData = readTestLabelData

    neuralNets.learningRate  = 3.0
    neuralNets.miniBatchSize = 10

    (0 until 30).foreach(_ => {
      neuralNets.train(imageData, labels)
      neuralNets.evaluate(testImageData, testLabelData)
    })
  }

  test("Neural Net test - 2 hidden layers") {
    val neuralNets = new NeuralNet(
      Array(784, 100, 30, 10),
      weights = null,
      biases = null
    )

    val imageData = readTrainImageData
    val labels = readTrainLabelData

    val testImageData = readTestImageData
    val testLabelData = readTestLabelData

    neuralNets.learningRate  = 3.0
    neuralNets.miniBatchSize = 10

    (0 until 30).foreach(_ => {
      neuralNets.train(imageData, labels)
      neuralNets.evaluate(testImageData, testLabelData)
    })
  }


  val initDatafile = "/Users/zy/Documents/workspace/neural-networks-and-deep-learning/workspace/init.h5"

  private def readWeights() =
    Array(readDataset(initDatafile, "/init/weights/layer1"),
      readDataset(initDatafile, "/init/weights/layer2"))

  private def readBiases() =
    Array(readDataset(initDatafile, "/init/biases/layer1")(::, 0),
      readDataset(initDatafile, "/init/biases/layer2")(::, 0))

  val datafile = "/Users/zy/Documents/workspace/neural-networks-and-deep-learning/workspace/dataset.h5"

  private def readTrainImageData = readDataset(datafile, "/training/images").copy

  private def readTrainLabelData = readDataset(datafile, "/training/labels").copy

  private def readTestImageData = readDataset(datafile, "/testing/images")

  private def readTestLabelData ={
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    val hdf5File = fileFormat.createInstance(datafile, FileFormat.READ)
    hdf5File.open

    val dataset = hdf5File.get("/testing/labels").asInstanceOf[Dataset]
    dataset.init()

    val data = dataset.getData

    val dims = dataset.getDims

    if (dims.length == 2) {
      val result = data.asInstanceOf[Array[Long]].map(_.toInt)
      hdf5File.close()
      DenseVector(result)
    } else {
      throw new IllegalAccessError()
    }
  }

  private def readDataset(datafile: String, path: String) = {
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    val hdf5File = fileFormat.createInstance(datafile, FileFormat.READ)
    hdf5File.open
    val dataset = hdf5File.get(path).asInstanceOf[Dataset]
    dataset.init()

    val data = dataset.getData

    val dims = dataset.getDims

    if (dims.length == 2) {
      val dimMinor = dataset.getDims()(0).toInt
      val dimMajor = dataset.getDims()(1).toInt

      val result = data.asInstanceOf[Array[Double]]

      hdf5File.close()

      /**
        * Note that hdf5 is row major, but breeze is column major.
        * Note also that DenseMatrix.t doesn't do hard copy on data.
        * It keep the reference of the data part.
        */
      new DenseMatrix(dimMajor, dimMinor, result).t
    } else {
      throw new IllegalAccessError()
    }
  }
}
