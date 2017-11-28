package resultsselection;

import java.util.ArrayList;

public class BestFitImpl {
    public class CalculationMethods {
        private long expectedResult;
        private long nums[];
        private boolean advanced;
        private boolean braces;
        private ArrayList<String> results = new ArrayList<>();// stores all matches

        CalculationMethods(long[] newNums, long newRes, boolean newAdvanced, boolean newBraces){
            this.nums = newNums;
            this.expectedResult = newRes;
            this.advanced = newAdvanced;
            this.braces = newBraces;
        }
        
        public ArrayList<String> getResults() {
            return results;
        }
        
        public void add(long sum, int currIndex, String currentString, boolean braced){
            
            if(currIndex >= nums.length - 1){

                if((sum + nums[currIndex]) == expectedResult){
                    results.add(currentString + nums[currIndex]);
                }
                
                return;
            }
            
            //allows miltiplication and division operations
            if(advanced){
                
                multiply(sum, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "*", "add", braced);
                divide(sum, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "/", "add", braced);
                
                if(braces){
                    //allows braces, not implemented
                }
                
            }
            
            add(sum + nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "+", braced);
            subtract(sum + nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "-", braced);
            
        }

        public void subtract(long subtr, int currIndex, String currentString, boolean braced){
            
            if(currIndex >= nums.length - 1){
                
                if((subtr - nums[currIndex]) == expectedResult){
                    results.add(currentString + nums[currIndex]);
                }
                
                return;
            }
            
            //allows miltiplication and division operations
            if(advanced){
                
                multiply(subtr, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "*", "sub", braced);
                divide(subtr, nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "/", "sub",  braced);
                
                if(braces){
                    //allows braces, not implemented
                }
                
            }
            
            add(subtr - nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "+", braced);
            subtract(subtr - nums[currIndex], currIndex + 1, currentString + nums[currIndex] + "-", braced);
        }

        public void multiply(long sum, long toMult, int currIndex, String resultString, String priorOperation, boolean braced){
            
            long tempSum = 0;
            //if end of array is reached
            if(currIndex >= nums.length - 1){
                
                if(priorOperation.equals("add")){
                    tempSum = sum + toMult * nums[currIndex];
                }else if(priorOperation.equals("sub")){
                    tempSum = sum - toMult * nums[currIndex];
                }else{
                    tempSum = toMult * nums[currIndex];
                }
                
                if(tempSum == expectedResult){
                    results.add(resultString + nums[currIndex]);
                }
                
                return;
            }
            
            if(priorOperation.equals("add")){
                add(sum + toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+", braced);
                subtract(sum + toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-", braced);
                multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", priorOperation, braced);
                divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", priorOperation, braced);
            }else if(priorOperation.equals("sub")){
                add(sum - toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+", braced);
                subtract(sum - toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-", braced);
                multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", priorOperation, braced);
                divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", priorOperation, braced);
            }else{
                add(toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "+", braced);
                subtract(toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "-", braced);
                multiply(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "*", "", braced);
                divide(sum, toMult * nums[currIndex], currIndex + 1, resultString + nums[currIndex] + "/", "", braced);
            }
            
            if(braces){
                //allows braces, not implemented
            }
                    
        }

        public void divide(long sum, long toDiv, int currIndex, String resultString, String priorOperation, boolean braced){
            
            if(nums[currIndex] != 0 && toDiv % nums[currIndex] == 0){
                toDiv /= nums[currIndex];
            }else{
                return;
            }
            
            long tempSum = 0;
            //if end of array is reached
            if(currIndex >= nums.length - 1){
            
                if(priorOperation.equals("add")){
                    tempSum = sum + toDiv;
                }else if(priorOperation.equals("sub")){
                    tempSum = sum - toDiv;
                }else{
                    tempSum = toDiv;
                }
                
                if(tempSum == expectedResult){
                    results.add(resultString + nums[currIndex]);
                }
                
                return;
            }
            
            if(priorOperation.equals("add")){
                add(sum + toDiv, currIndex + 1, resultString + nums[currIndex] + "+", braced);
                subtract(sum + toDiv, currIndex + 1, resultString + nums[currIndex] + "-", braced);
                multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", priorOperation, braced);
                divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", priorOperation, braced);
            }else if(priorOperation.equals("sub")){
                add(sum - toDiv, currIndex + 1, resultString + nums[currIndex] + "+", braced);
                subtract(sum - toDiv, currIndex + 1, resultString + nums[currIndex] + "-", braced);
                multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", priorOperation, braced);
                divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", priorOperation, braced);
            }else{
                add(toDiv, currIndex + 1, resultString + nums[currIndex] + "+", braced);
                subtract(toDiv, currIndex + 1, resultString + nums[currIndex] + "-", braced);
                multiply(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "*", "", braced);
                divide(sum, toDiv, currIndex + 1, resultString + nums[currIndex] + "/", "", braced);
            }
            
            if(braces){
                //allows braces, not implemented
            }

        }

    }
    public class StringProcessor implements BestFitInt{
        private long nums [];//array for numbers
        private long expectedResult;
        private CalculationMethods calc;
        private boolean advanced;//flag indicating usage of multiplication and division
        private boolean braces;//flag indicating usage of parentheses
        
        //checks if the string contains numbers only and if it's a non-zero length string
        private boolean preparations(String digits, long expectedResult){
            this.expectedResult = expectedResult;

            if(digits.length() < 2){
                System.out.println("The string must be of length 2+");
                return false;
            }

            nums = new long[digits.length()];

            try{    

                //convert the string to arr of integers
                for(int i = 0; i < nums.length; i++){

                    char temp = digits.charAt(i);
                    nums[i] = Long.parseLong(Character.toString(temp));
                    
                }

            }catch(NumberFormatException ex){
                System.out.println("Incorrect input string.");
                return false;
            }

            calc = new CalculationMethods(nums, expectedResult, advanced, braces);

            return true;
        }

        @Override
        public String fitPlusMinus(String digits, long expectedResult) {
            
            //advanced math functions and braces are turned off 
            boolean launch = preparations(digits, expectedResult);

            if(!launch){
                
                if(advanced){
                    return "false";
                }
                
                return "";
            }
            
            calc.add(nums[0], 1, nums[0] + "+", false);
            calc.subtract(nums[0], 1, nums[0] + "-", false);
            
            if(calc.getResults().size() > 0){
                return calc.getResults().get(0);
            }else{
                return "";
            }
            
        }

        @Override
        public String fit(String digits, long expectedResult) {
            advanced = true;
            String result = fitPlusMinus(digits, expectedResult);

            if(result.equals("false")){
                return "";
            }
            
            calc.multiply(0, nums[0], 1, nums[0] + "*", "", false);
            calc.divide(0, nums[0], 1, nums[0] + "/", "", false);
            
            advanced = false;

            if(calc.getResults().size() > 0){
                return calc.getResults().get(0);
            }else{
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
        
        String result = proc.fitPlusMinus("7654", 4);
        System.out.println("fitPlusMinus(): " + result);
        
        String result1 = proc.fit("8363653", 7);
        System.out.println("fit():" + result1);
    }

}