package ai

import java.util.Arrays;

class Perceptron(val numInputs: Int, val learningRate: Double) extends BinaryClassifier {
  var weights = List.fill(numInputs + 1)(0.0).toArray
  private var numTrainingSamples = 0
  var iteratedError = 0.0

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
    iteratedError = (iteratedError * numTrainingSamples + Math.abs(i - o))
    numTrainingSamples += 1
    iteratedError /= numTrainingSamples
  }
}