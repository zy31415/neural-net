package mnist.features

import mnist.ProjectConstant
import mnist.ProjectConstant.{ImageHalfWidth, ImageWidth}
import mnist.interpolation.NearestInterpolator
import org.apache.commons.math3.exception.OutOfRangeException

import scala.collection.mutable.ArrayBuffer

class ImageRotator(image: Array[Int]) {
  private val interpolator = new NearestInterpolator(
    ImageRotator.GridPoints,
    ImageRotator.GridPoints,
    image.grouped(ProjectConstant.ImageWidth).toArray
  )

  private def function(x: Double, y: Double): Double  =
    try {
      _function.value(x, y)
    } catch {
      case e: OutOfRangeException => 0.0
    }

  def rotate(alpha: Double): Array[Double] = {
    val cos = Math.cos(alpha)
    val sin = Math.sin(alpha)
    val result = ArrayBuffer[Double]()

    for (m <- ImageRotator.GridPoints) {
      for (n <- ImageRotator.GridPoints) {
        val _m = cos * m + sin * n
        val _n = -sin * m + cos * n
        result += function(_m, _n)
      }
    }
    result.toArray
  }
}

object ImageRotator {
  val GridPoints: Array[Int] =
    (- ImageHalfWidth + 1 to ImageHalfWidth by 1).toArray
}
