package mnist.interpolation

class NearestNeighborInterpolator(val x: Array[Int], val y: Array[Int], val f: Array[Int]) {
  NearestNeighborInterpolator.assertStrictAscendingOrder(x)
  NearestNeighborInterpolator.assertStrictAscendingOrder(y)
  var ff:Array[Array[Int]] = _
  assertDimension(x, y, f)

  def assertDimension(x: Array[Int], y: Array[Int], f: Array[Int]): Unit = {
    ff = f.grouped(x.length).toArray
    for(n <- 1 until ff.length)
      assert(ff(0).length == ff(n).length)
    assert(x.length == ff(0).length)
    assert(y.length == ff.length)
  }

  def interpolate(xx: Double, yy: Double): Int = {
    val ix = NearestNeighborInterpolator.binarySearch(x, xx)
    if (ix < 0) return 0
    val iy = NearestNeighborInterpolator.binarySearch(y, yy)
    if (iy < 0) return 0

    // mean of the nearest neighbors
    Math.round((ff(iy)(ix) + ff(iy)(ix+1) + ff(iy+1)(ix) + ff(iy+1)(ix+1))/4.0).toInt
  }

}

object NearestNeighborInterpolator {
  def assertStrictAscendingOrder(x: Array[Int]): Unit = {
    for(n <- 1 until x.length) {
      assert(x(n-1) < x(n), "Should be strictly ascending.")
    }
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
