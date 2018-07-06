package mnist.features

import java.util.concurrent.{ExecutorService, Executors, Future}

import mnist.data.DataReader

import scala.collection.mutable.ListBuffer

class IntensityAndSymmetryCalculationThreadPool(val numThreads: Int =  4) {
  private val pool = Executors.newFixedThreadPool(numThreads)

  class CalculationResult (val label: Int, val index: Int, val intensity: Double, val symmetry: Double)

  private val labels = ListBuffer[Int]()

  private var _results = List[CalculationResult]()

  /**
    * Return results for this calculation.
    * @return a Map contains the following key-value elements:
    *         Label => An Array of tuple (index, intensity, symmetry)
    */
  def results: List[CalculationResult] = _results

  def addLabel(label: Int): Unit = labels.append(label)

  def calculate(): Unit = {

    for (label <- labels) {
      val (indices1, imageData1) = DataReader.getImageDataByLabelWithIndices(label).unzip
      _results ++= calculateIntensitiesAndSymmetries(pool, label, indices1, imageData1)
    }
    pool.shutdown()
  }

  private def calculateIntensitiesAndSymmetries(pool: ExecutorService,
                                                label: Int,
                                                indices: Array[Int],
                                                imageData: Array[Array[Int]]): List[CalculationResult]= {
    var resultsFuture = List[Future[CalculationResult]]()
    for((index, row) <- indices zip imageData)
      resultsFuture =  pool.submit(
        () => new CalculationResult(label,index, FeatureExtractor.intensity(row), new SymmetryChooser(row).symmetry())
      ) :: resultsFuture
    resultsFuture.map(_.get())
  }
}
