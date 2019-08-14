package ai

class SVM(val numInputs: Int, val learningRate: Double) extends BinaryClassifier {
  var weights = List.fill(numInputs + 1)(0.0).toArray

  def classify(input: Array[Double]): Int =
    dotProduct(input, weights).signum

  private def updateWeights(input: Array[Double], desiredOutput: Int) = {
    weights = (weights zip input) map { case (w, i) => w + learningRate * desiredOutput * i }
    classify(input)
  }

  def classifyAndUpdate(input: Array[Double], desiredOutput: Int): Int = {
    val inputWithBiasTerm = input :+ 1.0
    if (misClassified(inputWithBiasTerm, desiredOutput))
      updateWeights(inputWithBiasTerm, desiredOutput) else classify(inputWithBiasTerm)
  }

  private def misClassified(input: Array[Double], desiredOutput: Int) =
    dotProduct(input, weights) * desiredOutput < 1
}