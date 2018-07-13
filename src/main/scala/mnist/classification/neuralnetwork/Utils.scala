package mnist.classification.neuralnetwork

import breeze.linalg.{DenseVector, argmax}

object Utils {
  def printChar(in: DenseVector[Double], y: DenseVector[Double] = null): Unit ={

    println("========================================")
    if (y != null) {
      println("Label: %d".format(argmax(y)))
    }
    println("--------------------------------------")
    for (i<- 0 until 28) {
      val row = in(i*28 until (i+1)*28)
      for(i <- row)
        if (i > 0.9)
          print("*")
        else
          print(" ")
      println("")
    }
    println("========================================")
  }
}
