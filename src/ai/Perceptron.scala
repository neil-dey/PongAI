package ai

import java.util.Arrays;
import scala.collection.JavaConversions._

class Perceptron(val numInputs: Int, val learningRate: Double) {
  var weights = List.fill(numInputs + 1)(0.0).toArray
  private var ideals = List.fill(0)(0)
  private var outputs = List.fill(0)(0)

  private def dotProduct(a: Array[Double], b: Array[Double]) =
    (a zip b).foldLeft(0.0)((acc, curr) => acc + curr._1 * curr._2)

  def classify(input: Array[Double]): Int =
    if (dotProduct(input, weights) > 0) 1 else 0

  def classifyAndUpdate(input: Array[Double], desiredOutput: Int): Int = {
    val inputWithBiasTerm = input :+ 1.0
    val output = classify(inputWithBiasTerm)
    ideals = desiredOutput :: ideals
    outputs = output :: outputs
    weights = (weights zip inputWithBiasTerm)
      .map(x => x._1 + learningRate * (desiredOutput - output) * x._2)
    return output
  }

  def iteratedError() =
    (ideals zip outputs).foldLeft(0.0)((acc, curr) => acc + Math.abs(curr._1 - curr._2)) / ideals.size

  def update(inputs: java.util.List[Array[Double]], desiredOutputs: java.util.List[Integer]) = {
    weights = List.fill(numInputs + 1)(0.0).toArray
    (inputs zip desiredOutputs).foreach(x => classifyAndUpdate(x._1, x._2))
  }
}