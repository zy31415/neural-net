package mnist.data

class MnistImage(val data: Array[Int], val index0: Int, val index1: Int) {

  override def toString: String = {
    val builder = new StringBuilder
    builder ++= "=" * 2 * MnistImage.ImageWidth += '\n'
    builder ++= s"MNIST Index: $index0, Data Index: $index1\n"
    builder ++= "-" * 2 * MnistImage.ImageWidth += '\n'
    builder ++= MnistImage.charPlot(data)
    builder ++= "=" * 2 * MnistImage.ImageWidth += '\n'

    builder.mkString
  }

}

object MnistImage {
  val ImageWidth = 28
  val ImageHeight = ImageWidth
  val numPixels = ImageWidth * ImageHeight
  val ImageHalfWidth = ImageWidth/2
  val GridPoints: Array[Int] =
    (- ImageHalfWidth + 1 to ImageHalfWidth by 1).toArray

  private def charPlot(imageData: Array[Int]): String = {
    val builder = new StringBuilder
    for ((a, n) <- imageData.zipWithIndex){
      if (a != 0 )
        builder ++= "* "
      else
        builder ++= "  "
      if (n % 28 == 0)
        builder += '\n'
    }
    builder += '\n'
    builder.mkString
  }
}