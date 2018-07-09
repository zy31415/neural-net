package mnist.data

import hdf.`object`.FileFormat

trait HDF5IO {
  val datafile: String
  var hdf5File: FileFormat = _

  protected def open():Int = {
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    hdf5File = fileFormat.createInstance(datafile, FileFormat.READ)
    hdf5File.open
  }

  protected def close(): Unit = hdf5File.close()
}
