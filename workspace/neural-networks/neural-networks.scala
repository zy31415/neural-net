import breeze.linalg.DenseMatrix
import mnist.classification.neuralnetwork.layer.{HiddenLayer, InputLayer, OutputLayer}
import mnist.classification.neuralnetwork.{InputLayer, OutputLayer}
import mnist.data.{DataReader, MnistImage}

val nth = 100
val data = DataReader.getImage(nth).data.map(_.toDouble)

val inputData = new DenseMatrix[Double](data.length, 1, data)

val inputLayer = new InputLayer(MnistImage.numPixels)
inputLayer.in = inputData

val layer1 = new HiddenLayer(inputLayer, 15)

val outputLayer = new OutputLayer(layer1, numNodes = 10)

// Forwarding:
println(outputLayer.activation)

val label = DataReader.getAllLabels()(nth)
val y = DenseMatrix.zeros[Double](10, 1)
y(label, 0) = 1.0

inputLayer.in = inputData
outputLayer.y = y

outputLayer.backPropagate
layer1.backPropagate


println(outputLayer.C_w)
println(outputLayer.C_b)

println(layer1.C_w)
println(layer1.C_b)