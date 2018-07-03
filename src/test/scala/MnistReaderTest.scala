import java.io.File

//import hdf.hdf5lib.{H5, HDF5Constants}
import javax.imageio.ImageIO
import mnist.data.MyMnistReader
import org.scalatest.FunSuite

class MnistReaderTest extends FunSuite{

  test("test MnistReader") {
    val nth = 20000
    val image = MyMnistReader.images(nth)
    val label = MyMnistReader.labels(nth)
    ImageIO.write(image, "png",
      new File(s"number-$label.png"))
  }

//  test("reader") {
//    val fname = "/Users/zy/Documents/workspace/mnist/temp/test.h5"
//
//    val file_id = H5.H5Fcreate(fname, HDF5Constants.H5F_ACC_TRUNC,
//      HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
//
//    H5.H5Fclose(file_id)
//  }
}
