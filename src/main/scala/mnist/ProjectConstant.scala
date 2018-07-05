package mnist

object ProjectConstant {
  val ImageWidth = 28
  val ImageHeight = ImageWidth
  val numPixels = ImageWidth * ImageHeight
  val ImageHalfWidth = ImageWidth/2
  val GridPoints: Array[Int] =
    (- ImageHalfWidth + 1 to ImageHalfWidth by 1).toArray
}
