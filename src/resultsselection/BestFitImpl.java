package resultsselection;

import java.util.ArrayList;

public class BestFitImpl {
    public class CalculationMethods {
        private long expectedResult;
        private long nums[];
        private boolean advanced;
        private boolean braces;
        private String match;
        private ArrayList<String> results = new ArrayList<>();// stores all matches

        CalculationMethods(long[] newNums, long newRes, boolean newAdvanced, boolean newBraces) {
            this.nums = newNums;
            this.expectedResult = newRes;
            this.advanced = newAdvanced;
            this.braces = newBraces;
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

        public void add(long sum, int currIndex, String currentString, boolean braced) {

            if (match != null) {
                return;
            }

            if (currIndex >= nums.length - 1) {

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

                multiply(sum, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "*", "add", braced);
                divide(sum, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "/", "add", braced);

                if (braces) {
                    //allows braces, not implemented
                }

            }

            add(sum + nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "+", braced);
            subtract(sum + nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "-", braced);

        }

        public void subtract(long subtr, int currIndex, String currentString, boolean braced) {

            if (match != null) {
                return;
            }

            if (currIndex >= nums.length - 1) {

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

                multiply(subtr, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "*", "sub", braced);
                divide(subtr, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "/", "sub", braced);

                if (braces) {
                    //allows braces, not implemented
                }

            }

            add(subtr - nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "+", braced);
            subtract(subtr - nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "-", braced);
        }

        public void multiply(long sum, long toMult, int currIndex, String resultString, String priorOperation, boolean braced) {

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
                    add(sum + toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+", braced);
                    subtract(sum + toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-", braced);
                    multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", priorOperation, braced);
                    divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", priorOperation, braced);
                    break;
                case "sub":
                    add(sum - toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+", braced);
                    subtract(sum - toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-", braced);
                    multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", priorOperation, braced);
                    divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", priorOperation, braced);
                    break;
                default:
                    add(toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+", braced);
                    subtract(toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-", braced);
                    multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", "", braced);
                    divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", "", braced);
                    break;
            }

            if (braces) {
                //allows braces, not implemented
            }

        }

        public void divide(long sum, long toDiv, int currIndex, String resultString, String priorOperation, boolean braced) {

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
                    add(sum + toDiv, currIndex + 1, resultString + nums[currIndex] + "+", braced);
                    subtract(sum + toDiv, currIndex + 1, resultString + nums[currIndex] + "-", braced);
                    multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", priorOperation, braced);
                    divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", priorOperation, braced);
                    break;
                case "sub":
                    add(sum - toDiv, currIndex + 1, resultString + nums[currIndex] + "+", braced);
                    subtract(sum - toDiv, currIndex + 1, resultString + nums[currIndex] + "-", braced);
                    multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", priorOperation, braced);
                    divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", priorOperation, braced);
                    break;
                default:
                    add(toDiv, currIndex + 1, resultString + nums[currIndex] + "+", braced);
                    subtract(toDiv, currIndex + 1, resultString + nums[currIndex] + "-", braced);
                    multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", "", braced);
                    divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", "", braced);
                    break;
            }

            if (braces) {
                //allows braces, not implemented
            }

        }

    }

    public class StringProcessor implements BestFitInt {
        private ArrayList<String> strings = new ArrayList<>();//contains strings of summand combinations
        private ArrayList<long[]> numbers = new ArrayList<>();//stores arrays of numbers to operate on
        private long nums[];//temporary array for numbers used while string to array of nums conversion
        private long expectedResult;
        private CalculationMethods calc;
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

        //checks if the string contains numbers only and if it's a non-zero length string
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

        @Override
        public String fitPlusMinus(String digits, long expectedResult) {

            //advanced math functions and braces are turned off 
            boolean launch = preparations(digits, expectedResult);

            if (!launch) {

                if (advanced) {
                    return "false";
                }

                return "";
            }

            for (long[] number : numbers) {

                calc.setNums(number);

                calc.add(number[0], 1, number[0] + "+", false);
                calc.subtract(number[0], 1, number[0] + "-", false);

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
                return "";
            }

            for (long[] number : numbers) {

                calc.setNums(number);

                calc.multiply(0, number[0], 1, number[0] + "*", "", false);
                calc.divide(0, number[0], 1, number[0] + "/", "", false);

            }

            advanced = false;

            if (calc.getMatch() != null) {
                return calc.getMatch();
            } else {
                return "";
            }
        }

        @Override
        public String fitBraces(String digits, long expectedResult) {
            //no implementation here yet
            return "";
        }

    }

    public static void main(String[] args) {
        BestFitImpl.StringProcessor proc = new BestFitImpl().new StringProcessor();

        String result = proc.fitPlusMinus("1111163", 64);
        System.out.println("fitPlusMinus(): " + result);

        String result1 = proc.fit("1111163", 64);
        System.out.println("fit():" + result1);
    }

}