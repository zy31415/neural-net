package mnist.features

import mnist.data.DataReader
import org.scalatest.FunSuite

class SymmetryChooserTest extends FunSuite {

  test("symmetry") {
    val nth = 99
    val imageData = DataReader.getImageData()(nth)
    val chooser = new SymmetryChooser(imageData)
    println(chooser.symmetry(), chooser.alpha)
    println(chooser.allSyms)
  }

}
