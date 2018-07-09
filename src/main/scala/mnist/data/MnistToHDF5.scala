package mnist.data

import hdf.`object`.Dataset
import mnist.data.DataReader.{close, open, testFile}


object MnistToHDF5 extends HDF5IO {

  override val datafile = getClass.getResource("/mnist.h5").getPath

  def convert():Unit = {
    open()
    val dataset = hdf5File.get("/train/image").asInstanceOf[Dataset]
    assert(dataset != null)
    // Lazy initialization
    dataset.init()
    val numCol = dataset.getDims()(1).toInt
    val results = dataset.getData
    close()

    for ((image, label) <- MyMnistReader.trainImageData zip MyMnistReader.trainLabels) {

    }
  }
}
