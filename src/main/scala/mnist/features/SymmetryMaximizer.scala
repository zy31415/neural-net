package mnist.features

/**
  * This class uses Newton's method to maximize symmetry with respect to rotation.
  *
  * However, since the symmetry to rotation function is not smooth locally
  *   (we are handling non-continuous pixels), this method didn't
  *   succeed.
  *
  * @param image image data to find max symmetry
  */
class SymmetryMaximizer(image: Array[Int]) {

  val rotator = new ImageRotator(image)

  var alpha = 0.0
  var alphas = List[Double](alpha)

  var vals = List[Double](-1.0)

  def symmetry: Double = {
    var v = func(alpha)
    var n = 0
    while ((Math.abs(v - vals.head) > SymmetryMaximizer.StopCriterion) && n < SymmetryMaximizer.MaxCycle){
      alpha -= dfOverDdf(alpha)
      alphas = alpha :: alphas
      vals = v :: vals
      v = func(alpha)
      n += 1
    }

    vals = v :: vals
    -vals.head
  }

  def func(alpha: Double): Double =
    FeatureExtractor.symmetryHeight( rotator.rotate(alpha))

  private def dfOverDdf(alpha: Double): Double = {
    val d = SymmetryMaximizer.DeltaAlpha
    val f = func(alpha)
    val f_D = func(alpha + d)
    val f_2D = func(alpha + 2.0 * d)
    d * (f_D - f) / (f_2D - 2.0 * f_D + f)
  }
}

object SymmetryMaximizer {
  val ImageWidth = 28
  val ImageHeight = 28
  val IntensityScale = 255.0

  val DeltaAlpha = 5 * Math.PI/180.0
  val StopCriterion = 0.001

  val MaxCycle = 100
}
