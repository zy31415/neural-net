import mnist.data.DataReader
import mnist.features.ImageRotator
import mnist.plot.ImagePlotter

val nth = 1000

val imageData = DataReader.getImageData()(nth)

ImagePlotter.plotChar(imageData)

val rotator = new ImageRotator(imageData)


val alpha = 45.0

ImagePlotter.plot(rotator.rotate(alpha*Math.PI/180.0),
  f"./workspace/image_rotation/$nth-r$alpha%.0f.png")

