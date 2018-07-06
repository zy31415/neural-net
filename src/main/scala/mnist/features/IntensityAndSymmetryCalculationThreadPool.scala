package mnist.features

import java.util.concurrent.{ExecutorService, Executors, Future}
import mnist.data.{DataReader, MnistImage}
import scala.collection.mutable.ListBuffer


class IntensityAndSymmetryCalculationThreadPool(val numThreads: Int =  4) {

  private val pool = Executors.newFixedThreadPool(numThreads)

  private val labels = ListBuffer[Int]()

  private var _results = List[MnistImage]()

  def results: List[MnistImage] = _results

  def addLabel(label: Int): Unit = labels.append(label)

  def calculate(): Unit = {

    for (label <- labels) {
      val images = DataReader.getMnistImageByLabel(label)

      _results ++= calculateIntensitiesAndSymmetries(pool, images)
    }
    pool.shutdown()
  }

  private def calculateIntensitiesAndSymmetries(pool: ExecutorService,
                                                images: Array[MnistImage]): List[MnistImage]= {
    var resultsFuture = List[Future[MnistImage]]()
    for(image <- images)
      resultsFuture =  pool.submit(
        () => {
          val chooser = new SymmetryChooser(image.data)
          image.properties += (
            "intensity" -> FeatureExtractor.intensity(image.data),
            "symmetry" -> chooser.symmetry(),
            "alpha" -> chooser.alpha,
          )
          image
        }
      ) :: resultsFuture
    resultsFuture.map(_.get())
  }
}
