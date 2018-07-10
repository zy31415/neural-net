package mnist.classification.neuralnetwork.layer

trait ActivationFunction {
  def apply(z: Double): Double
  def d(z: Double): Double
}

class SigmoidFunction extends ActivationFunction {
  def apply(z: Double): Double = 1.0 / (1.0 + Math.exp(-z))

  /**
    * Note to avoid NaN output here.
    *
    * If you compute the value as:
    *   exp(-z)/(1 + exp(-z))^2
    *
    *   You likely to get NaN where z is a large negative value (Infinity/Infinity)
    *
    * @param z
    * @return
    */
  def d(z: Double): Double = {
    val v = apply(z)
    v * (1.0 - v)
  }

}
