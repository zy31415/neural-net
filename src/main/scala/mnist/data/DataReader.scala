package mnist.data

import hdf.`object`.{Dataset, FileFormat}

object DataReader {
  val datafile = getClass.getResource("/data.h5").getPath

  private var testFile: FileFormat = _

  private var _imageData: Array[Array[Int]] = _
  private var _labels: Array[Int] = _

  def getImageData(): Array[Array[Int]] = {
    if (_imageData == null) {
      open()
      val dataset = testFile.get("/Data/Image Data").asInstanceOf[Dataset]
      assert(dataset != null)
      // Lazy initialization
      dataset.init()
      val numCol = dataset.getDims()(1).toInt
      val results = dataset.getData
      close()
      _imageData = results.asInstanceOf[Array[Int]].grouped(numCol).toArray
    }
    _imageData
  }

  def getLabels(): Array[Int] = {
    if (_labels == null) {
      open()
      val dataset = testFile.get("/Data/labels").asInstanceOf[Dataset]
      assert(dataset != null)
      val results = dataset.getData
      close()
      _labels = results.asInstanceOf[Array[Int]]
    }
    _labels
  }

  private def open():Int = {
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    testFile = fileFormat.createInstance(datafile, FileFormat.READ)
    testFile.open
  }

  private def close(): Unit = testFile.close()

  def getImageDataWithLabel(label: Int) = {
    val imageData = getImageData()
    val labels = getLabels()

    var results = List[Array[Int]]()
    for ((i, l) <- imageData zip labels if l == label)
      results = i :: results
    results.toArray
  }
}
