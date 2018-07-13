package mnist.data

import hdf.`object`.{Datatype, FileFormat, Group}
import hdf.`object`.h5.H5File
import javax.swing.tree.DefaultMutableTreeNode

import scala.collection.mutable.ArrayBuffer


object GenerateData {

  def selectOnesAndFives(outputPath: String): Unit = {

    val imageData = MyMnistReader.trainImageData
    val labels = MyMnistReader.trainLabels

    var nth = 0
    val labelsChoosen = ArrayBuffer[Int]()
    val imageDataChoosen = ArrayBuffer[Array[Int]]()
    val indexChoosen = ArrayBuffer[Int]()

    for ((d, l) <- imageData zip labels) {
      if (l == 1 || l == 5) {
        indexChoosen += nth
        labelsChoosen += l
        imageDataChoosen += d
      }
      nth += 1
    }

    save(outputPath, indexChoosen.toArray, labelsChoosen.toArray, imageDataChoosen.toArray)

  }

  private def save(filename: String, indices: Array[Int], labels: Array[Int], imageData: Array[Array[Int]]): Unit = {
    val fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5)
    // create a new file with a given file name.
    val testFile = fileFormat.createFile(
      filename,
      FileFormat.FILE_CREATE_DELETE
    ).asInstanceOf[H5File]

    testFile.open()

    val root = testFile.getRootNode.asInstanceOf[DefaultMutableTreeNode].getUserObject.asInstanceOf[Group]

    val g1 = testFile.createGroup("Data", root)

    val dtype = testFile.createDatatype(
      Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE)

    val imageDataDS = testFile.createScalarDS(
      "Image Data", g1, dtype,
      Array[Long](imageData.length, imageData(0).length),
      null, null, 0,
      imageData
    )

    val labelsDS = testFile.createScalarDS(
      "labels", g1, dtype,
      Array[Long](imageData.length, 1),
      null, null, 0,
      labels
    )

    val indicesDS = testFile.createScalarDS(
      "indices", g1, dtype,
      Array[Long](imageData.length, 1),
      null, null, 0,
      indices
    )

    testFile.close()

  }
}
