package mnist.plot

import java.io.File
import java.nio.file.Paths

import javax.imageio.ImageIO
import mnist.data.DataReader

object ImagePlotter {
  def plotAll(outputPath: String): Unit = {
    val imageData = DataReader.getImageData()
    val labels = DataReader.getLabels()

    var n = 0
    for ((d, l) <- imageData zip labels) {
      val image = PlotUtils.asBufferedImage(d)
      ImageIO.write(image, "png",
        Paths.get(outputPath, f"$n%05d-$l.png").toFile
      )
      n += 1
    }
  }
}
