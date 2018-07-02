import java.io.File

import javax.imageio.ImageIO
import org.scalatest.FunSuite

class MnistReaderTest extends FunSuite{

  test("test MnistReader") {
    val nth = 10000
    val image = MyMnistReader.images(nth)
    val label = MyMnistReader.labels(nth)
    ImageIO.write(image, "png",
      new File(s"number-$label.png"))
  }
}
