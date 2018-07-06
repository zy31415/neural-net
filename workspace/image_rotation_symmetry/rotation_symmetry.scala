import java.io.File

import mnist.data.DataReader
import mnist.features.{FeatureExtractor, ImageRotator}
import org.jfree.chart.ChartFactory
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import org.jfree.chart.ChartUtils

import scala.collection.mutable.ArrayBuffer


def getRotationSymmetry(angles: Array[Double], nth: Integer): Array[Double] = {
  val imageData = DataReader.getAllImageDataMatrix()(nth)
  val rotator = new ImageRotator(imageData)
  val symmetry = ArrayBuffer[Double]()
  for (r <- angles) {
    val rotatedImage = rotator.rotate(r * Math.PI / 180.0)
    symmetry += FeatureExtractor.symmetryHeight(rotatedImage)
  }
  symmetry.toArray
}

def addToDataset(rs: Array[Double], s: Array[Double], key: String, dataset: XYSeriesCollection):Unit = {
  val series = new XYSeries(key)
  for((ri, si) <- rs zip s) {
    series.add(ri, si)
  }
  dataset.addSeries(series)
}

val rs = (0 to 360 by 1).map(_.toDouble).toArray

val nth5 = 198
val nth1 = 12159

val s100 = getRotationSymmetry(rs, nth5)
val s1000 = getRotationSymmetry(rs, nth1)




val dataset = new XYSeriesCollection

addToDataset(rs, s100, s"sample $nth5 (5)", dataset)
addToDataset(rs, s1000, s"sample $nth1 (1)", dataset)

val xylineChart = ChartFactory.createXYLineChart(
  "Rotation Symmetry", "rotation", "symmetry", dataset, PlotOrientation.VERTICAL,
  true, true, false)

val width = 640 /* Width of the image */
val height = 480 /* Height of the image */
val XYChart = new File("workspace/image_rotation_symmetry/XYLineChart.jpeg")
ChartUtils.saveChartAsJPEG(XYChart, xylineChart, width, height)