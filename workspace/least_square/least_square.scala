import breeze.linalg.DenseMatrix
import mnist.ProjectConstant.{ImageHalfWidth, ImageWidth}
import mnist.data.DataReader
import mnist.features.{ImageRotator, Sharpener}
import mnist.plot.ImagePlotter

val nth = 99

val imageData = DataReader.getAllImageDataAsMatrix()(nth)

ImagePlotter.plot(imageData, "workspace/least_square/before.png")

val sharpImageData = Sharpener.sharpen(imageData)
ImagePlotter.plot(sharpImageData, "workspace/least_square/before-sharp.png")

var A = List[Array[Double]]()
var c = List[Array[Double]]()

for ((d, n) <- sharpImageData.zipWithIndex) {
  if (d == Sharpener.TRUE) {
    val xi = n % ImageWidth - ImageHalfWidth + 1
    val yi = n / ImageWidth - ImageHalfWidth + 1
    A = Array(yi.toDouble, 1.0) :: A
    c = Array(xi.toDouble) :: c
  }
}

val Am = DenseMatrix(A:_*)
val cm = DenseMatrix(c:_*)

val out = cm \ Am
val lambda = out(0, 0)
val interception = out(0, 1)

println(lambda, interception)
val alpha =  Math.atan(lambda)
println(alpha * 180.0 / Math.PI)
//
val rotatedImageData = new ImageRotator(imageData).rotate(alpha)
//
ImagePlotter.plot(rotatedImageData, "workspace/least_square/after.png")



