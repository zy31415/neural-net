package mnist.classification.neuralnetwork

trait ActivationFunction {
  def apply(z: Double): Double
  def d(z: Double): Double
}

class SigmoidFunction extends ActivationFunction {
  def apply(z: Double): Double = 1.0 / (1.0 + Math.exp(-z))

  def d(z: Double): Double = {
    val v1 = Math.exp(-z)
    val v2 = 1.0 + v1
    v1/v2/v2
  }

}
