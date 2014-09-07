package com.tipcalculator.app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Anu on 8/28/14.
 */
public class TipCalculatorData implements Serializable {

    private BigDecimal tipAmountD;
    private BigDecimal billAmountD;
    private BigDecimal totalAmountD = BigDecimal.ONE;
    private BigDecimal numPeopleToSplitD;;
    private BigDecimal amountPerPersonD,tipValueD;

    public BigDecimal getTotalAmountD() {
        return totalAmountD;
    }

    public void setTotalAmountD(BigDecimal totalAmountD) {
        this.totalAmountD = totalAmountD;
    }



    public void setTipAmountD(BigDecimal tipAmountD) {
        this.tipAmountD = tipAmountD;
    }

    public void setBillAmountD(BigDecimal billAmountD) {
        this.billAmountD = billAmountD;
    }

    public void setNumPeopleToSplitD(BigDecimal numPeopleToSplitD) {
        this.numPeopleToSplitD = numPeopleToSplitD;
    }

    public void setAmountPerPersonD(BigDecimal amountPerPersonD) {
        this.amountPerPersonD = amountPerPersonD;
    }

    public void setTipValueD(BigDecimal tipValueD) {
        this.tipValueD = tipValueD;
    }

    public BigDecimal getTipAmountD() {
        return tipAmountD;
    }

    public BigDecimal getBillAmountD() {
        return billAmountD;
    }

    public BigDecimal getNumPeopleToSplitD() {
        return numPeopleToSplitD;
    }

    public BigDecimal getAmountPerPersonD() {
        return amountPerPersonD;
    }

    public BigDecimal getTipValueD() {
        return tipValueD;
    }


    public static boolean saveTipCalcData(Context context, TipCalculatorData data,String fileName) {
        try {
            Log.d("TipCalModel","inside saveTip- static fun");
            Toast.makeText(context,"inside save ",Toast.LENGTH_SHORT).show();
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static TipCalculatorData getTipCalcData(Context context,String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object readObject = is.readObject();
            is.close();

            if(readObject != null && readObject instanceof TipCalculatorData) {
                Toast.makeText(context,"inside get tip", Toast.LENGTH_SHORT).show();

                Toast.makeText(context, ((TipCalculatorData) readObject).getAmountPerPersonD().toString(),
                        Toast.LENGTH_SHORT).show();
                return (TipCalculatorData) readObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void clearData()
    {
       tipAmountD = BigDecimal.ZERO;
       totalAmountD = BigDecimal.ZERO;
       billAmountD = BigDecimal.ZERO;
       amountPerPersonD = BigDecimal.ZERO;
       numPeopleToSplitD = BigDecimal.ZERO;
       tipValueD = BigDecimal.ZERO;
    }


}
