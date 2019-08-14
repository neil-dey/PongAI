package ai

import scala.collection.JavaConversions._

abstract class BinaryClassifier {
  var weights: Array[Double]

  protected def dotProduct(a: Array[Double], b: Array[Double]) =
    (a zip b) map { case (x, y) => x * y } reduce (_ + _)

  def classify(input: Array[Double]): Int

  def classifyAndUpdate(input: Array[Double], desiredOutput: Int): Int

  def classifyAndUpdate(
      inputs: java.util.List[Array[Double]],
      desiredOutputs: java.util.List[Integer]): java.util.List[Integer] =
    (inputs zip desiredOutputs).map(x => classifyAndUpdate(x._1, x._2)).map(new Integer(_)).toList
}