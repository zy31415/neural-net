package mnist.features

object Sharpener {
  val TRUE = 255
  def sharpen(imageData: Array[Int]): Array[Int] =
    imageData.map(a => Math.round(a.toDouble / 255.0).toInt * TRUE)
}
