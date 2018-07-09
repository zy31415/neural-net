package mnist.classification.neuralnetwork

class HiddenLayer(override val preLayer: BaseLayer, override val numNodes: Int) extends ForwardLayer(preLayer, numNodes) {
  override def delta() = nextLayer.weights.t * nextLayer._delta *:* z.map(activation.d(_))
}