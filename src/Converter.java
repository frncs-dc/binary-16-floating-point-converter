import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Converter extends JFrame {
    private JButton convertBtn;
    private JLabel title;
    private JPanel converterPanel;
    private JTextField inputField;
    private JLabel inputLabel;
    private JLabel outputLabel;
    private JButton outputInTextFileButton;
    private JLabel signBitField;
    private JLabel expField;
    private JLabel mantissaField;

    /**
     * @param input the input to check if binary or decimal
     * @return true if binary value, false if decimal
     */
    static boolean checkBinary(String input) {
        return input.contains("x2");
    }

    /**
     * @param input to convert to Binary
     * @return  converted Binary
     */
    public static String decimalToBinary(String input){ //  function to convert decimal to binary
//      String[] inputs = input.split("x10");
        int wholeNumber = 0;
        float deciNumber = 0.0f;

        if(input.contains(".")) {  // if given string has a decimal point
            String[] inputs = input.split("\\.");
            wholeNumber = Integer.parseInt(inputs[0]); // whole number value
            deciNumber = 0.0f;
            String strDeciNumber = "0." + Integer.parseInt(inputs[1]);
            deciNumber = Float.parseFloat(strDeciNumber); // decimal point value
        }
        else{ //  if  given does not have a decimal point
            String[] inputs = input.split("\\.");
            wholeNumber = Integer.parseInt(inputs[0]); // whole number value
            deciNumber = 0.0f;
        }
//        gets the exponent
//        String[] exponent = inputs[1].split("\\^");
//        int exp = Integer.parseInt(exponent[1]);

        int[] binaryNum = new int[1000];
        int i = 0;
        while(wholeNumber > 0){
            binaryNum[i] = wholeNumber % 2;  //remainder is stored
            wholeNumber = wholeNumber / 2;   //divide for next iteration until n = 0
            i++;
        }

        String finalBinary = "";

        for(int j = i - 1; j >= 0; j--){// printing of binary in reverse order
            finalBinary = finalBinary + binaryNum[j];  //concat reversely the array that contains the modulo by 2
                                                       // as we store the modulo from end to  start
        }

        if(deciNumber > 0.0000){
            finalBinary += "."; // appending the decimal point

            while(deciNumber > 0.0){
                deciNumber *= 2;

                if(deciNumber >= 1.0){
                    deciNumber -= 1;  //if the product of multiplying by 2 is more than 1.(some number)  subtract 1
                    finalBinary += "1"; //  if true we now append 1
                }
                else {
                    finalBinary += "0"; // if not more than 1 append 0
                }
            }
        }

        return String.valueOf(finalBinary); // returns the converted decimal to binary as string
    }

    /** @param input 
     *
     */
    static String expand(String input){
        String[] expandedNumber = input.split("[x^]");
        double number = Double.parseDouble(expandedNumber[0]);
        int base = Integer.parseInt(expandedNumber[1]);
        int exponent = Integer.parseInt(expandedNumber[2]);

        return String.valueOf(number * Math.pow(base, exponent));
    }

    static String getHexLetter(int value){
        switch (value){
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            default:
                return String.valueOf(value);
        }
    }

    static String convertToHex(String binaryInput){
        // 1000 0110 0010 0001
        String[] binGroups = new String[4];
        int[] hexValues = new int[]{8, 4, 2, 1};
        int[] hexRawValue = new int[4];
        String finalHex = "";
        int beginIndex = 0;
        int endIndex = 3;
        for(int i = 0; i < 4; i++){
            binGroups[i] = binaryInput.substring(beginIndex,endIndex);
            beginIndex+=4;
            endIndex+=4;
        }

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++) {
                hexRawValue[i] += (binGroups[i].charAt(j) - '0') * hexValues[j];
            }

            finalHex += getHexLetter(hexRawValue[i]);
        }

        return finalHex;
    }

    public Converter() {
        convertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Output output = new Output();
                String value = null;
                String input = inputField.getText();
                boolean isNegative = input.contains("-");
                if (isNegative){input = input.substring(1);}

                if (!checkBinary(input)){ // check if Binary
                    value = decimalToBinary(expand(input)); // if not convert
                }

                // TODO: make function to standardize binary to 1.f before passing to extractValues

                output.extractValues(value);

                signBitField.setText(output.sign);
                expField.setText(output.exponent);
                mantissaField.setText(output.mantissa);
            }
        });


        this.setContentPane(this.converterPanel);
        this.setTitle("Binary-16 floating point converter");
        this.setSize(300,200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
