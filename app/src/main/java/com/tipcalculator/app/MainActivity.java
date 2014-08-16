package com.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

// changes made Aug 7th
// set the billAmountD, tipAmountD = 0.0
// clearEditText method: clears the edittext of tip, total amount
// whent the totalamount is 0.
// totalamount is 0.0 (basically clear the exitText)

// since float and double does not give exact values, as per stack overflow,
// use bigdecimal with string constructor
public class MainActivity extends Activity implements OnCheckedChangeListener{

    private EditText etNumPeopleToSplit;
    private EditText etTotalAmount;

    private RadioButton rbTipButton;
    private RadioButton rbOther;
    private RadioButton rbFifteen;
    private RadioButton rbTwenty;
    private RadioButton rbTwentyFive;
    private RadioGroup rgTip;

    private TextView tvTotalPerPerson;
   /* public EditText etTipAmount;
    public EditText etBillAmount;
    public EditText etOtherTip; */

    private TextView tvTipAmount;
    private TextView tvBillAmount;
    private EditText etOtherTip;

    public static final double FIFTEEN = .15;
    public static final double TWENTY = .20;
    public static final double TWENTYFIVE = .25;


    public enum TipSelection{FIFTEEN,TWENTY,TWENTYFIVE,OTHER};

    private String totalAmount,billAmount,numPeopleToSplit,tipValue;
    private BigDecimal totalAmountD;

    private BigDecimal tipAmountD,billAmountD;
    private BigDecimal numPeopleToSplitD;;
    private BigDecimal amountPerPersonD,tipValueD;

    /*private Double totalAmountD = 0.0;
    private Double tipAmountD = 0.0,billAmountD = 0.0; // Added this recently
    private Double numPeopleToSplitD,amountPerPersonD,tipValueD; */
    private TipSelection tipState;

    public interface Constants {
        String LOG = "com.tipcalculator.app";
    }

