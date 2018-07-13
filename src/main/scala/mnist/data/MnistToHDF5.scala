package mnist.data

import java.nio.file.Paths

import hdf.`object`.{Datatype, Group}
import javax.swing.tree.DefaultMutableTreeNode


object MnistToHDF5 extends HDF5IO {

  override var datafile = Paths.get(getClass.getResource("/").getPath, "mnist.h5").toString

  def genrate():Unit = {
    create()
    val root = hdf5File.getRootNode.asInstanceOf[DefaultMutableTreeNode].getUserObject.asInstanceOf[Group]

    val groupTrain = hdf5File.createGroup("train", root)
    addTrainImages(groupTrain)
    addTrainLabels(groupTrain)

    val groupTest = hdf5File.createGroup("test", root)
    addTestImages(groupTest)
    addTestLabels(groupTest)

    close()

  }

  private def addTrainImages(group: Group): Unit = {
    val dtype = hdf5File.createDatatype(
      Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE)

    val trainImage = MyMnistReader.trainImageData

    hdf5File.createScalarDS(
      "images", group, dtype,
      Array[Long](trainImage.length, trainImage(0).length),
      null, null, 0,
      trainImage
    )
  }

  private def addTrainLabels(group: Group): Unit = {

    val trainLabels = MyMnistReader.trainLabels

    val dtype = hdf5File.createDatatype(
      Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE)

    hdf5File.createScalarDS(
      "labels", group, dtype,
      Array[Long](trainLabels.length, 1),
      null, null, 0,
      trainLabels
    )
  }

  private def addTestImages(group: Group): Unit = {
    val testImage = MyMnistReader.testImageData

    val dtype = hdf5File.createDatatype(
      Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE)

    hdf5File.createScalarDS(
      "images", group, dtype,
      Array[Long](testImage.length, testImage(0).length),
      null, null, 0,
      testImage
    )
  }

  private def addTestLabels(group: Group): Unit = {

    val testLabels = MyMnistReader.testLabels

    val dtype = hdf5File.createDatatype(
      Datatype.CLASS_INTEGER, 4, Datatype.NATIVE, Datatype.NATIVE)

    hdf5File.createScalarDS(
      "labels", group, dtype,
      Array[Long](testLabels.length, 1),
      null, null, 0,
      testLabels
    )
  }
}
