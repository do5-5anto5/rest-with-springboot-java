package do55antos.converters;

import do55antos.exceptions.UnsupportedMathOperationException;

public class Converter {
	
	public static Double convertToDouble(String strNumber) {
		if (strNumber == null) return 0D;
		String number = strNumber.replaceAll("," , ".");
		if (isNumeric(number)) return Double.parseDouble(number);
		if(!isNumeric(strNumber)) {
			throw new UnsupportedMathOperationException("Please set a numeric value.");
		}
		return 0D;
		
	}

	public static boolean isNumeric(String strNumber) {
		if (strNumber == null) return false;
		String number = strNumber.replaceAll("," , ".");
		return number.matches("[-+]?[0-9]*\\.?[0-9]+");
	}
	
}
