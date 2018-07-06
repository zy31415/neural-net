package mnist.plot

import java.awt.Color
import java.awt.geom.{Ellipse2D, Rectangle2D}
import java.io.File

import mnist.data.MnistImage
import mnist.features.IntensityAndSymmetryCalculationThreadPool
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.{ChartFactory, ChartUtils}
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}


/**
  *  Plot symmetry vs intensity for all ones and fives.
  *    Use multi-threading to calculate symmetry and intensity for all images.
  */
object PlotSymmetryVSIntensity {

  val NumThreads = 4

  def plot(filename: String): Unit = plot(makeDataset(), filename)

  private def makeDataset():XYSeriesCollection = {
    val dataset = new XYSeriesCollection()

    for (label <- Array(1, 5)) {
      val pool = new IntensityAndSymmetryCalculationThreadPool(NumThreads)
      pool.addLabel(label)
      pool.calculate()
      val results = pool.results
      dataset.addSeries(getFeatureSeries(results, s"label $label"))
    }
    dataset
  }

  private def getFeatureSeries(images: List[MnistImage], key: String): XYSeries = {
    val series = new XYSeries(key)
    for (i <- images) {
      series.add(i.properties("intensity"), i.properties("symmetry"))
    }
    series
  }

  private def plot(dataset: XYSeriesCollection, filename: String): Unit = {
    val chart = ChartFactory.createScatterPlot(
      "Intensity vs symmetry",
      "Intensity", "Symmetry",
      dataset)

    val plot = chart.getPlot.asInstanceOf[XYPlot]

    plot.getDomainAxis.setRange(0, 0.34)
    plot.getRangeAxis().setRange(-0.21, 0.0)

    val renderer = plot.getRenderer

    renderer.setSeriesPaint(0, new Color(1.0f, 0.0f, 0.0f, 0.2f))
    renderer.setSeriesPaint(1, new Color(0.0f, 1.0f, 0.0f, 0.2f))

    val size = 3.0
    val delta = size / 2.0
    val shape1 = new Rectangle2D.Double(-delta, -delta, size, size)
    val shape2 = new Ellipse2D.Double(-delta, -delta, size, size)
    renderer.setSeriesShape(0, shape1)
    renderer.setSeriesShape(1, shape2)

    ChartUtils.saveChartAsJPEG(new File(filename), 1.0f, chart, 800, 800, null)
  }

}
