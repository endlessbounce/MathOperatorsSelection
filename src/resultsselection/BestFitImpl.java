package resultsselection;

import java.util.*;

public class BestFitImpl implements BestFitInt {
    private CalculationMethods calc;
    private ArrayList<String> strings = new ArrayList<>();//contains strings of summand combinations
    private ArrayList<long[]> numbers = new ArrayList<>();//stores arrays of numbers to operate on
    private long nums[];//temporary array for numbers used while string to array of nums conversion
    private long expectedResult;
    private boolean advanced;//flag indicating usage of multiplication and division
    private boolean braces;//flag indicating usage of parentheses

    //converts the input string into multiple strings with all possible combinations of summands
    private void parser(String toPasre, String curString, int aim) {

        if (toPasre.length() <= 1) return;

        String left = toPasre.substring(0, aim + 1);
        String right = toPasre.substring(aim + 1);

        String result = curString + left + "-" + right;
        strings.add(result);

        for (int i = 0; i < right.length() - 1; i++) {
            parser(right, curString + left + "-", i);
        }

    }

    //checks for correct input
    private boolean preparations(String digits, long expectedResult) {
        this.expectedResult = expectedResult;
        numbers.clear();
        strings.clear();

        if (digits.length() < 2) {
            System.out.println("The string must be of length 2+");
            return false;
        }

        //pick out all possible combinations of summands
        for (int i = 0; i < digits.length() - 1; i++) {
            parser(digits, "", i);
        }

        try {

            //convert strings of number combinations to number arrays
            for (String string : strings) {

                String tempStringArr[] = string.split("-");
                nums = new long[tempStringArr.length];

                for (int i = 0; i < nums.length; i++) {
                    nums[i] = Long.parseLong(tempStringArr[i]);
                }

                numbers.add(nums);
            }


        } catch (NumberFormatException ex) {
            System.out.println("Incorrect input string.");
            return false;
        }

        calc = new CalculationMethods(null, expectedResult, advanced, braces);

        return true;
    }

    private void allOperations(long tempArr[]) {
        calc.add(tempArr[0], 1, tempArr[0] + "+");
        calc.subtract(tempArr[0], 1, tempArr[0] + "-");
        calc.multiply(0, tempArr[0], 1, tempArr[0] + "*", "");
        calc.divide(0, tempArr[0], 1, tempArr[0] + "/", "");
    }

    @Override
    public String fitPlusMinus(String digits, long expectedResult) {

        boolean launch = preparations(digits, expectedResult);

        if (!launch) {

            if (advanced) {
                advanced = false;
                return "false";
            }

            return "";
        }

        for (long[] number : numbers) {

            calc.setNums(number);

            calc.add(number[0], 1, number[0] + "+");
            calc.subtract(number[0], 1, number[0] + "-");

        }

        if (calc.getMatch() != null) {
            return calc.getMatch();
        } else {
            return "";
        }

    }

    @Override
    public String fit(String digits, long expectedResult) {
        advanced = true;
        String result = fitPlusMinus(digits, expectedResult);

        if (result.equals("false")) {

            if (braces) {
                braces = false;
                return "false";
            }

            return "";
        } else if (!result.equals("")) {
            return result;
        }

        for (long[] number : numbers) {

            calc.setNums(number);

            calc.multiply(0, number[0], 1, number[0] + "*", "");
            calc.divide(0, number[0], 1, number[0] + "/", "");

        }

        advanced = false;

        if (calc != null && calc.getMatch() != null) {
            return calc.getMatch();
        } else {
            return "";
        }
    }

