package mnist.plot

import org.scalatest.FunSuite

class ImagePlotterTest extends FunSuite {

  test("test plot") {
    ImagePlotter.plotAll("/Users/zy/Documents/workspace/mnist/workspace/plots/")
  }

}
