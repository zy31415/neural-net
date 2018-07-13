import java.io.File

//import hdf.hdf5lib.{H5, HDF5Constants}
import javax.imageio.ImageIO
import mnist.data.MyMnistReader
import org.scalatest.FunSuite

class MnistReaderTest extends FunSuite{

  test("test MnistReader") {
    val nth = 20000
    val image = MyMnistReader.images(nth)
    val label = MyMnistReader.trainLabels(nth)
    ImageIO.write(image, "png",
      new File(s"number-$label.png"))
  }
}