    // decimalformat.
    //isnumber (string)

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Toast.makeText(getBaseContext(),"inside on create " ,Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        Toast.makeText(getBaseContext(),"after content menu ", Toast.LENGTH_SHORT).show();
        setup();

        etTotalAmount.requestFocus();
        //on edit text changed on numOfPeopleSplit
        etNumPeopleToSplit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                String inputString = s.toString();
                try {
                numPeopleToSplit = etNumPeopleToSplit.getText().toString();
                }catch (NullPointerException e){
                    numPeopleToSplit = "1"; // set "1" as default
                }


              //  else
             //   {

                    Log.i(Constants.LOG,numPeopleToSplit);
                    Log.i(Constants.LOG,inputString);
                    try {
                        // convert string to double - numPeopleTosplit
                        //calling the string constructor on BigDecimal
                        numPeopleToSplitD = new BigDecimal(numPeopleToSplit);


                        //numPeopleToSplitD = Double.parseDouble(numPeopleToSplit);
                    }
                    catch (NumberFormatException e)
                    {
                        numPeopleToSplitD = BigDecimal.ONE;

                       // etNumPeopleToSplit.setText("1");
                        return;
                    }
                    calculateBill();
               // }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });

        //on edit text changed on total amount edit
        etTotalAmount.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

               // String inputString = s.toString(); // this is just for validation check
                try {
                totalAmount = etTotalAmount.getText().toString();
                }catch (NullPointerException e)
                {
                    totalAmount = "5"; // set $5 as the default
                    return;
                }

               // if (totalAmount.length() != 0)
                if ((totalAmount == null) && (totalAmount.equals("")))
                {
                    Toast.makeText(getBaseContext(),"enter the total amount greater than $5:00 ",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                {
                    //totalAmountD = Double.parseDouble(totalAmount);
                    try
                    {
                        totalAmountD = new BigDecimal(totalAmount);
                    } catch (NumberFormatException e)
                    {

                        //etTotalAmount.setText("5");

                        return;
                    }
                    Log.d(Constants.LOG,"Before calling calculateBill");
                    Log.d(Constants.LOG,"totalAmountD should be either 5 or some other amount " +
                        totalAmountD);
                   // Log.d(Constants.LOG,"the value of totalAmountd = "+ totalAmountD);

                    calculateBill(); //uncomment aug 15th, friday
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        //listener for other tip edit text
        etOtherTip.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                tipValue = etOtherTip.getText().toString();
                if (tipValue.length() != 0) {
                    try {
                        //tipValueD = Double.parseDouble(tipValue);
                        tipValueD = new BigDecimal(tipValue);
                    }
                    catch (NumberFormatException e)
                    {
                        etOtherTip.setText("");
                    }
                    calculateBill();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        // listener for the radio buttons
        rgTip.setOnCheckedChangeListener(this);

    }

    private void setup() {

        etNumPeopleToSplit = (EditText)findViewById(R.id.etNumPeopleToSplit);
        etNumPeopleToSplit.setText("1");
        etTotalAmount = (EditText)findViewById(R.id.etTotalAmount);
        etTotalAmount.setText("0.0");
        tvTipAmount = (TextView)findViewById(R.id.tvTipAmount);
        //tvTipAmount.setEnabled(false);
        tvBillAmount = (TextView)findViewById(R.id.tvBillAmount);
        //tvBillAmount.setEnabled(false);
        tvTotalPerPerson = (TextView)findViewById(R.id.tvTotalPerPerson);
        tvTotalPerPerson.setEnabled(false);
        etOtherTip = (EditText)findViewById(R.id.etOtherTip);
        disableOtherTipEditText();

        // find the group id for radio group
        // make sure that at least one button is checked, by checking for -1
        rgTip = (RadioGroup)findViewById(R.id.radioGroup);
        rbFifteen = (RadioButton)findViewById(R.id.rbFifteen);
        rbTwenty = (RadioButton)findViewById(R.id.rbTwenty);
        rbTwentyFive = (RadioButton)findViewById(R.id.rbTwentyfive);
        rbOther = (RadioButton)findViewById(R.id.rbOther);

        // clear all the radio buttons
        rgTip.clearCheck();

        //set the default radio button to radio button fifteen
        // if I don't set this the listener is not firing
        rbFifteen.setChecked(true);
        tipState = TipSelection.FIFTEEN;
        numPeopleToSplitD = BigDecimal.ONE; //default
    }

    //clear the edit boxes of tipamount,total amount
    //ADDED THIS RECENTLY
    private void clearEditText()
    {
      // tipAmountD = 0.0;
       tvTipAmount.setText("0.0");
       etTotalAmount.setText("0.0");
       tvBillAmount.setText("0.0");

    }

  /*  private void writeUserDataToFile()
    {
        File filesDir = getFilesDir();
        //create the file based on that path that gives access to handle
        File tipCalFile = new File(filesDir,"tipCal.txt");
        // try ... catch it's possible that file do not exist
        // we don't the app to crash
        try {
            // load the items from the file
            FileUtils.writeLines(tipCalFile,todo);
        }
        // if the file does not exist, create a new arraylist
        catch(IOException e){
            e.printStackTrace();
        }

    }
()
    private void readItems()
    {

        //create an absolute path
        File filesDir = getFilesDir();
        //create the file based on that path that gives access to handle
        File todoFile = new File(filesDir,"todo.txt");
        // try ... catch it's possible that file do not exist
        // we don't the app to crash
        try {
            // load the items from the file
            todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        // if the file does not exist, create a new arraylist
        catch(IOException e){
            todoItems = new ArrayList<String>();
        }
    }
*/
    //enable and disable other tip edit text
    // disable for tip percentage 15 and tip percentage = 25
    private void disableOtherTipEditText()
    {
       // etOtherTip = (EditText)findViewById(R.id.etOtherTip);
        etOtherTip.setEnabled(false);
        etOtherTip.setInputType(InputType.TYPE_NULL);
        etOtherTip.setFocusableInTouchMode(false);
        etOtherTip.setText("");
        etOtherTip.clearFocus();
    }
    private void enableOtherTipEditText()
    {
        etOtherTip.setEnabled(true);
        etOtherTip.setInputType(InputType.TYPE_CLASS_NUMBER);
        etOtherTip.setFocusableInTouchMode(true);
        etOtherTip.requestFocus();

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        // checkedId is the RadioButton selected
        switch(checkedId)
        {
            case R.id.rbFifteen:
               tipState = TipSelection.FIFTEEN;
               disableOtherTipEditText();
               calculateBill();
               break;

            case R.id.rbTwenty:
                tipState = TipSelection.TWENTY;
                disableOtherTipEditText();
                calculateBill();
                break;
            case R.id.rbTwentyfive:
                tipState = TipSelection.TWENTYFIVE;
                disableOtherTipEditText();
                calculateBill();
                break;
            case R.id.rbOther:
                tipState = TipSelection.OTHER;
                enableOtherTipEditText();
                break;

        }
        // the reason you don't want to call outside because when the other
        // radio button is selected, I would like to show the existing values
        // only when the user enters the new value, the new bill is calculated.
        //calculateBill();
    }


    private void calculateBill()
    {
       // double tipPercent = 0.0;
       // BigDecimal tipPercent = new BigDecimal("0.0");
        //BigDecimal tipPercent = BigDecimal.valueOf(0.0);
        BigDecimal tipPercent = new BigDecimal("0.0");

        //calculate tip
        if (tipState == TipSelection.FIFTEEN)
        {
            tipPercent = BigDecimal.valueOf(FIFTEEN);
        }
        else if (tipState == TipSelection.TWENTY)
        {
            tipPercent = BigDecimal.valueOf(TWENTY);
        }
        else if (tipState == TipSelection.TWENTYFIVE)
        {
            tipPercent = BigDecimal.valueOf(TWENTYFIVE);
        }
        else
           // tipAmountD = tipValueD;
           tipAmountD = tipValueD;
          // tipAmountD = BigDecimal.valueOf(tipValueD);

        Log.d(Constants.LOG," inside calculate bill");
        Log.d(Constants.LOG,"tipPercent = "+ tipPercent);
        Log.d(Constants.LOG,"tipValueD = "+ tipValueD);

        // calculate tip if the user selects percentages.
        if (tipState != TipSelection.OTHER)
            tipAmountD = totalAmountD.multiply(tipPercent);
            //tipAmountD = totalAmountD * tipPercent;

        // calculate Bill Amount
        // tip amount+total amount = bill amount
        //billAmountD = tipAmountD + totalAmountD;
        Log.d(Constants.LOG,"totalAmountD = " + totalAmountD);
        Log.d(Constants.LOG,"tipPercent = "+ tipPercent);
        //billAmountD = tipAmountD.add(totalAmountD);
        billAmountD = totalAmountD.add(tipValueD);

       // calculate the amount per person - string to double and back to string.
       // numPeopleToSplitD = Double.parseDouble(etNumPeopleToSplit.getText().toString());
        //numPeopleToSplitD =


        // check division by zero
       // if ( (numPeopleToSplitD > 0) && (billAmountD > 0) )
        if ( (numPeopleToSplitD.compareTo(BigDecimal.ZERO) > 0)  &&
                (billAmountD.compareTo(BigDecimal.ZERO) > 0) )
        {
            try{
           // amountPerPersonD = billAmountD / numPeopleToSplitD;
           // ArithmeticException error: Non-terminating decimal expansion; no exact representation
           // when the recurring decimal happens, it would be
             amountPerPersonD = billAmountD.divide(numPeopleToSplitD,2,RoundingMode.HALF_UP);
            }catch (NumberFormatException e)
            {
                etNumPeopleToSplit.setText("1");
            }

        }
        else
        {
           // billAmountD = 0.0;
           // totalAmountD = 0.0;
          //  billAmountD.se(BigDecimal.valueOf(0));
            billAmountD = BigDecimal.ZERO;
            tipAmountD = BigDecimal.ZERO;
            //amountPerPersonD = 0.0;
            amountPerPersonD = BigDecimal.ZERO;
        }

        display();

    }

    private void display()
    {
       // set tip amount,bill amount, total per person
       // convert double values to string
        tvTipAmount.setText(tipAmountD.toString());
        tvBillAmount.setText(billAmountD.toString());

        // Format NOT DONE -------
        // NumberFormat formatter = NumberFormat.getCurrencyInstance();
        tvTotalPerPerson.setText(amountPerPersonD.toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
