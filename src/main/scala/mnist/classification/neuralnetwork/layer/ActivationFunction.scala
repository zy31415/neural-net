package mnist.classification.neuralnetwork.layer

import breeze.linalg._
import breeze.numerics._


trait ActivationFunction {
  def apply(z: DenseVector[Double]): DenseVector[Double]
  def apply(z: DenseMatrix[Double]): DenseMatrix[Double]
  def d(z: DenseVector[Double]): DenseVector[Double]
  def d(z: DenseMatrix[Double]): DenseMatrix[Double]
}

// TODO: Use UFunc to rewrite this part so that you don't need to write one function twice separately for vectors and matrices
class SigmoidFunction extends ActivationFunction {

  def apply(z: DenseVector[Double]): DenseVector[Double] = 1.0 /(exp(-z) + 1.0)
  def apply(z: DenseMatrix[Double]): DenseMatrix[Double] = 1.0 /(exp(-z) + 1.0)

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
  def d(z: DenseVector[Double]): DenseVector[Double]= {
    val v = apply(z)
    v * (1.0 - v)
  }

  def d(z: DenseMatrix[Double]): DenseMatrix[Double]= {
    val v = apply(z)
    v * (1.0 - v)
  }

}
