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

    private String totalAmount,billAmount,numPeopleToSplit;
    private Double totalAmountD,tipAmountD;
    private Double numPeopleToSplitD,amountPerPersonD;

    public interface Constants {
        String LOG = "com.tipcalculator.app";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


       /* etOtherTip = (EditText)findViewById(R.id.etOtherTip);
        etOtherTip.setEnabled(false);
        etOtherTip.setInputType(InputType.TYPE_NULL);
        etOtherTip.setFocusableInTouchMode(false);
        etOtherTip.clearFocus();*/

        disableOtherTipEditText();
//

        // find the group id for radio group
        // make sure that at least one button is checked, by checking for -1
        rgTip = (RadioGroup)findViewById(R.id.radioGroup);
        rbFifteen = (RadioButton)findViewById(R.id.rbFifteen);
        rbTwentyFive = (RadioButton)findViewById(R.id.rbTwentyfive);
        rbOther = (RadioButton)findViewById(R.id.rbOther);


        //on edit text changed on numOfPeopleSplit
        etNumPeopleToSplit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                numPeopleToSplit =  etNumPeopleToSplit.getText().toString();

                // this if statement is to prevent the app crashing when you hit backspace
                // this is to make sure that the text box is not null
                if ((numPeopleToSplit.length() != 0))
                {
                    Toast.makeText(getBaseContext(), numPeopleToSplit, Toast.LENGTH_SHORT).show();
                    rbFifteen.setChecked(true);
                    calculateTip(FIFTEEN);
                 //   calculateBill();

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
                    Toast.makeText(getBaseContext(),totalAmount,Toast.LENGTH_SHORT).show();
                    etBillAmount.setText(totalAmount);
                    billAmount = etBillAmount.getText().toString();
                    rbFifteen.setChecked(true);
                    calculateTip(FIFTEEN);
                    calculateBill();
                }
              /*  if (totalAmount.matches("0.0"))
                {
                    etBillAmount.setText(totalAmount);
                    rbFifteen.setChecked(true);
                    calculateBill();


                } */

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });



        // clear all the radio buttons
        rgTip.clearCheck();

        //set the default radio button to radio button fifteen
        // if I don't set this the listener is not firing
        rbFifteen.setChecked(true);


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
        etOtherTip = (EditText)findViewById(R.id.etOtherTip);
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
        double tipPercent = 0.0;
        // checkedId is the RadioButton selected
        switch(checkedId)
        {
            case R.id.rbFifteen:
                Toast.makeText(getBaseContext(),
                        "fifteen is selected", Toast.LENGTH_SHORT).show();

                tipPercent = FIFTEEN;
               /* tipAmountD = totalAmountD * FIFTEEN;
                System.out.println("the tip amount double = " + tipAmountD);
                System.out.println("the total amount double = " + totalAmountD);
                Log.i("constants.LOG","inside fifteen");*/
                calculateTip(tipPercent);
                disableOtherTipEditText();
                break;

            case R.id.rbTwentyfive:
                Toast.makeText(getBaseContext(),
                        "twenty five is selected", Toast.LENGTH_SHORT).show();
                tipPercent = TWENTYFIVE;
                calculateTip(tipPercent);
                //tipAmountD = totalAmountD * TWENTYFIVE;
                disableOtherTipEditText();
                break;
            case R.id.rbOther:
                Toast.makeText(getBaseContext(),
                        "other is selected", Toast.LENGTH_SHORT).show();

                enableOtherTipEditText();
                tipPercent = Double.parseDouble(etOtherTip.getText().toString());
                calculateTip(tipPercent);
               /* String otherTip = etOther.getText().toString();

                tipAmountD = totalAmountD + Double.parseDouble(etOther.getText().toString());*/
                break;

        }

        calculateBill();
        // convert from double to string and assign it to editText tip amount
       /* etTipAmount.setText(tipAmountD.toString());

        // tip amount+total amount = bill amount
        Double billAmountD = tipAmountD + Double.parseDouble(billAmount);
        etBillAmount.setText(billAmountD.toString());

        // calculate the amount per person - string to double and back to string.
        Toast.makeText(getBaseContext(),numPeopleToSplit,Toast.LENGTH_SHORT).show();
        Double numPeopleToSplitD = Double.parseDouble(numPeopleToSplit);
        Double amountPerPersonD = billAmountD / numPeopleToSplitD;

        // Format and display
      //  NumberFormat formatter = NumberFormat.getCurrencyInstance();
        etTotalPerPerson.setText(amountPerPersonD.toString());
        */

    }

    private void calculateTip(double tipPercent)
    {
        tipAmountD = totalAmountD * tipPercent;
        System.out.println("the tip amount double = " + tipAmountD);
        System.out.println("the total amount double = " + totalAmountD);
        Log.i("constants.LOG","inside fifteen");

    }
    private void calculateBill()
    {
        // convert from double to string and assign it to editText tip amount
        etTipAmount.setText(tipAmountD.toString());

        // tip amount+total amount = bill amount
        Double billAmountD = tipAmountD + Double.parseDouble(billAmount);
        etBillAmount.setText(billAmountD.toString());

        // calculate the amount per person - string to double and back to string.
        Toast.makeText(getBaseContext(),"num people to split" + numPeopleToSplit,Toast.LENGTH_SHORT).show();
        numPeopleToSplitD = Double.parseDouble(numPeopleToSplit);

        // check division by zero
        if (numPeopleToSplitD > 0)
        {
            amountPerPersonD = billAmountD / numPeopleToSplitD;
        }


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
