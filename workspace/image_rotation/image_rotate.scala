import mnist.data.DataReader
import mnist.features.ImageRotator
import mnist.plot.ImagePlotter

val nth = 200

val imageData = DataReader.getAllImageDataAsMatrix()(nth)

ImagePlotter.plotChar(imageData)

val rotator = new ImageRotator(imageData)

for (alpha <- 0 to 360 by 10) {
  ImagePlotter.plot(rotator.rotate(alpha*Math.PI/180.0),
    f"./workspace/image_rotation/plots/$nth-r$alpha%.0f.png")
}



