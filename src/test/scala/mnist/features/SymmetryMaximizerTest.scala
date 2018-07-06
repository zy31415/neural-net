package mnist.features

import java.nio.file.Paths

import javax.imageio.ImageIO
import mnist.data.{DataReader, MyMnistReader}
import org.scalatest.FunSuite

class SymmetryMaximizerTest extends FunSuite {
  test("Rotation Symmetry") {
    val nth = 99

    val imageData = DataReader.getAllImageDataMatrix()(nth)

    val image = MyMnistReader.asBufferedImage(imageData)

    val tempPath = "/Users/zy/Documents/workspace/mnist/temp"

    ImageIO.write(image, "png",
      Paths.get(tempPath, s"low-symmetry.png").toFile)

    val sym = new SymmetryMaximizer(imageData)
    println(sym.symmetry)
    println(sym.vals)
    println(sym.alphas.map(_*180.0/Math.PI))
  }

  test("") {
    val images1 = MyMnistReader.getImageData(1)

    val symmetries = images1.map(FeatureExtractor.symmetryHeight _)

    var n = 0
    for {
      (imageData, sym) <- images1 zip symmetries
      if sym < -0.2
    } {
      val image = MyMnistReader.asBufferedImage(imageData)

      val tempPath = "/Users/zy/Documents/workspace/mnist/temp"

      ImageIO.write(image, "png",
        Paths.get(tempPath, s"low-symmetry-$n.png").toFile)

      val sym = new SymmetryMaximizer(imageData)
      println(sym.symmetry, sym.alpha * 180.0 / Math.PI)

      n += 1
    }
  }

}
