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

public class MainActivity extends Activity implements OnCheckedChangeListener{

    private EditText etNumPeopleToSplit;
    private EditText etTotalAmount;
    private RadioButton rbTipButton;
    private RadioButton rbOther;
    private RadioButton rbFifteen;
    private RadioButton rbTwentyFive;
    private RadioGroup rgTip;

    public  EditText etTotalPerPerson;
    public EditText etTipAmount;
    public EditText etBillAmount;
    public EditText etOtherTip;

    public static final double FIFTEEN = .15;
    public static final double TWENTYFIVE = .25;

    public enum TipSelection{FIFTEEN,TWENTYFIVE,OTHER};

    private String totalAmount,billAmount,numPeopleToSplit,tipValue;
    private Double totalAmountD = 0.0;
    private Double tipAmountD,billAmountD;
    private Double numPeopleToSplitD,amountPerPersonD,tipValueD;
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
        setContentView(R.layout.activity_main);
        setup();

        //on edit text changed on numOfPeopleSplit
        etNumPeopleToSplit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


                String inputString = s.toString();
                numPeopleToSplit = etNumPeopleToSplit.getText().toString();

                if ((inputString != null) && (!inputString.equals("")))
                {

                    Log.i(Constants.LOG,numPeopleToSplit);
                    Log.i(Constants.LOG,inputString);
                    try {
                        // convert string to double - numPeopleTosplit
                        numPeopleToSplitD = Double.parseDouble(numPeopleToSplit);
                    }
                    catch (NumberFormatException e)
                    {
                        etNumPeopleToSplit.setText("");
                    }
                    calculateBill();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

              /*

                String inputString = s.toString();

                if ((inputString != null) && (!inputString.equals("")))
                {
                    numPeopleToSplit = etNumPeopleToSplit.getText().toString();
                    Log.i(Constants.LOG,numPeopleToSplit);
                    Log.i(Constants.LOG,inputString);
                    try {
                        // convert string to double - numPeopleTosplit
                        numPeopleToSplitD = Double.parseDouble(numPeopleToSplit);
                    }
                    catch (NumberFormatException e)
                    {
                        etNumPeopleToSplit.setText("");
                    }
                    calculateBill();

                }*/

                //numPeopleToSplit =  etNumPeopleToSplit.getText().toString();

                // this if statement is to prevent the app crashing when you hit backspace
                // this is to make sure that the text box is not null
                //if ((numPeopleToSplit.length() != 0))
                //{
               //     calculateBill();
               // }
            }
        });

        //on edit text changed on total amount edit
        etTotalAmount.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                totalAmount = etTotalAmount.getText().toString();
                if (totalAmount.length() != 0)
                {
                    totalAmountD = Double.parseDouble(totalAmount);
                    calculateBill();
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
                        tipValueD = Double.parseDouble(tipValue);
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

                //change this to
                // String inputString =
             /*   if (tipValue.length() != 0) {
                    try {
                        tipValueD = Double.parseDouble(tipValue);
                    }
                    catch (NumberFormatException e)
                    {
                        etOtherTip.setText("");
                    }
                    calculateBill();
                }//end of if
                */
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
        etTipAmount = (EditText)findViewById(R.id.etTipAmount);
        etTipAmount.setEnabled(false);
        etBillAmount = (EditText)findViewById(R.id.etBillAmount);
        etBillAmount.setEnabled(false);
        etTotalPerPerson = (EditText)findViewById(R.id.etTotalPerPerson);
        etTotalPerPerson.setEnabled(false);
        etOtherTip = (EditText)findViewById(R.id.etOtherTip);
        disableOtherTipEditText();

        // find the group id for radio group
        // make sure that at least one button is checked, by checking for -1
        rgTip = (RadioGroup)findViewById(R.id.radioGroup);
        rbFifteen = (RadioButton)findViewById(R.id.rbFifteen);
        rbTwentyFive = (RadioButton)findViewById(R.id.rbTwentyfive);
        rbOther = (RadioButton)findViewById(R.id.rbOther);

        // clear all the radio buttons
        rgTip.clearCheck();

        //set the default radio button to radio button fifteen
        // if I don't set this the listener is not firing
        rbFifteen.setChecked(true);
        tipState = TipSelection.FIFTEEN;
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
        double tipPercent = 0.0;

        //calculate tip
        if (tipState == TipSelection.FIFTEEN)
        {
            tipPercent = FIFTEEN;
        }
        else if (tipState == TipSelection.TWENTYFIVE)
        {
            tipPercent = TWENTYFIVE;
        }
        else
            tipAmountD = tipValueD;

        // calculate tip if the user selects percentages.
        if (tipState != TipSelection.OTHER)
            tipAmountD = totalAmountD * tipPercent;

        // calculate Bill Amount
        // tip amount+total amount = bill amount
        billAmountD = tipAmountD + totalAmountD;

       // calculate the amount per person - string to double and back to string.
        numPeopleToSplitD = Double.parseDouble(etNumPeopleToSplit.getText().toString());


        // check division by zero
        if ( (numPeopleToSplitD > 0) && (billAmountD > 0) )
        {
            try{
            amountPerPersonD = billAmountD / numPeopleToSplitD;
            }catch (NullPointerException e)
            {
                etNumPeopleToSplit.setText("1");
            }

        }
        else
        {
            billAmountD = 0.0;
            tipAmountD = 0.0;
            amountPerPersonD = 0.0;
        }

        display();

    }

    private void display()
    {
       // set tip amount,bill amount, total per person
       // convert double values to string
        etTipAmount.setText(tipAmountD.toString());
        etBillAmount.setText(billAmountD.toString());

        // Format NOT DONE -------
        // NumberFormat formatter = NumberFormat.getCurrencyInstance();
        etTotalPerPerson.setText(amountPerPersonD.toString());
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
