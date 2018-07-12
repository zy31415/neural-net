package mnist.classification.neuralnetwork

import breeze.linalg.{*, DenseMatrix, Transpose, argmax}
import hdf.`object`.{Dataset, FileFormat, Group}
import javax.swing.tree.DefaultMutableTreeNode
import mnist.classification.neuralnetwork.layer.ForwardLayer
import mnist.data.DataReader.{_imageData, close}
import mnist.data.MnistToHDF5.hdf5File
import mnist.data.{MnistImage, MyMnistReader}
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
      results.append(new DenseMatrix(arr.length ,1 , arr.map(_.toDouble)) /:/ 255.0)
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

  test("sin") {
    ForwardLayer.isRandomInitialization = false
    val neuralNet = new NeuralNet(
      Array(1, 1, 1),
      ifRandomShuffle = false,
      miniBatchSize = 1,
      learningRate = 1.0
    )

    val _x = (BigDecimal(0.0) to Math.PI by 0.5).map(_.toDouble)
    val _y = _x.map(Math.sin(_))

    val x = _x.map(i => new DenseMatrix[Double](1, 1, Array(i))).toList
    val y = _y.map(i => new DenseMatrix[Double](1, 1, Array(i))).toList

    println(x)
    println(y)

    neuralNet.train(x, y)

    val x_test = (BigDecimal(0.0) to Math.PI by 0.1).map(_.toDouble).toArray
    val y_test_buffer = new ArrayBuffer[Double]()


    for (xi <- x_test) {
      neuralNet.inputLayer.in = new DenseMatrix[Double](1, 1, Array(xi))
      y_test_buffer += neuralNet.outputLayer.out(0, 0)
    }
    val y_test = y_test_buffer.toArray

    x_test.foreach(i => print(s"$i "))
    println("")
    y_test.foreach(i => print(s"$i "))

  }

  test("model Neural Net and Deep Learning") {
    val weights = readWeights()
    val biases = readBiases()

    val neuralNets = new NeuralNet(
      Array(784, 30, 10),
      learningRate = 3.0,
      ifRandomShuffle = true,
      miniBatchSize = 100,
      weights = weights,
      biases = biases
    )

    readTrainImageData

    val m = DenseMatrix((1.0, 2.0, 3.0))
    m.t


  }

  val initDatafile = "/Users/zy/Documents/workspace/neural-networks-and-deep-learning/workspace/init.h5"

  private def readWeights() =
    Array(readDataset(initDatafile, "/init/weights/layer1"),
      readDataset(initDatafile, "/init/weights/layer2"))

  private def readBiases() =
    Array(readDataset(initDatafile, "/init/biases/layer1"),
      readDataset(initDatafile, "/init/biases/layer2"))

  val datafile = "/Users/zy/Documents/workspace/neural-networks-and-deep-learning/workspace/dataset.h5"

  private def readTrainImageData = {
    val images = readDataset(datafile, "/training/images")
    val out = ListBuffer[DenseMatrix[Double]]()
    for (ii <- 0 to images.rows) {
      out.append(images(ii, ::).t.asDenseMatrix)
      Transpose
    }
    out.toList
  }
  private def readTrainLabelData = readDataset(datafile, "/training/labels")

  private def readDataset(datafile: String, path: String) = {
    val datafile = "/Users/zy/Documents/workspace/neural-networks-and-deep-learning/workspace/init.h5"
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    val hdf5File = fileFormat.createInstance(datafile, FileFormat.READ)
    hdf5File.open
    val dataset = hdf5File.get(path).asInstanceOf[Dataset]
    dataset.init()

    val data = dataset.getData
    val numRow = dataset.getDims()(0).toInt
    val numCol = dataset.getDims()(1).toInt

    val result = data.asInstanceOf[Array[Double]]
    hdf5File.close()
    new DenseMatrix(numRow, numCol, result)
  }
}
