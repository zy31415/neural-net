package mnist.features

object FeatureExtractor {

  val NumWidth = 28
  val NumHeight = 28

  val IntensityScale = 255.0

  def intensity(image: Array[Int]): Double = {
    image.map(_/IntensityScale).sum/image.length
  }

  def symmetryHeight(image: Array[Int]): Double = {
    var result = 0.0
    for {
      m <- 0 until NumHeight
      n <- 0 until NumWidth
    } result += Math.abs(image(m * NumWidth + n) - image((NumHeight - m - 1) * NumWidth + n))/IntensityScale

    -result/image.length
  }

  def symmetryWidth(image: Array[Int]): Double = {
    var result = 0.0
    for {
      m <- 0 until NumHeight
      n <- 0 until NumWidth
    } result += Math.abs(image(m * NumWidth + n) - image(m * NumWidth + NumHeight - n - 1))/IntensityScale

    -result/image.length
  }
}
