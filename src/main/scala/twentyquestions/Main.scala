package twentyquestions

import scala.io.StdIn


sealed trait Step
case class Question(text: String, yes: Step, no: Step) extends Step
case class Guess(text: String) extends Step


object Main {
  def runStep(step: Step): Action[Step] =
    step match {
      case q@Question(questionText, yes, no) =>
        def recurse(input: String) = input.toLowerCase match {
          case "yes" => runStep(yes).map(newYesBranch => q.copy(yes = newYesBranch))
          case "no"  => runStep(no).map(newNoBranch => q.copy(no = newNoBranch))
        }
        for {
          input <- Input(prompt = questionText, cont = End(_))
          result <- recurse(input)
        } yield result

      case Guess(guessText) =>
        def handleResponse(input: String) = input.toLowerCase match {
          case "yes" =>
            Output(text = "I won!", () => End(())).map(_ => step)
          case "no"  =>
            for {
              answer <- Input(prompt = "I give up. What were you thinking of", cont = End(_))
              prompt = "Enter a question that the answer is 'yes' for " + answer + " and 'no' for " + guessText
              newQuestionText <- Input(prompt = prompt, cont = End(_))
            } yield Question(text = newQuestionText, yes = Guess(answer), no = Guess(guessText))
        }
        for {
          input <- Input(prompt = "Is it " + guessText, cont = End(_))
          result <- handleResponse(input)
        } yield result
    }

  def main(args: Array[String]): Unit = {
    val initialModel =
      Question(
        text = "Is it an animal?",
        yes = Guess("an elephant"),
        no = Guess("a frying pan")
      )
    def loop(model: Step): Unit = {
      val result = Action.run(runStep(model))
      StdIn.readLine("Do you want to play again? ").toLowerCase match {
        case "no"  =>
        case "yes" => loop(result)
      }
    }
    loop(initialModel)
  }
}
