package net.muellersites.affordable.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.muellersites.affordable.Objects.Balance;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Affordable";
    private static final String TABLE_NAME = "Balances";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CURRENT_VALUE = "current_value";

    private static final int DATABASE_VERSION = 2;
    private static final String BALANCE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER, " +
                    COLUMN_CURRENT_VALUE + " INTEGER);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BALANCE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DELETE FROM User");
        db.close();
    }

    public void insertBalance(Balance balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", balance.getId());
        values.put("current_value", balance.getCurrent_value());
        db.insert("Balances", null, values);
        db.close();
    }

    public void updateBalance(Balance balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", balance.getId());
        values.put("current_value", balance.getCurrent_value());
        db.update("Balances", values, "id=" + balance.getId(), null);
        db.close();
    }

    public Balance getBalance() throws Exception{
        Balance balance = new Balance();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Balances";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null && cursor.moveToFirst()){
            balance.setId(cursor.getInt(0));
            balance.setCurrent_value(cursor.getInt(1));
            cursor.close();
        }else {
            throw new Exception("Table is empty");
        }
        db.close();
        return balance;
    }

    public void clearBalances(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Balances");
        db.close();
    }

}
