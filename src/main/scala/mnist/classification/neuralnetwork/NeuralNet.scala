package mnist.classification.neuralnetwork

import breeze.linalg.{DenseMatrix, DenseVector, argmax, shuffle}
import mnist.classification.neuralnetwork.layer._

import scala.collection.mutable.ArrayBuffer

class NeuralNet(val sizes: Array[Int],
                val ifRandomShuffle: Boolean = true,
                val learningRate: Double = 0.1,
                val miniBatchSize: Int = 100,
                weights: Array[DenseMatrix[Double]] = null,
                biases: Array[DenseVector[Double]] = null) {

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


  def train(samplePairs: Array[(DenseVector[Double], DenseVector[Double])], numEpochs: Int): Unit = {
    val numSamples = samplePairs.length

    println("Number of samples: %d".format(numSamples))

    for (ithEpoch <- 0 until numEpochs) {
      val order =
        if (ifRandomShuffle)
          shuffle(0 until numSamples)
        else
          0 until numSamples

      for ((nth, count) <- order.zipWithIndex) {
        val (in, y) = samplePairs(nth)

//        Utils.printChar(in, y)

        inputLayer.in = in
        outputLayer.y = y

        // Forwarding calculation
        outputLayer.out

        // backward propagation
        backPropagate()
        if (count % miniBatchSize == 0)
          update()
      }

      if (outputLayer.C_ws.nonEmpty)
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

  def train(X: DenseMatrix[Double], Y: DenseMatrix[Double], numEpoch: Int): Unit = {
    assert(X.rows ==  Y.rows)

    val pairs = ArrayBuffer[(DenseVector[Double], DenseVector[Double])]()

    for(i <- 0 until X.rows) {
      pairs += ((X(i, ::).t, Y(i, ::).t))
    }
    train(pairs.toArray, numEpoch)
  }

  def train(Xs: Array[DenseVector[Double]], Ys: Array[DenseVector[Double]], numEpoch: Int): Unit = {
    // Assert shape
    assert(Xs.length == Ys.length)
    Xs.foreach(i => assert(i.length == Xs(0).length))
    Ys.foreach(i => assert(i.length == Ys(0).length))

    val pairs = ArrayBuffer[(DenseVector[Double], DenseVector[Double])]()
    for ((x, y) <- Xs zip Ys)
      pairs.append((x,y))

    train(pairs.toArray, numEpoch)
  }

  def evaluate(X: DenseMatrix[Double], Y: DenseVector[Int]): Unit = {
    assert(X.rows == Y.length)
    val numSamples = X.rows

    var correct = 0

    for (ith <- 0 until numSamples) {
      inputLayer.in = X(ith, ::).t

      if (argmax(outputLayer.out) == Y.data(ith))
        correct += 1
    }
    println(s"$correct / $numSamples")
  }

}
