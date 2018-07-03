import java.awt.Color
import java.awt.geom.{Ellipse2D, Rectangle2D}
import java.nio.file.Paths

import javax.imageio.ImageIO
import mnist.data.MyMnistReader
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.{ChartFactory, ChartUtils}
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import org.scalatest.FunSuite

class ClassificationTest extends FunSuite {
  test("test") {
    val images5 = MyMnistReader.getImageData(5)
    val images1 = MyMnistReader.getImageData(1)

    val dataset = new XYSeriesCollection()

//    addSeries(dataset, images5, "5")
//    addSeries(dataset, images1, "1")
    addSeriesSymmetryRotation(dataset, images5, "5")
    addSeriesSymmetryRotation(dataset, images1, "1")


    // Create chart// Create chart

    val chart = ChartFactory.createScatterPlot(
      "Intensity vs symmetry",
      "Intensity", "Symmetry",
      dataset)

    val plot = chart.getPlot.asInstanceOf[XYPlot]

    plot.getDomainAxis.setRange(0, 0.4)
    plot.getRangeAxis().setRange(-0.4, 0.0)

    val renderer = plot.getRenderer

    renderer.setSeriesPaint(0, new Color(1.0f, 0.0f, 0.0f, 0.1f))
    renderer.setSeriesPaint(1, new Color(0.0f, 1.0f, 0.0f, 0.1f))


    val size = 5.0
    val delta = size / 2.0
    val shape1 = new Rectangle2D.Double(-delta, -delta, size, size)
    val shape2 = new Ellipse2D.Double(-delta, -delta, size, size)
    renderer.setSeriesShape(0, shape1)
    renderer.setSeriesShape(1, shape2)

    ChartUtils.saveChartAsJPEG(
      Paths.get("/Users/zy/Documents/workspace/mnist/temp", "chart.jpg").toFile,
      chart, 500, 500, null)
  }

  private def addSeries(dataset: XYSeriesCollection, images: List[Array[Int]], key: String): Unit = {
    val it = images.map(FeatureExtractor.intensity _ )
    val sym = images.map(FeatureExtractor.symmetryHeight _)

    val series1 = new XYSeries(key)

    for ((i, s) <- it zip sym) {
      series1.add(i, s)
    }
    dataset.addSeries(series1)
  }

  private def addSeriesSymmetryRotation(dataset: XYSeriesCollection, images: List[Array[Int]], key: String): Unit = {
    val it = images.map(FeatureExtractor.intensity _ )
    val sym = images.map(new RotationSymmetry(_).symmetry)

    val series1 = new XYSeries(key)

    for ((i, s) <- (it zip sym)) {
      series1.add(i, s)
    }
    dataset.addSeries(series1)
  }

  test("low symmetry") {
    val images1 = MyMnistReader.getImageData(1)

    val symmetries = images1.map(FeatureExtractor.symmetryHeight _)

    var n = 0
    for {
      (imageData, sym) <- images1 zip symmetries
      if sym < -0.2
    } {
      val image = MyMnistReader.asBufferedImage(imageData)

      val tempPath = "/Users/zy/Documents/workspace/mnist/temp"

      ImageIO.write(image, "png",
        Paths.get(tempPath, s"low-symmetry-$n.png").toFile)
      n += 1
    }
  }

}