    @Override
    public String fitBraces(String digits, long expectedResult) {

        String result = fit(digits, expectedResult);

        if (result.equals("false")) {
            return "";
        } else if (!result.equals("")) {
            return result;
        }

        for (long[] number : numbers) {
            long[] tempArr;
            if (number.length <= 2) continue;

            //i moves parentheses from left to right
            for (int i = 0; i < number.length - 1; i++) {

                int tempCondition;

                if (i == 0) {
                    tempCondition = number.length - 1;
                } else {
                    tempCondition = number.length - i;
                }

                //j sets length of expression that should be taken in parentheses
                for (int j = 1; j < tempCondition; j++) {

                    ArrayList<Long> tempLong = new ArrayList<>();
                    calc.getMap().clear();
                    //create an array for numbers in parentheses
                    tempArr = new long[j + 1];
                    System.arraycopy(number, i, tempArr, 0, tempArr.length);

                    //get values of expressions in parentheses and put them in the map (braces==true)
                    calc.setNums(tempArr);
                    calc.setBraces(true);
                    allOperations(tempArr);
                    calc.setBraces(false);

                    Set<String> keys = calc.getMap().keySet();

                    for (String key : keys) {

                        tempLong.clear();

                        //copying sequence before parentheses if exists
                        for (int k = 0; k < i; k++) {
                            tempLong.add(number[k]);
                        }

                        //add to the list the value of expression in the parentheses
                        long value = calc.getMap().get(key);
                        tempLong.add(value);

                        //copying sequence after parentheses if exists
                        for (int k = i + j + 1; k < number.length; k++) {
                            tempLong.add(number[k]);
                        }

                        //copy numbers from list to array
                        long tempArrForCalc[] = new long[tempLong.size()];
                        for (int k = 0; k < tempArrForCalc.length; k++) {
                            tempArrForCalc[k] = tempLong.get(k);
                        }

                        calc.setNums(tempArrForCalc);
                        allOperations(tempArrForCalc);

                        int startIndex = 0;
                        if (calc.getMatch() != null) {
                            String match = calc.getMatch();

                            StringBuilder tempStr = new StringBuilder(match);
                            String valueString = Long.toString(value);
                            int replaceLength = valueString.length();

                            for (int k = 0; k < match.length(); k++) {

                                if ((k + replaceLength) <= match.length()
                                        && match.substring(k, k + replaceLength).equals(valueString)) {
                                    startIndex = k;
                                    break;
                                }

                            }

                            tempStr.replace(startIndex, startIndex + replaceLength, key);
                            match = tempStr.toString();

                            return match;
                        }

                    }

                }

            }

        }

        if (calc != null && calc.getMatch() != null) {
            return calc.getMatch();
        } else {
            return "";
        }
    }

    //contains some auxillary methods
    public class CalculationMethods {
        private long expectedResult;
        private long nums[];
        private boolean advanced;
        private boolean braces;
        private String match;
        private ArrayList<String> results = new ArrayList<>();// stores all matches
        private Map<String, Long> map = new LinkedHashMap<>();

        CalculationMethods(long[] newNums, long newRes, boolean newAdvanced, boolean newBraces) {
            this.nums = newNums;
            this.expectedResult = newRes;
            this.advanced = newAdvanced;
            this.braces = newBraces;
        }

        public void setBraces(boolean braces) {
            this.braces = braces;
        }

        public Map<String, Long> getMap() {
            return map;
        }

        public ArrayList<String> getResults() {
            return results;
        }

        public void setNums(long[] nums) {
            this.nums = nums;
        }

        public String getMatch() {
            return match;
        }

        public void add(long sum, int currIndex, String currentString) {

            if (match != null) {
                return;
            }

            if (currIndex >= nums.length - 1) {

                if (braces) {
                    map.put("(" + currentString + nums[currIndex] + ")", sum + nums[currIndex]);
                    return;
                }

                //put the match into the list
//                if ((sum + nums[currIndex]) == expectedResult) {
//                    results.add(currentString + nums[currIndex]);
//                }

                //find first match and return
                if ((sum + nums[currIndex]) == expectedResult) {
                    match = currentString + nums[currIndex];
                }

                return;
            }

            //allows miltiplication and division operations
            if (advanced) {

                multiply(sum, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "*", "add");
                divide(sum, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "/", "add");

            }

            add(sum + nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "+");
            subtract(sum + nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "-");

        }

