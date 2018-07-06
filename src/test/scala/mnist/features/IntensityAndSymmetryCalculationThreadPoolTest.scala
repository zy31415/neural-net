package mnist.features

import org.scalatest.FunSuite

class IntensityAndSymmetryCalculationThreadPoolTest extends FunSuite {
  test("test") {
    val pool = new IntensityAndSymmetryCalculationThreadPool(4)
    pool.addLabel(1)
    pool.addLabel(5)
    pool.calculate()

    println(pool.results.size)

  }

}
