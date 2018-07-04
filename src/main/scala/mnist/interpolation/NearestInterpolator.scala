package mnist.interpolation

class NearestInterpolator(val x: Array[Int], val y: Array[Int], val f: Array[Array[Int]]) {
  NearestInterpolator.assertStrictAscendingOrder(x)
  NearestInterpolator.assertStrictAscendingOrder(y)
  NearestInterpolator.assertDimension(x, y, f)

  def interpolate(xx: Double, yy: Double): Int = {
    val ix = NearestInterpolator.binarySearch(x, xx)
    if (ix < 0) return 0
    val iy = NearestInterpolator.binarySearch(y, yy)
    if (iy < 0) return 0

    // mean of the nearest neighbors
    (f(ix)(iy) + f(ix+1)(iy) + f(ix)(iy+1) + f(ix+1)(iy+1))/4
  }

}

object NearestInterpolator {
  def assertStrictAscendingOrder(x: Array[Int]): Unit = {
    for(n <- 1 until x.length) {
      assert(x(n-1) < x(n), "Should be strictly ascending.")
    }
  }

  def assertDimension(x: Array[Int], y: Array[Int], f: Array[Array[Int]]): Unit = {
    for(n <- 1 until f.length)
      assert(f(0).length == f(n).length)
    assert(x.length == f(0).length)
    assert(y.length == f.length)
  }

  def binarySearch(a: Array[Int], p: Double): Int = {
    if (p < a(0) || p > a.last)
      return -1

    var low = 0
    var hi = a.length - 1

    while (low < hi - 1) {
      val m = (low + hi)/2
      if (p < a(m))
        hi = m
      else
        low = m
    }
    low
  }
}
