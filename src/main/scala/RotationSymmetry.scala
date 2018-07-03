import org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolator
import org.apache.commons.math3.exception.OutOfRangeException

class RotationSymmetry(image: Array[Int]) {
  private val interpolator = new BicubicSplineInterpolator()
  private val function = interpolator.interpolate(
    (-13 to 14).toArray.map(_.toDouble),
    (-13 to 14).toArray.map(_.toDouble),
    image.map(_.toDouble).grouped(RotationSymmetry.ImageWidth).toArray)

  var alpha = 0.0
  var alphas = List[Double](alpha)

  var vals = List[Double](-1.0)

  def symmetry: Double = {
    var v = func(alpha)
    var n = 0
    while ((Math.abs(v - vals.head) > RotationSymmetry.StopCriterion) && n < 3){
      alpha -= dfOverDdf(alpha)
      alphas = alpha :: alphas
      vals = v :: vals
      v = func(alpha)
      n += 1
    }

    vals = v :: vals
    -vals.head
  }

  def func(alpha: Double): Double = {
    var result = 0.0
    for {
      m <- -13 to 14
      n <- -13 to 14
    } {
      val cos = Math.cos(alpha)
      val sin = Math.sin(alpha)
      val _m = cos * m - sin * n
      val _n = sin * m + cos * n

      result += Math.abs(interpolatedFunc(_m, _n) - interpolatedFunc(_m, -_n))/RotationSymmetry.IntensityScale
    }

    result / RotationSymmetry.ImageWidth / RotationSymmetry.ImageHeight
  }

  private def dfOverDdf(alpha: Double): Double = {
    val d = RotationSymmetry.DeltaAlpha
    val f = func(alpha)
    val f_D = func(alpha + d)
    val f_2D = func(alpha + 2.0 * d)
    d * (f_D - f) / (f_2D - 2.0 * f_D + f)
  }

  private def interpolatedFunc(x: Double, y: Double): Double  = {
    try {
      function.value(x, y)
    } catch {
      case e: OutOfRangeException => 0.0
    }
  }
}

object RotationSymmetry {
  val ImageWidth = 28
  val ImageHeight = 28
  val IntensityScale = 255.0

  val DeltaAlpha = 0.1 * Math.PI/180.0
  val StopCriterion = 0.001
}
