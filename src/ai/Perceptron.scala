package ai

import java.util.Arrays;
import scala.collection.JavaConversions._

class Perceptron(val numInputs: Int, val learningRate: Double) {
  var weights = List.fill(numInputs + 1)(0.0).toArray
  private var numTrainingSamples = 0
  var iteratedError = 0.0

  private def dotProduct(a: Array[Double], b: Array[Double]) =
    (a zip b) map { case (x, y) => x * y } reduce (_ + _)

  def classify(input: Array[Double]): Int =
    if (dotProduct(input, weights) > 0) 1 else 0

  def classifyAndUpdate(input: Array[Double], desiredOutput: Int): Int = {
    val inputWithBiasTerm = input :+ 1.0
    val output = classify(inputWithBiasTerm)
    updateIteratedError(desiredOutput, output)
    weights = (weights zip inputWithBiasTerm) map {
      case (w, i) => w + learningRate * (desiredOutput - output) * i
    }
    return output
  }
    
    private def updateIteratedError(i: Double, o: Double): Unit = {
      iteratedError = (iteratedError*numTrainingSamples + Math.abs(i - o))
      numTrainingSamples += 1
      iteratedError /= numTrainingSamples
    }
      

  def classifyAndUpdate(
      inputs: java.util.List[Array[Double]],
      desiredOutputs: java.util.List[Integer]): java.util.List[Integer] =
    (inputs zip desiredOutputs).map(x => classifyAndUpdate(x._1, x._2)).map(new Integer(_)).toList

}