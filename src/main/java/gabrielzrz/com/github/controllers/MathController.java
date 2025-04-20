package gabrielzrz.com.github.controllers;

import gabrielzrz.com.github.exception.UnsupportedMathOperationExeption;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/math")
public class MathController {

    @RequestMapping("/sum/{numberOne}/{numberTwo}")
    public BigDecimal sum(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo){
        if (isNumberNullOrBlank(numberOne) || isNumberNullOrBlank(numberTwo)) {
            throw new UnsupportedMathOperationExeption("Please a set a numerci value");
        }
        numberOne = replaceNumber(numberOne);
        numberTwo = replaceNumber(numberTwo);
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationExeption("value is not number");
        }
        return new BigDecimal(numberOne).add(new BigDecimal(numberTwo));
    }

    private boolean isNumeric(String number) {
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    private boolean isNumberNullOrBlank(String number) {
        return Objects.isNull(number) || number.isBlank();
    }

    private String replaceNumber(String number) {
        return number.replace(",", ".");
    }

}
