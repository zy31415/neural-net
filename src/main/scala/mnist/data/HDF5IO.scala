package mnist.data

import hdf.`object`.FileFormat
import hdf.`object`.h5.H5File

trait HDF5IO {
  val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
  var datafile: String
  var hdf5File: FileFormat = _

  protected def open():Int = {
    hdf5File = fileFormat.createInstance(datafile, FileFormat.READ)
    hdf5File.open
  }

  protected def create() = {
    // create a new file with a given file name.
    hdf5File = fileFormat.createFile(
      datafile,
      FileFormat.FILE_CREATE_DELETE
    ).asInstanceOf[H5File]
    hdf5File.open()
  }

  protected def close(): Unit = hdf5File.close()
}
