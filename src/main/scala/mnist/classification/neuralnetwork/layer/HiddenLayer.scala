package mnist.classification.neuralnetwork.layer

class HiddenLayer(override val previous: BaseLayer, override val numNodes: Int) extends ForwardLayer(previous, numNodes) {
  override def delta = next.weights.t * next._delta *:* z.map(activation.d(_))
}