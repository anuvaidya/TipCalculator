package com.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    //private RadioButton rbTipButton;
    private RadioButton rbOther;
    private RadioButton rbFifteen;
    private RadioButton rbTwenty;
    private RadioButton rbTwentyFive;
    private RadioGroup rgTip;

    private TextView tvTotalPerPerson;

    private TextView tvTipAmount;
    private TextView tvBillAmount;
    private EditText etOtherTip;

    private Button mBtnSave;
    private Button mBtnClear;
    private Button mBtnExit;

    public static final double FIFTEEN = .15;
    public static final double TWENTY = .20;
    public static final double TWENTYFIVE = .25;


    public enum TipSelection{FIFTEEN,TWENTY,TWENTYFIVE,OTHER};

    private String totalAmount,billAmount,numPeopleToSplit,tipValue;

  /*  private BigDecimal tipAmountD,billAmountD; TODO UNCOMMENT
    private BigDecimal numPeopleToSplitD,totalAmountD;
    private BigDecimal amountPerPersonD,tipValueD;
*/
    /*private Double totalAmountD = 0.0;
    private Double tipAmountD = 0.0,billAmountD = 0.0; // Added this recently
    private Double numPeopleToSplitD,amountPerPersonD,tipValueD; */

    private TipCalculatorData tipCalcObject;
    private TipSelection tipState;

    public interface Constants {
        String LOG = "com.tipcalculator.app";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"inside ",Toast.LENGTH_SHORT).show();
        tipCalcObject = new TipCalculatorData();

        setup();
        setUpListener();
    }



    private void setup() {


        etNumPeopleToSplit = (EditText)findViewById(R.id.etNumPeopleToSplit);
        etNumPeopleToSplit.setText("1");
        etNumPeopleToSplit.setInputType(InputType.TYPE_CLASS_TEXT);

        etTotalAmount = (EditText)findViewById(R.id.etTotalAmount);
        etTotalAmount.setText("0.0");
        etTotalAmount.setInputType(InputType.TYPE_CLASS_TEXT);
        etTotalAmount.requestFocus();


        tvTipAmount = (TextView)findViewById(R.id.tvTipAmount);
        tvBillAmount = (TextView)findViewById(R.id.tvBillAmount);
        tvTotalPerPerson = (TextView)findViewById(R.id.tvTotalPerPerson);
        etOtherTip = (EditText)findViewById(R.id.etOtherTip);
        disableOtherTipEditText();

        // find the group id for radio group
        // make sure that at least one button is checked, by checking for -1
        rgTip = (RadioGroup)findViewById(R.id.radioGroup);
        rbFifteen = (RadioButton)findViewById(R.id.rbFifteen);
        rbTwenty = (RadioButton)findViewById(R.id.rbTwenty);
        rbTwentyFive = (RadioButton)findViewById(R.id.rbTwentyfive);
        rbOther = (RadioButton)findViewById(R.id.rbOther);

        mBtnSave = (Button) findViewById(R.id.btnSave);
        mBtnClear = (Button) findViewById(R.id.btnClear);
        mBtnExit = (Button) findViewById(R.id.btnExit);

        // clear all the radio buttons
        rgTip.clearCheck();

        //default - 15%, num of people is 1
        rbFifteen.setChecked(true);
        tipState = TipSelection.FIFTEEN;
        tipCalcObject.setNumPeopleToSplitD(BigDecimal.ONE);
        //numPeopleToSplitD = BigDecimal.ONE; todo uncomment if necessary


    }


    private void setUpListener()
    {
        etNumPeopleToSplit.addTextChangedListener(myetNumPeopleSplitTextWatcherListener);
        etTotalAmount.addTextChangedListener(myetTotalAmountTextWatcherListener);
        etTotalAmount.setOnKeyListener(myetTotalAmountOnKeyListener);
        etOtherTip.addTextChangedListener(myetOtherTipTextWatcherListener);

        mBtnSave.setOnClickListener(myBtnSaveClickListener);
        mBtnClear.setOnClickListener(myBtnClearClickListener);
        // listener for the radio buttons
        rgTip.setOnCheckedChangeListener(this);

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

    private View.OnClickListener myBtnSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         //  Toast.makeText(getApplication()),"saving file",Toast.LENGTH_SHORT).show();
            Log.d(Constants.LOG,"saving file");
            TipCalculatorData.saveTipCalcData(getBaseContext(),tipCalcObject,"tipCal1.txt");

        }
    };

    private View.OnClickListener myBtnClearClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(Constants.LOG,"clearing data and UI");
           // tipCalcObject.clearData();
            clearEditText();

        }
    };

    // Listener for enter button on TOtalAmount
    private View.OnKeyListener myetTotalAmountOnKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() ==       KeyEvent.KEYCODE_ENTER)
            {
                Log.d(Constants.LOG, "captured");
                calculateBill();
                return false;
            }

            return false;
        }
    };

    private  TextWatcher myetNumPeopleSplitTextWatcherListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String inputString = s.toString();
            try {
                numPeopleToSplit = etNumPeopleToSplit.getText().toString();
            }catch (NullPointerException e){
                numPeopleToSplit = "1"; // set "1" as default so that app doesn't crash
              //  calculateBill();
                clearEditText();
            }

            try {
                // convert string to double - numPeopleTosplit
                //calling the string constructor on BigDecimal
               // numPeopleToSplitD = new BigDecimal(numPeopleToSplit); todo uncomment later aug 28
                tipCalcObject.setNumPeopleToSplitD(new BigDecimal(numPeopleToSplit));
                //numPeopleToSplitD = Double.parseDouble(numPeopleToSplit);
            }
            catch (NumberFormatException e)
            {
                //numPeopleToSplitD = BigDecimal.ONE; todo uncomment
                tipCalcObject.setNumPeopleToSplitD(BigDecimal.ONE);
                return;
            }
            calculateBill();

        }
    };

    private TextWatcher myetTotalAmountTextWatcherListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // String inputString = s.toString(); // this is just for validation check
            try {
                totalAmount = etTotalAmount.getText().toString();
            }catch (NullPointerException e) {
                totalAmount = "1"; // set $5 as the default
                clearEditText();
            }

            //DO you NEED THIS?????
            // if (totalAmount.length() != 0)
         /*   if ( totalAmount.isEmpty())           (totalAmount == null) || (totalAmount.equals("") ))
            {
               Toast.makeText(getBaseContext(),"enter the total amount greater than $5:00 ",
                        Toast.LENGTH_SHORT).show();
                clearEditText();

            }


            else
            { */
                //totalAmountD = Double.parseDouble(totalAmount);
                try
                {
                    //totalAmountD = new BigDecimal(totalAmount); TODO UNCOMMENT
                    tipCalcObject.setTotalAmountD(new BigDecimal(totalAmount));
                } catch (NumberFormatException e) {
                    //totalAmountD = BigDecimal.ONE;  TODO uncomment later
                    // set 1 as default to prevent null pointerexception
                    tipCalcObject.setTotalAmountD(BigDecimal.ONE);
                 //   return;
                }

                calculateBill(); //uncomment aug 15th, friday
           // }

        }
    };

    private TextWatcher myetOtherTipTextWatcherListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            tipValue = etOtherTip.getText().toString();
            if (tipValue.length() != 0) {
                try {
                    //tipValueD = Double.parseDouble(tipValue);
                   // tipValueD = new BigDecimal(tipValue); TODO Uncomment
                    tipCalcObject.setTipValueD(new BigDecimal(tipValue));
                }
                catch (NumberFormatException e)
                {
                    etOtherTip.setText("");
                }
                calculateBill();
            }
        }
    };





    // when the activity is paused or sent to background or UI completely hidden
    // onPause followed by on Stop.
    // if you want to save the data, best would be to save in onPause()
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplication(),"inside onPause",Toast.LENGTH_SHORT).show();
        Log.d(Constants.LOG,"on pause - saving file");
        TipCalculatorData.saveTipCalcData(getBaseContext(),tipCalcObject,"tipCal1.txt");


    }

    @Override
    protected void onResume() {
       super.onResume();

        Log.d(Constants.LOG,"resuming file");
        TipCalculatorData fromFileTipObject =
                TipCalculatorData.getTipCalcData(getBaseContext(),"tipCal1.txt");

        Log.d(Constants.LOG,fromFileTipObject.getBillAmountD().toString());
        Log.d(Constants.LOG,fromFileTipObject.getTotalAmountD().toString());
        tipCalcObject.setTipAmountD(fromFileTipObject.getNumPeopleToSplitD());
        tipCalcObject.setBillAmountD(fromFileTipObject.getBillAmountD());
        tipCalcObject.setNumPeopleToSplitD(fromFileTipObject.getNumPeopleToSplitD());
        tipCalcObject.setAmountPerPersonD(fromFileTipObject.getAmountPerPersonD());
        tipCalcObject.setTotalAmountD(fromFileTipObject.getTotalAmountD());

    }


    // 2 methods in FileUtil class readlines and write lines
    // these 2 methods read and write from arraylist to files.
    // get access to files first


 /*  private void readItems()
    {

        //create an absolute path
        File filesDir = getFilesDir();
        //create the file based on that path that gives access to handle
        File tipCalFile = new File(filesDir,"tipCal.txt");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(tipCalFile);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);


        // try ... catch it's possible that file do not exist
        // we don't the app to crash
        try {
            // load the items from the file
            //todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));


            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();
            System.out.println("Contents of file:");
            System.out.println(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

   private void writeItems()
    {
        File filesDir = getFilesDir();
        Toast.makeText(getApplicationContext(),"the filesDir = " + filesDir,Toast.LENGTH_LONG).show();
        //create the file based on that path that gives access to handle
        File tipCalFile = new File(filesDir,"tipCal.txt");
        // try ... catch it's possible that file do not exist
        // we don't the app to crash

            // load the items from the file
            //FileUtils.writeLines(todoFile, todoItems);

            String totalAmountString = totalAmountD.toPlainString();
            String tipValueString = tipValueD.toPlainString();

            /*
            String

            tipAmountD,billAmountD;
    private BigDecimal numPeopleToSplitD;;
    private BigDecimal amountPerPersonD,tipValueD; */

      /*      try {
                FileUtils.writeStringToFile(tipCalFile, tipValueString);
            } catch (IOException e) {
                Log.e(Constants.LOG, "Unable to write to the todo.txt file.");
            }

    } */


   /* create the absolute path using getFilesDir()
      create the new file to write to and
      write the items.
   */


  /*  private void readItems()
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
                enableOtherTipEditText(); // editTextListener calls calculateBill()
                break;

        }

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
          // tipAmountD = tipValueD; TODO UNCOMMENT
          //tipAmountD = tipValueD.setScale(2,RoundingMode.HALF_UP);
        tipCalcObject.setTipValueD(tipCalcObject.getTipValueD());


        Log.d(Constants.LOG," inside calculate bill");
        Log.d(Constants.LOG,"tipPercent = "+ tipPercent);
        Log.d(Constants.LOG,"tipValueD = "+ tipCalcObject.getTipValueD());

        // calculate tip if the user selects percentages.
        if (tipState != TipSelection.OTHER)
           // tipAmountD = totalAmountD.multiply(tipPercent); TODO uncomment
              tipCalcObject.setTipAmountD(tipCalcObject.getTotalAmountD().
                      multiply(tipPercent).setScale(2,RoundingMode.HALF_UP));

        // rounding up to 2 digits
        // tipAmountD.setScale(2, RoundingMode.HALF_UP); TODO uncomment
            //tipAmountD = totalAmountD * tipPercent;


        // calculate Bill Amount
        // tip amount+total amount = bill amount
        //billAmountD = tipAmountD + totalAmountD;
       // Log.d(Constants.LOG,"totalAmountD = " + totalAmountD);
        Log.d(Constants.LOG,"tipPercent = "+ tipPercent);
        //billAmountD = tipAmountD.add(totalAmountD); TODO uncomment
        tipCalcObject.setBillAmountD(tipCalcObject.getTipAmountD().add(tipCalcObject.getTotalAmountD()));




        // check division by zero
       // if ( (numPeopleToSplitD > 0) && (billAmountD > 0) )
     /*   if ( (numPeopleToSplitD.compareTo(BigDecimal.ZERO) > 0)  &&
                (billAmountD.compareTo(BigDecimal.ZERO) > 0) ) TODO uncomment */

        if ( (tipCalcObject.getNumPeopleToSplitD().compareTo(BigDecimal.ZERO) > 0)  &&
                (tipCalcObject.getBillAmountD().compareTo(BigDecimal.ZERO) > 0) )

        {
            try{
           // amountPerPersonD = billAmountD / numPeopleToSplitD;
           // ArithmeticException error: Non-terminating decimal expansion; no exact representation
           // when the recurring decimal happens, it would be
            // amountPerPersonD = billAmountD.divide(numPeopleToSplitD,2,RoundingMode.HALF_UP); TODO Uncomment
               tipCalcObject.setAmountPerPersonD(tipCalcObject.getBillAmountD().
                       divide(tipCalcObject.getNumPeopleToSplitD(), RoundingMode.HALF_UP));
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
            //amountPerPersonD = 0.0;
           /*
            billAmountD = BigDecimal.ZERO;
            tipAmountD = BigDecimal.ZERO;
            amountPerPersonD = BigDecimal.ZERO;  TODO */

            tipCalcObject.setBillAmountD(BigDecimal.ZERO);
            tipCalcObject.setTipAmountD(BigDecimal.ZERO);
            tipCalcObject.setAmountPerPersonD(BigDecimal.ZERO);




        }

        display();

    }

    private void display()
    {
       // set tip amount,bill amount, total per person
       // convert double values to string

      /*  tvTipAmount.setText(tipAmountD.toString());
        tvBillAmount.setText(billAmountD.toString()); TODO UNCOMMENT */

        tvTipAmount.setText(tipCalcObject.getTipAmountD().toString());
        tvBillAmount.setText(tipCalcObject.getBillAmountD().toString());

        // Format NOT DONE -------
        // NumberFormat formatter = NumberFormat.getCurrencyInstance();
      //  tvTotalPerPerson.setText(amountPerPersonD.toString()); TODO
        tvTotalPerPerson.setText(tipCalcObject.getAmountPerPersonD().toString());


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

