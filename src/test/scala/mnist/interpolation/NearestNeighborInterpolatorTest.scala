package mnist.interpolation

import mnist.interpolation.NearestNeighborInterpolator
import org.scalatest.FunSuite

class NearestNeighborInterpolatorTest extends FunSuite {
  test("binary search: odd number") {
    val a = Array(1, 2, 3, 4, 5)
    assert(NearestNeighborInterpolator.binarySearch(a, -1.0) == -1)

    assert(NearestNeighborInterpolator.binarySearch(a, 1.0) == 0)
    assert(NearestNeighborInterpolator.binarySearch(a, 1.5) == 0)
    assert(NearestNeighborInterpolator.binarySearch(a, 2) == 1)
    assert(NearestNeighborInterpolator.binarySearch(a, 2.9) == 1)
    assert(NearestNeighborInterpolator.binarySearch(a, 4.5) == 3)
    assert(NearestNeighborInterpolator.binarySearch(a, 5) == 3)
    assert(NearestNeighborInterpolator.binarySearch(a, 6.0) == -1)

  }

  test("binary search: even number") {
    val a = Array(1, 2, 3, 4, 5, 6)
    assert(NearestNeighborInterpolator.binarySearch(a, -1.0) == -1)

    assert(NearestNeighborInterpolator.binarySearch(a, 1.0) == 0)
    assert(NearestNeighborInterpolator.binarySearch(a, 1.5) == 0)
    assert(NearestNeighborInterpolator.binarySearch(a, 2) == 1)
    assert(NearestNeighborInterpolator.binarySearch(a, 2.9) == 1)
    assert(NearestNeighborInterpolator.binarySearch(a, 4.5) == 3)
    assert(NearestNeighborInterpolator.binarySearch(a, 5) == 4)
    assert(NearestNeighborInterpolator.binarySearch(a, 6.1) == -1)

  }

  test("nearest neighbor") {
    val x = Array(0, 1)
    val y = Array(0, 1)

    val f = Array(
      1, 1,
      1, 1
    )

    val interpolator = new NearestNeighborInterpolator(x, y, f)
    assert(interpolator.interpolate(0.5, 0.5) == 1)
    assert(interpolator.interpolate(-0.1, 0.5) == 0)
    assert(interpolator.interpolate(0.5, -0.1) == 0)
    assert(interpolator.interpolate(0.0, 1.0) == 1)
  }

  test("nearest neighbor 2") {
    val x = Array(0, 1)
    val y = Array(0, 1)

    val f = Array(
      1, 3,
      1, 3
    )

    val interpolator = new NearestNeighborInterpolator(x, y, f)
    assert(interpolator.interpolate(0.5, 0.5) == 2)
  }

  test("nearest neighbor 3") {
    val x = Array(0, 1, 2)
    val y = Array(0, 1)

    val f = Array(
      1, 3, 0,
      1, 3, 0
    )

    val interpolator = new NearestNeighborInterpolator(x, y, f)
    assert(interpolator.interpolate(0.5, 0.5) == 2)
    assert(interpolator.interpolate(0, 0.5) == 2)
    assert(interpolator.interpolate(1, 0.5) == 2)
    assert(interpolator.interpolate(1.5, 0.5) == 2)
  }
}
