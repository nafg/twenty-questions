package twentyquestions

import scala.io.StdIn


object Action {
  def run[A](action: Action[A]): A = action match {
    case Output(text, cont)  =>
      run(cont())
    case Input(prompt, cont) =>
      val input = StdIn.readLine(prompt + " ")
      val nextAction = cont(input)
      run(nextAction)
    case End(result)         =>
      result
  }
}

sealed trait Action[+A] {
  def map[B](f: A => B): Action[B]
  def flatMap[B](f: A => Action[B]): Action[B]
}

case class Output[A](text: String, cont: () => Action[A]) extends Action[A] {
  override def map[B](f: (A) => B) = Output(text, () => cont().map(f))
  override def flatMap[B](f: (A) => Action[B]) = Output(text, () => cont().flatMap(f))
}
case class Input[A](prompt: String, cont: String => Action[A]) extends Action[A] {
  override def map[B](f: (A) => B) = Input(prompt, s => cont(s).map(f))
  override def flatMap[B](f: (A) => Action[B]) = Input(prompt, s => cont(s).flatMap(f))
}
case class End[A](result: A) extends Action[A] {
  override def map[B](f: (A) => B) = End(f(result))
  override def flatMap[B](f: (A) => Action[B]) = f(result)
}

object TestAction {
  def main(args: Array[String]): Unit = {
    val action =
      Input(
        prompt = "Enter a string",
        cont = input =>
          Output(
            text = "You entered " + input,
            cont = () => End(input.reverse)
          )
      )
    val result = Action.run(action)
    println("Action.run returned " + result)
  }
}
