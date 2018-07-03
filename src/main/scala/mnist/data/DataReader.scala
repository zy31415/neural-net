package mnist.data

import hdf.`object`.{Dataset, FileFormat, Group, ScalarDS}
import javax.swing.tree.DefaultMutableTreeNode

object DataReader {
  val datafile = getClass.getResource("/data.h5").getPath

  private var testFile: FileFormat = _

  def getImageData(): Array[Array[Int]] = {
    open()

    val dataset = testFile.get("/Data/Image Data").asInstanceOf[Dataset]
    assert(dataset != null)

    // Lazy initialization
    dataset.init()

    val numCol = dataset.getDims()(1).toInt
    val results = dataset.getData

    close()

    results.asInstanceOf[Array[Int]].grouped(numCol).toArray
  }

  def getLabels(): Array[Int] = {
    open()
    val dataset = testFile.get("/Data/labels").asInstanceOf[Dataset]
    assert(dataset != null)
    val results = dataset.getData
    close()
    results.asInstanceOf[Array[Int]]
  }

  private def open():Int = {
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    testFile = fileFormat.createInstance(datafile, FileFormat.READ)
    testFile.open
  }

  private def close(): Unit = testFile.close()

}
