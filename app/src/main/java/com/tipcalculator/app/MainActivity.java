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
import android.widget.Toast;

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
    private Double totalAmountD,tipAmountD,billAmountD;
    private Double numPeopleToSplitD,amountPerPersonD,tipValueD;
    private TipSelection tipState;

    public interface Constants {
        String LOG = "com.tipcalculator.app";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       setup();






        //on edit text changed on numOfPeopleSplit
        etNumPeopleToSplit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Toast.makeText(getBaseContext(),"after text is changed",Toast.LENGTH_SHORT).show();
                numPeopleToSplit =  etNumPeopleToSplit.getText().toString();
                Log.i(Constants.LOG,numPeopleToSplit);

                // this if statement is to prevent the app crashing when you hit backspace
                // this is to make sure that the text box is not null
                if ((numPeopleToSplit.length() != 0))
                {
                    Toast.makeText(getBaseContext(), numPeopleToSplit, Toast.LENGTH_SHORT).show();

                   calculateBill();

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //on edit text changed on total amount edit
        etTotalAmount.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // rgTip.setEnabled(true);
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


        etOtherTip.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                tipValue = etOtherTip.getText().toString();
                if (tipValue.length() != 0)
                {

                    // rgTip.setEnabled(true);
                    //totalAmount = etTotalAmount.getText().toString();
                    tipValueD = Double.parseDouble(tipValue);
                    Toast.makeText(getBaseContext(),tipValueD.toString(),Toast.LENGTH_SHORT).show();

                    calculateBill();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


                // set the handler for the radio buttons
        rgTip.setOnCheckedChangeListener(this);

        // convert from double to string and assign it to editText tip amount

       /* double total = 44;
        String total2= new Double(total).toString();*/


      //  String s = String.valueOf(tipAmountD);

      //  if (BuildConfig.DEBUG) {
//            Log.d(Constants.LOG, tipAmountD.toString());
      //  }
      //  etTipAmount.setText(s);
       // Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();

        // tip amount+total amount = bill amount
   /*     Double billAmountD = tipAmountD + Double.parseDouble(billAmount);
        etBillAmount.setText(billAmountD.toString());

        // calculate the amount per person - string to double and back to string.
        Double numPeopleToSplitD = Double.parseDouble(numPeopleToSplit);
        Double amountPerPersonD = billAmountD / numPeopleToSplitD;
        etTotalPerPerson.setText(amountPerPersonD.toString()); */

    }

    private void setup() {

        Toast.makeText(getBaseContext(),"inside set up method",Toast.LENGTH_SHORT).show();

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


/*
    private void writeItems()
    {
        File filesDir = getFilesDir();
        //create the file based on that path that gives access to handle
        File tipCalFile = new File(filesDir,"tipCal.txt");
        // try ... catch it's possible that file do not exist
        // we don't the app to crash
        try {
            // load the items from the file
           // FileUtils.writeLines(tipCalFile,todo);
        }
        // if the file does not exist, create a new arraylist
        catch(IOException e){
            e.printStackTrace();
        }

    }

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
        //double tipPercent = 0.0;

        Toast.makeText(getBaseContext(),"inside on checked changed", Toast.LENGTH_SHORT).show();

        // checkedId is the RadioButton selected
        switch(checkedId)
        {
            case R.id.rbFifteen:
                Toast.makeText(getBaseContext(),
                        "fifteen is selected", Toast.LENGTH_SHORT).show();

               tipState = TipSelection.FIFTEEN;

               // tipPercent = FIFTEEN;
               /* tipAmountD = totalAmountD * FIFTEEN;
                System.out.println("the tip amount double = " + tipAmountD);
                System.out.println("the total amount double = " + totalAmountD);
                Log.i("constants.LOG","inside fifteen");*/
                //calculateTip(tipPercent);
                disableOtherTipEditText();
                //try this first time
                calculateBill();
                break;

            case R.id.rbTwentyfive:
                Toast.makeText(getBaseContext(),
                        "twenty five is selected", Toast.LENGTH_SHORT).show();
                tipState = TipSelection.TWENTYFIVE;
                //tipPercent = TWENTYFIVE;
               //calculateTip(tipPercent);
                //tipAmountD = totalAmountD * TWENTYFIVE;
                disableOtherTipEditText();
                calculateBill();
                break;
            case R.id.rbOther:
                Toast.makeText(getBaseContext(),
                        "other is selected", Toast.LENGTH_SHORT).show();
                tipState = TipSelection.OTHER;
                enableOtherTipEditText();

                //tipPercent = Double.parseDouble(etOtherTip.getText().toString());
               // calculateTip(tipPercent);
               /* String otherTip = etOther.getText().toString();

                tipAmountD = totalAmountD + Double.parseDouble(etOther.getText().toString());*/
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

        //Log.i(Constants.LOG,"inside caldulate Bill");

        Toast.makeText(getBaseContext(),"inside calculate bill",Toast.LENGTH_SHORT).show();


        //calculate tip
        if (tipState == TipSelection.FIFTEEN)
        {
            //Toast.makeText(getBaseContext(),"state is fifteen", Toast.LENGTH_SHORT).show();
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



        //Log.i(Constants.LOG,tipAmountD.toString());

        // calculate Bill Amount
        // tip amount+total amount = bill amount
        billAmountD = tipAmountD + totalAmountD;
        Log.i(Constants.LOG,billAmountD.toString());

       // etBillAmount.setText(billAmountD.toString());

        // convert from double to string and assign it to editText tip amount
       // etTipAmount.setText(tipAmountD.toString());




            // calculate the amount per person - string to double and back to string.
       // Toast.makeText(getBaseContext(),"num people to split" + numPeopleToSplit,Toast.LENGTH_SHORT).show();
        numPeopleToSplitD = Double.parseDouble(etNumPeopleToSplit.getText().toString());
        Toast.makeText(getBaseContext(),"numofpeople "+ numPeopleToSplitD, Toast.LENGTH_SHORT).show();

        // check division by zero
        if (numPeopleToSplitD > 0)
        {
            amountPerPersonD = billAmountD / numPeopleToSplitD;

        }
        Toast.makeText(getBaseContext(),"perperson "+ amountPerPersonD, Toast.LENGTH_SHORT).show();

        // Format and display
        // NumberFormat formatter = NumberFormat.getCurrencyInstance();
        //etTotalPerPerson.setText(amountPerPersonD.toString());

        display();

    }

    private void display()
    {


       // set tip amount,bill amount, total per person
       // convert double values to string
        etTipAmount.setText(tipAmountD.toString());
        etBillAmount.setText(billAmountD.toString());
        // Format and display
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
