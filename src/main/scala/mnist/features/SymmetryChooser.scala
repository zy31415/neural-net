package mnist.features

import scala.collection.mutable.ArrayBuffer

/**
  *  Try several predefined rotation angles and pick the best one
  */
class SymmetryChooser(val imageData: Array[Int]) {

  var sym: Double = Double.MinValue
  var alpha: Double = _
  val allSyms = ArrayBuffer[Double]()

  def symmetry(): Double = {
    val rotator = new ImageRotator(imageData)
    SymmetryChooser.angles.foreach(
      a => {
        val rotatedImage = rotator.rotate(a * Math.PI / 180.0)
        val newSymmetry = FeatureExtractor.symmetryHeight(rotatedImage)
        allSyms += newSymmetry

        if (newSymmetry > sym) {
          sym = newSymmetry
          alpha = a
        }
      }
    )
    sym
  }

}

object SymmetryChooser {
  val angles = -95 to 95 by 1 map(_.toDouble)
}
