package do55antos.math;

import do55antos.exceptions.UnsupportedMathOperationException;

public class Operation {

		public Double sum(Double numberOne, Double numberTwo) {
			return numberOne + numberTwo;
		}
		
		public Double subtraction(Double numberOne, Double numberTwo) {
			return numberOne - numberTwo;
		}
		
		public Double multiplication(Double numberOne, Double numberTwo) {
			return numberOne * numberTwo;
		}
		
		public Double division(Double numberOne, Double numberTwo) {
			if(notZero(numberTwo)) {
			return numberOne / numberTwo;
			} else {
				throw new UnsupportedMathOperationException("It is not possible divide by zero.");
			}
		}
		
		public Double average(Double numberOne, Double numberTwo) {
			return (numberOne + numberTwo) / 2;
		}
		
		public Double squareRoot(Double number) {
			return Math.sqrt(number);
		}
		
		public boolean notZero(Double number) {
			return number != 0;
		}
}
