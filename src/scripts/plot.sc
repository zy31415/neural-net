import java.io.File

import javax.imageio.ImageIO
import mnist.data.MyMnistReader

val images1 = MyMnistReader.getImageData(1)

val symmetries = images1.map(FeatureExtractor.symmetryHeight _)

var n = 0
for {
  (imageData, sym) <- images1 zip symmetries
  if sym < -0.2
} {
  val image = MyMnistReader.asBufferedImage(imageData)
  ImageIO.write(image, "png", new File(s"low-symmetry-%nth.png"))
  n += 1
}



