package mnist.data

import breeze.linalg.DenseVector

object Utils {
  def labelEncoder(label: Int, size: Int) = {
    assert(label >= 0 && label < size)
    val out = DenseVector.zeros[Double](size)
    out(label) = 1.0
    out
  }
}
