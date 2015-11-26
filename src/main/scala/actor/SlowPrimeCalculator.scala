package actor

object SlowPrimeCalculator {

    def calculate(): Int = {
      var result = 0
      for (i <- 1 to 1000 if isPrime(i)) { result = i }
      result
    }

    def isPrime(n: Int) = (2 until n) forall (n % _ != 0)
}