        public void subtract(long subtr, int currIndex, String currentString) {

            if (match != null) {
                return;
            }

            if (currIndex >= nums.length - 1) {

                if (braces) {
                    map.put("(" + currentString + nums[currIndex] + ")", subtr - nums[currIndex]);
                    return;
                }

                //put the match into the list
//                if ((subtr - nums[currIndex]) == expectedResult) {
//                    results.add(currentString + nums[currIndex]);
//                }

                //find first match and return
                if ((subtr - nums[currIndex]) == expectedResult) {
                    match = currentString + nums[currIndex];
                }

                return;
            }

            //allows miltiplication and division operations
            if (advanced) {

                multiply(subtr, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "*", "sub");
                divide(subtr, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "/", "sub");

            }

            add(subtr - nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "+");
            subtract(subtr - nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "-");
        }

        public void multiply(long sum, long toMult, int currIndex, String resultString, String priorOperation) {

            if (match != null) {
                return;
            }

            long tempSum;
            //if end of array is reached
            if (currIndex >= nums.length - 1) {

                switch (priorOperation) {
                    case "add":
                        tempSum = sum + toMult * nums[currIndex];
                        break;
                    case "sub":
                        tempSum = sum - toMult * nums[currIndex];
                        break;
                    default:
                        tempSum = toMult * nums[currIndex];
                        break;
                }

                if (braces) {
                    map.put("(" + resultString + nums[currIndex] + ")", tempSum);
                    return;
                }

                //put the match into the list
//                if (tempSum == expectedResult) {
//                    results.add(resultString + nums[currIndex]);
//                }

                //find first match and return
                if (tempSum == expectedResult) {
                    match = resultString + nums[currIndex];
                }

                return;
            }

            switch (priorOperation) {
                case "add":
                    add(sum + toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+");
                    subtract(sum + toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-");
                    multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", priorOperation);
                    divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", priorOperation);
                    break;
                case "sub":
                    add(sum - toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+");
                    subtract(sum - toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-");
                    multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", priorOperation);
                    divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", priorOperation);
                    break;
                default:
                    add(toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+");
                    subtract(toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-");
                    multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", "");
                    divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", "");
                    break;
            }

        }

        public void divide(long sum, long toDiv, int currIndex, String resultString, String priorOperation) {

            if (match != null) {
                return;
            }

            if (nums[currIndex] != 0 && toDiv % nums[currIndex] == 0) {
                toDiv /= nums[currIndex];
            } else {
                return;
            }

            long tempSum;
            //if end of array is reached
            if (currIndex >= nums.length - 1) {

                switch (priorOperation) {
                    case "add":
                        tempSum = sum + toDiv;
                        break;
                    case "sub":
                        tempSum = sum - toDiv;
                        break;
                    default:
                        tempSum = toDiv;
                        break;
                }

                if (braces) {
                    map.put("(" + resultString + nums[currIndex] + ")", tempSum);
                    return;
                }

                //put the match into the list
//                if (tempSum == expectedResult) {
//                    results.add(resultString + nums[currIndex]);
//                }

                //find first match and return
                if (tempSum == expectedResult) {
                    match = resultString + nums[currIndex];
                }

                return;
            }

            switch (priorOperation) {
                case "add":
                    add(sum + toDiv, currIndex + 1, resultString + nums[currIndex] + "+");
                    subtract(sum + toDiv, currIndex + 1, resultString + nums[currIndex] + "-");
                    multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", priorOperation);
                    divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", priorOperation);
                    break;
                case "sub":
                    add(sum - toDiv, currIndex + 1, resultString + nums[currIndex] + "+");
                    subtract(sum - toDiv, currIndex + 1, resultString + nums[currIndex] + "-");
                    multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", priorOperation);
                    divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", priorOperation);
                    break;
                default:
                    add(toDiv, currIndex + 1, resultString + nums[currIndex] + "+");
                    subtract(toDiv, currIndex + 1, resultString + nums[currIndex] + "-");
                    multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", "");
                    divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", "");
                    break;
            }

        }

    }

    public static void main(String[] args) {
        BestFitImpl proc = new BestFitImpl();

        String result = proc.fitPlusMinus("2234", 34);
        System.out.println("fitPlusMinus(): " + result);

        String result1 = proc.fit("1111163", 64);
        System.out.println("fit():" + result1);

        for (int i = 1; i < 100000; i++) {
            String result2 = proc.fitBraces("" + i, 78);
            System.out.println("inputString: " + i + " fitBraces():" + result2);
        }

    }

}
