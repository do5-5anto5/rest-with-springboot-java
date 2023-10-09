package do55antos;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import do55antos.converters.Converter;
import do55antos.math.Operation;

@RestController
public class MathController {
	
	private final AtomicLong counter = new AtomicLong();
	
	Operation calculator = new Operation();
	
	@GetMapping("/sum/{numberOne}/{numberTwo}")
	public Double sum(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {
		
		return calculator.sum(
				Converter.convertToDouble(numberOne), Converter.convertToDouble(numberTwo));
	}
	
	@GetMapping("/subtraction/{numberOne}/{numberTwo}")
	public Double subtraction(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {
		
		return calculator.subtraction(
				Converter.convertToDouble(numberOne), Converter.convertToDouble(numberTwo));
	}
	
	@GetMapping("/multiplication/{numberOne}/{numberTwo}")
	public Double multiplication(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {
		
		return calculator.multiplication(Converter.convertToDouble(numberOne), Converter.convertToDouble(numberTwo));
	}
	
	@GetMapping("/division/{numberOne}/{numberTwo}")
	public Double division(

			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {
		
		return calculator.division(Converter.convertToDouble(numberOne), Converter.convertToDouble(numberTwo));
	}	
	
	@GetMapping("/average/{numberOne}/{numberTwo}")
	public Double average(
			@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo
			) throws Exception {	
		
		return calculator.average(
				Converter.convertToDouble(numberOne), Converter.convertToDouble(numberTwo));
	}

	@GetMapping("/squareroot/{number}")
	public Double root(
			@PathVariable(value = "number") String number
			) throws Exception {		
		return calculator.squareRoot(Converter.convertToDouble(number));
	}		
	
}
