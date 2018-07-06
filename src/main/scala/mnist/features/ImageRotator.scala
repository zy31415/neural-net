package mnist.features

import mnist.data.MnistImage
import mnist.interpolation.NearestNeighborInterpolator

import scala.collection.mutable.ArrayBuffer

/**
  * Rotate image.
  * @param image
  */
class ImageRotator(image: Array[Int]) {
  private val interpolator = new NearestNeighborInterpolator(
    ImageRotator.GridPoints,
    ImageRotator.GridPoints,
    image
  )

  private def function(x: Double, y: Double): Int  =
    interpolator.interpolate(x, y)

  def rotate(alpha: Double): Array[Int] = {
    val cos = Math.cos(alpha)
    val sin = Math.sin(alpha)
    val result = ArrayBuffer[Int]()

    for (y <- ImageRotator.GridPoints) {
      for (x <- ImageRotator.GridPoints) {
        val _x = cos * x + sin * y
        val _y = - sin * x + cos * y
        result += function(_x, _y)
      }
    }
    result.toArray
  }
}

object ImageRotator {
  val GridPoints = MnistImage.GridPoints
}
