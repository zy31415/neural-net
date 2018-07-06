package mnist.plot

import java.io.File
import java.nio.file.Paths

import javax.imageio.ImageIO
import mnist.data.DataReader

object ImagePlotter {
  def plotAll(outputPath: String): Unit = {
    val imageData = DataReader.getAllImageDataMatrix()
    val labels = DataReader.getAllLabels()

    for (((d, l), n) <- (imageData zip labels).zipWithIndex)
      plot(d, Paths.get(outputPath, f"$n%05d-$l.png").toString)
  }

  def plot(imageData: Array[Int], filename: String): Unit =
    ImageIO.write(PlotUtils.asBufferedImage(imageData), "png", new File(filename))

  def plot(imageData: Array[Double], filename: String): Unit =
    plot(imageData.map(Math.round(_).toInt), filename)

//  def plotChar(imageData: Array[Int]): Unit = {
//    for ((a, n) <- imageData.zipWithIndex){
//      if (a != 0 )
//        print( "* ")
//      else
//        print("  ")
//      if (n % 28 == 0)
//        println("")
//    }
//  }
}
