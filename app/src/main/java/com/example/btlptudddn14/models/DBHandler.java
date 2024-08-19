package com.example.btlptudddn14.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "fima.db";
    private static final String USERS = "users";
    private static final String USER_EXPENSES = "user_expenses";
    private static final String TARGET = "target";
    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String TYPE = "type";
    private static final String DESCRIPTION = "description";
    private static final String DATE = "date";
    private static final String AMOUNT = "amount";
    private static final String TITLE = "title";
    private static final String TOTAL_BUDGET = "totalBudget";
    private static final String SAVED_BUDGET = "saved_budget";
    private static final String PRIORITY_LEVEL = "priority_level";
    private static final String TARGET_TYPE = "target_type";
    private static final String IMG_SRC = "img_src";
    public static final String USERNAME = "username";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private static DBHandler instance;

    public static synchronized DBHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DBHandler(context.getApplicationContext());
        }
        return instance;
    }

    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //tao bang
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsersQuery = "CREATE TABLE " + USERS + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIRSTNAME + " TEXT, " + LASTNAME + " TEXT, " +
                EMAIL + " TEXT, " +
                PASSWORD + " TEXT);";
        db.execSQL(createTableUsersQuery);
        //tạo bảng Userexpnse
        String createTableUsersExpensesQuery = "CREATE TABLE " + USER_EXPENSES + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID + " INTEGER, " +
                TYPE + " INTEGER, " +
                DESCRIPTION + " TEXT, " +
                DATE + " TEXT, " +
                AMOUNT + " REAL);";
        db.execSQL(createTableUsersExpensesQuery);
        // sửa bảng này
        String createTableUsersTodoQuery = "CREATE TABLE " + TARGET + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID + " INTEGER, " +
                // name
                TITLE + " TEXT, " +
                TOTAL_BUDGET + " REAL, " +
                SAVED_BUDGET + " REAL, " +
                DATE + " DATE, " +
                TARGET_TYPE + " TEXT, " +
                PRIORITY_LEVEL + " INTEGER, " +
                IMG_SRC + " TEXT);";
        db.execSQL(createTableUsersTodoQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        db.execSQL("DROP TABLE IF EXISTS " + USER_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TARGET);
        onCreate(db);
    }
    // Kiem tra user
    public boolean checkUserIsExit(String email)
    {
        String sql = "SELECT * FROM " + USERS +
                " WHERE " + EMAIL + " = " + "'" + email + "'";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, null);
        if (cursor.getCount() != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    // Them mot user moi
    public boolean addUser(String firstname, String lastname, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues us = new ContentValues();
        us.put(FIRSTNAME, firstname);
        us.put(LASTNAME, lastname);
        us.put(EMAIL, email);
        us.put(PASSWORD, password);
        long rowID = db.insert(USERS, null, us);
        db.close();
        if (rowID == -1)
        {
            return  false;
        }
        return true;
    }
    // Update thông tin ca nhan
    public boolean updateProfile (String firstname, String lastname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues us = new ContentValues();
        us.put(FIRSTNAME, firstname);
        us.put(LASTNAME, lastname);
        String whereClause =  ID + " = ?";
        String[] whereArgs = {String.valueOf(User.getInstance().getId())};
        int row = db.update(USERS, us, whereClause, whereArgs);
        db.close();
        if (row > 0)
        {
            return true;
        }
        return false;
    }
    // Update mat khau
    public boolean updatePassword(String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues us = new ContentValues();
        us.put(PASSWORD, password);
        String whereClause =  ID + " = ?";
        String[] whereArgs = {String.valueOf(User.getInstance().getId())};
        int row = db.update(USERS, us, whereClause, whereArgs);
        db.close();
        if (row > 0)
        {
            return true;
        }
        return false;
    }
    public boolean updateForgetPassword(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues us = new ContentValues();
        us.put(PASSWORD, password);
        String whereClause =  EMAIL + " = ?";
        String[] whereArgs = {email};
        int row = db.update(USERS, us, whereClause, whereArgs);
        db.close();
        if (row > 0)
        {
            return true;
        }
        return false;
    }
    // Hàm kiểm tra đăng nhập
    public Map<String, String> checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD};
        String selection = EMAIL + " = ? AND " + PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(USERS, columns, selection, selectionArgs, null, null, null);

        Map<String, String> userData = null;

        if (cursor.moveToFirst()) {
            userData = new HashMap<>();
            userData.put("id", String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ID))));
            userData.put("firstname", cursor.getString(cursor.getColumnIndexOrThrow(FIRSTNAME)));
            userData.put("lastname", cursor.getString(cursor.getColumnIndexOrThrow(LASTNAME)));
            userData.put("email", cursor.getString(cursor.getColumnIndexOrThrow(EMAIL)));
            userData.put("password", cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD)));
        }

        cursor.close();
        db.close();

        return userData;
    }
    public boolean checkPassword(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Lấy thông tin người dùng hiện tại từ lớp User
        User currentUser = User.getInstance();

        String[] columns = {ID};
        String selection = ID + " = ? AND " + PASSWORD + " = ?";
        String[] selectionArgs = {String.valueOf(currentUser.getId()), password};

        Cursor cursor = db.query(USERS, columns, selection, selectionArgs, null, null, null);

        boolean isPasswordCorrect = cursor.getCount() != 0;

        cursor.close();
        db.close();

        return isPasswordCorrect;
    }

    public boolean deleteUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(User.getInstance().getId())};
        int row = db.delete(USERS, whereClause, whereArgs);
        if (row > 0)
        {
            return true;
        }
        return false;
    }
    // Check infor of account to change password
    public boolean checkInforForgetPass(String firstname, String lastname, String email)
    {
        String sql = "SELECT * FROM " + USERS +
                " WHERE " + FIRSTNAME + " = " + "'" + firstname + "'" +
                " AND " + LASTNAME + " = " + "'" + lastname + "'" +
                " AND " + EMAIL + " = " + "'" + email + "'";
        Cursor cursor = this.getReadableDatabase().rawQuery(sql, null);
        if (cursor.getCount() != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //them mot expense moi
    public void addExpense(UserExpense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, expense.getUserId());
        cv.put(TYPE, expense.getType());
        cv.put(AMOUNT, expense.getAmount());
        cv.put(DATE, expense.getDate());
        cv.put(DESCRIPTION, expense.getDescription());
        db.insert(USER_EXPENSES, null, cv);
        db.close();
    }
    //cap nhap expnse
    public void updateExpense(UserExpense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TYPE, expense.getType());
        cv.put(AMOUNT, expense.getAmount());
        cv.put(DATE, expense.getDate());
        cv.put(DESCRIPTION, expense.getDescription());
        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(expense.getId())};
        db.update(USER_EXPENSES, cv, whereClause, whereArgs);
    }
    //xoa expnse
    public void deleteExpense(UserExpense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(expense.getId())};
        db.delete(USER_EXPENSES, whereClause, whereArgs);
    }

    public ArrayList<UserExpense> fetchExpensesByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor
                = db.rawQuery("SELECT * FROM " + USER_EXPENSES + " WHERE "+ USER_ID + " = " + "'" + User.getInstance().getId()+"'" + " AND " + DATE + " = " + "'"+date+"'", null);

        ArrayList<UserExpense> expenses
                = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                expenses.add(new UserExpense(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DATE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(AMOUNT))));
            } while (cursor.moveToNext());
        }
        Collections.reverse(expenses);
        cursor.close();
        return expenses;
    }

    // câu lệnh them bang TARGET
    public void addTarget(Target target) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLE, target.getPlanName());
        cv.put(USER_ID, User.getInstance().getId());
        cv.put(TOTAL_BUDGET, target.getTotalBudget());
        cv.put(SAVED_BUDGET, target.getSavedBudget());
        cv.put(DATE, target.getDeadline());
        cv.put(TARGET_TYPE, target.getTargetType());
        cv.put(PRIORITY_LEVEL, target.getPriorityLevel());
        cv.put(IMG_SRC, target.getImgSrc());
        db.insert(TARGET, null, cv);
        db.close();
    }

    public void updateTarget(Target target) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLE, target.getPlanName());
        cv.put(TOTAL_BUDGET, target.getTotalBudget());
        cv.put(SAVED_BUDGET, target.getSavedBudget());
        cv.put(DATE, target.getDeadline());
        cv.put(PRIORITY_LEVEL, target.getPriorityLevel());
        cv.put(TARGET_TYPE, target.getTargetType());
        cv.put(IMG_SRC, target.getImgSrc());
        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(target.getID())};
        db.update(TARGET, cv, whereClause, whereArgs);

    }

    public void deleteTarget(Target target) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID + " = ?";
        String[] whereArgs = {String.valueOf(target.getID())};
        db.delete(TARGET, whereClause, whereArgs);
    }

    public ArrayList<Target> fetchTargetByType(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor
                = db.rawQuery("SELECT * FROM " + TARGET + " WHERE " + USER_ID + " = " + "'" + User.getInstance().getId()+"'" + " AND " + TARGET_TYPE + " = " + "'" + type + "'", null);

        ArrayList<Target> targets
                = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                targets.add(new Target(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(TOTAL_BUDGET)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(SAVED_BUDGET)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TARGET_TYPE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PRIORITY_LEVEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(IMG_SRC))
                ));

            } while (cursor.moveToNext());
        }
        Collections.reverse(targets);
        cursor.close();
        return targets;
    }

    // select toan bo du lieu
    public ArrayList<Target> fetchAllTarget() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor
                = db.rawQuery("SELECT * FROM " + TARGET + " WHERE " + USER_ID + " = " + "'" + User.getInstance().getId()+"'", null);

        ArrayList<Target> targets
                = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                targets.add(new Target(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(TOTAL_BUDGET)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(SAVED_BUDGET)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TARGET_TYPE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PRIORITY_LEVEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(IMG_SRC))
                ));
            } while (cursor.moveToNext());
        }
        Collections.reverse(targets);
        cursor.close();
        return targets;
    }

    private static String getNextDay(String dateString) {
        try {
            // Định dạng ngày
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf.parse(dateString);

            // Lấy ngày tiếp theo
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);

            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<UserExpense> fetchExpensesBetweenDates(String startDate, String endDate){


        ArrayList<UserExpense> expenses
                = new ArrayList<>();
        String date = startDate;
        while (!date.equals(getNextDay(endDate))){
            expenses.addAll(fetchExpensesByDate(date));
            date = getNextDay(date);
        }

        Collections.sort(expenses, new Comparator<UserExpense>() {
            @Override
            public int compare(UserExpense e1, UserExpense e2) {
                return Integer.compare(e1.getType(), e2.getType());
            }
        });

        for (UserExpense expense : expenses) {
            expense.setDescription("");
        }
        int i = 0;
        while (i < expenses.size()-1){
            if(expenses.get(i).getType() == expenses.get(i+1).getType()) {
                expenses.get(i).setAmount(expenses.get(i).getAmount()+expenses.get(i+1).getAmount());

                expenses.remove(i+1);
            }
            else i++;
        }

        return expenses;
    }
}