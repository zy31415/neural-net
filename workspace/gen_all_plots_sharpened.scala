import java.nio.file.Paths

import javax.imageio.ImageIO
import mnist.data.DataReader
import mnist.features.Sharpener
import mnist.plot.PlotUtils


val outputPath = "/Users/zy/Documents/workspace/mnist/workspace/plots_sharpened"

val imageData = DataReader.getAllImageDataAsMatrix()
val labels = DataReader.getAllLabels()

var n = 0
for ((d, l) <- imageData zip labels) {
  val image = PlotUtils.asBufferedImage(Sharpener.sharpen(d))
  ImageIO.write(image, "png",
    Paths.get(outputPath, f"$n%05d-$l.png").toFile
  )
  n += 1
}