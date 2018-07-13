package mnist.classification.neuralnetwork

import breeze.linalg.{DenseMatrix, DenseVector, argmax, shuffle}
import mnist.classification.neuralnetwork.layer._

import scala.collection.mutable.ArrayBuffer

class NeuralNet(val sizes: Array[Int],
                weights: Array[DenseMatrix[Double]] = null,
                biases: Array[DenseVector[Double]] = null) {

  private var _learningRate = 1.0
  def learningRate = _learningRate
  def learningRate_= (value:Double): Unit = _learningRate = value

  private var _miniBatchSize = 1.0
  def miniBatchSize = _miniBatchSize
  def miniBatchSize_= (value:Double): Unit = _miniBatchSize = value

  private var _ifRandomShuffle = true
  def ifRandomShuffle = _ifRandomShuffle
  def ifRandomShuffle_= (value:Boolean): Unit = _ifRandomShuffle = value

  val inputLayer = new InputLayer(sizes(0))

  val outputLayer = {
    var preLayer:BaseLayer = inputLayer

    var weight = if (weights == null) null else weights.head
    var bias = if (biases == null) null else biases.head

    for (nth <- 1 until sizes.length -1) {
      preLayer = new HiddenLayer(preLayer, sizes(nth), weight, bias)
      if (weight != null) weight = weights(nth)
      if (bias != null) bias = biases(nth)
    }

    new OutputLayer(preLayer, sizes.last, weight, bias)
  }

  
  def train(samplePairs: Array[(DenseVector[Double], DenseVector[Double])]): Unit = {
    val numSamples = samplePairs.length
    println("Number of samples: %d".format(numSamples))
    val order =
      if (_ifRandomShuffle)
        shuffle(0 until numSamples)
      else
        0 until numSamples

    for ((nth, count) <- order.zipWithIndex) {
      val (in, y) = samplePairs(nth)
//        Utils.printChar(in, y)
      inputLayer.in = in
      outputLayer.y = y

      // Forwarding calculation
      outputLayer.activation

      // backward propagation
      backPropagate()
      if (count % _miniBatchSize == 0)
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
      layer.update(_learningRate)
      if (layer.previous != inputLayer)
        layer = layer.previous.asInstanceOf[ForwardLayer]
      else
        layer = null
    }
  }

  def train(X: DenseMatrix[Double], Y: DenseMatrix[Double]): Unit = {
    assert(X.rows ==  Y.rows)

    val pairs = ArrayBuffer[(DenseVector[Double], DenseVector[Double])]()

    for(i <- 0 until X.rows) {
      pairs += ((X(i, ::).t, Y(i, ::).t))
    }
    train(pairs.toArray)
  }

  def train(Xs: Array[DenseVector[Double]], Ys: Array[DenseVector[Double]]): Unit = {
    // Assert shape
    assert(Xs.length == Ys.length)
    Xs.foreach(i => assert(i.length == Xs(0).length))
    Ys.foreach(i => assert(i.length == Ys(0).length))

    val pairs = ArrayBuffer[(DenseVector[Double], DenseVector[Double])]()
    for ((x, y) <- Xs zip Ys)
      pairs.append((x,y))

    train(pairs.toArray)
  }

  def evaluate(X: DenseMatrix[Double], Y: DenseVector[Int]): Unit = {
    assert(X.rows == Y.length)
    val numSamples = X.rows

    var correct = 0

    for (ith <- 0 until numSamples) {
      inputLayer.in = X(ith, ::).t

      if (argmax(outputLayer.activation) == Y.data(ith))
        correct += 1
    }
    println(s"$correct / $numSamples")
  }

}
