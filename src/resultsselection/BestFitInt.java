package resultsselection;

public interface BestFitInt {
    String fitPlusMinus(String digits, long expectedResult);
    String fit(String digits, long expectedResult);
    String fitBraces(String digits, long expectedResult);
}  