package mnist.data

import hdf.`object`.{Dataset, FileFormat}

import scala.collection.mutable.ListBuffer

object DataReader {
  val datafile = getClass.getResource("/data.h5").getPath

  private var hdf5File: FileFormat = _

  private var _imageData: Array[Array[Int]] = _
  private var _labels: Array[Int] = _
  private var _indices: Array[Int] = _

  def getAllImageDataAsMatrix(): Array[Array[Int]] = {
    if (_imageData == null) {
      open()
      val dataset = hdf5File.get("/Data/Image Data").asInstanceOf[Dataset]
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

  def getImage(index: Int): MnistImage = {
    open()
    val dataset = hdf5File.get("/Data/Image Data").asInstanceOf[Dataset]
    assert(dataset != null)

    // Lazy initialization
    dataset.init()

    val startDims = dataset.getStartDims
    startDims(0) = index
    startDims(1) = 0

    val size = dataset.getSelectedDims
    size(0) = 1

    val results = dataset.getData
    close()
    new MnistImage(results.asInstanceOf[Array[Int]], getAllLabels()(index), getAllIndices()(index), index)
  }

  def getAllLabels(): Array[Int] = {
    if (_labels == null) {
      open()
      val dataset = hdf5File.get("/Data/labels").asInstanceOf[Dataset]
      assert(dataset != null)
      val results = dataset.getData
      close()
      _labels = results.asInstanceOf[Array[Int]]
    }
    _labels
  }

  def getAllIndices(): Array[Int] = {
    if (_indices == null) {
      open()
      val dataset = hdf5File.get("/Data/indices").asInstanceOf[Dataset]
      assert(dataset != null)
      val results = dataset.getData
      close()
      _indices = results.asInstanceOf[Array[Int]]
    }
    _indices
  }

  private def open():Int = {
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    hdf5File = fileFormat.createInstance(datafile, FileFormat.READ)
    hdf5File.open
  }

  private def close(): Unit = hdf5File.close()

  def getImageDataByLabel(label: Int) = {
    val imageData = getAllImageDataAsMatrix()
    val labels = getAllLabels()

    val results = ListBuffer[Array[Int]]()
    for ((i, l) <- imageData zip labels if l == label)
      results.append(i)
    results.toArray
  }



  /**
    * Return (index, imageData) pair picked by labels.
    *   Index is the index in the original MNIST dataset.
    *
    * @param label
    * @return
    */
  @deprecated def getImageDataByLabelWithIndices(label: Int): Array[(Int, Array[Int])] = {
    val imageData = getAllImageDataAsMatrix()
    val labels = getAllLabels()
    val indices = getAllIndices()

    val results = ListBuffer[(Int, Array[Int])]()
    for ((index, data, l) <- (indices, imageData, labels).zipped.toList if l == label)
      results.append((index, data))
    results.toArray
  }

  /**
    * Return an array of MnistImage objects.
    *
    * @param label
    * @return
    */
  def getMnistImageByLabel(label: Int): Array[MnistImage] = {
    val imageData = getAllImageDataAsMatrix()
    val labels = getAllLabels()
    val indices = getAllIndices()

    val results = ListBuffer[MnistImage]()

    val n = 0
    for ((index, data, l) <- (indices, imageData, labels).zipped.toList if l == label) {
      results.append(new MnistImage(data, l, index, n))
    }

    results.toArray
  }
}
