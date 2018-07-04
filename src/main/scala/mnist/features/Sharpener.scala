package mnist.features

object Sharpener {
  def sharpen(imageData: Array[Int]): Array[Int] =
    imageData.map(a => Math.round(a.toDouble / 255.0).toInt * 255)
}
