package edu.uga.cs.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StateCapitalsDBHelper extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "StateCapitalsDBHelper";
    private static StateCapitalsDBHelper dbHelper;
    private final Context context;

    public static final String QUIZ_HISTORY_TABLE = "QuizHistory";
    public static final String QUIZ_HISTORY_COLUMN_ID = "id";
    public static final String QUIZ_HISTORY_COLUMN_RESULT = "result";
    public static final String QUIZ_HISTORY_COLUMN_DATE = "date";
    public static final String QUIZ_QUESTIONS_TABLE = "QuizQuestions";
    public static final String QUIZ_QUESTIONS_COLUMN_ID = "id";
    public static final String QUIZ_QUESTIONS_COLUMN_QUIZ_ID = "QuizId";
    public static final String QUIZ_QUESTIONS_COLUMN_QUESTION_ID = "QuestionId";

    public static final String QUESTIONS_TABLE = "StateCapitals";
    public static final String QUESTIONS_COLUMN_ID = "id";
    public static final String QUESTIONS_COLUMN_STATE = "State";
    public static final String QUESTIONS_COLUMN_CAPITAL_CITY = "Capitalcity";
    public static final String QUESTIONS_COLUMN_SECOND_CITY = "Secondcity";
    public static final String QUESTIONS_COLUMN_THIRD_CITY = "Thirdcity";
    public static final String QUESTIONS_COLUMN_STATEHOOD = "Statehood";
    public static final String QUESTIONS_COLUMN_CAPITALSINCE = "Capitalsince";
    public static final String QUESTIONS_COLUMN_SIZERANK = "Sizerank";

//Create statement for quiz history table
    private static final String CREATE_QUIZ_HISTORY =
            "create table " + QUIZ_HISTORY_TABLE + " ("
                    + QUIZ_HISTORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZ_HISTORY_COLUMN_RESULT + " TEXT, "
                    + QUIZ_HISTORY_COLUMN_DATE + " TEXT "
                    + ")";

    //Create statement for quiz questions table
    private static final String CREATE_QUIZ_QUESTIONS_TABLE =
            "create table " + QUIZ_QUESTIONS_TABLE + " ("
                    + QUIZ_QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZ_QUESTIONS_COLUMN_QUIZ_ID + " INTEGER, "
                    + QUIZ_QUESTIONS_COLUMN_QUESTION_ID + " INTEGER "
                    + ")";

    //Create statement for questions table
    private static final String CREATE_QUESTIONS_TABLE =
            "create table " + QUESTIONS_TABLE + " ("
                    + QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUESTIONS_COLUMN_STATE + " TEXT, "
                    + QUESTIONS_COLUMN_CAPITAL_CITY + " TEXT ,"
                    + QUESTIONS_COLUMN_SECOND_CITY + " TEXT ,"
                    + QUESTIONS_COLUMN_THIRD_CITY + " TEXT ,"
                    + QUESTIONS_COLUMN_CAPITALSINCE + " INTEGER ,"
                    + QUESTIONS_COLUMN_STATEHOOD + " INTEGER ,"
                    + QUESTIONS_COLUMN_SIZERANK + " INTEGER "
                    + ")";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use for locating paths to the the database
     */
    private StateCapitalsDBHelper(@Nullable Context context) {
        super(context, "stateCapitals.db", null, 1);
        this.context = context;
    }

    public static synchronized StateCapitalsDBHelper getInstance(Context context) {
        // check if the instance already exists and if not, create the instance
        if (dbHelper == null) {
            dbHelper = new StateCapitalsDBHelper(context.getApplicationContext());
        }
        return dbHelper;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUIZ_QUESTIONS_TABLE);
        db.execSQL(CREATE_QUIZ_HISTORY);
        db.execSQL(CREATE_QUESTIONS_TABLE);
        insertDataFromCSV( db, context);
        Log.d(DEBUG_TAG, "Table " + QUIZ_HISTORY_TABLE + " created");
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     * <p>
     * <em>Important:</em> You should NOT modify an existing migration step from version X to X+1
     * once a build has been released containing that migration step.  If a migration step has an
     * error and it runs on a device, the step will NOT re-run itself in the future if a fix is made
     * to the migration step.</p>
     * <p>For example, suppose a migration step renames a database column from {@code foo} to
     * {@code bar} when the name should have been {@code baz}.  If that migration step is released
     * in a build and runs on a user's device, the column will be renamed to {@code bar}.  If the
     * developer subsequently edits this same migration step to change the name to {@code baz} as
     * intended, the user devices which have already run this step will still have the name
     * {@code bar}.  Instead, a NEW migration step should be created to correct the error and rename
     * {@code bar} to {@code baz}, ensuring the error is corrected on devices which have already run
     * the migration step with the error.</p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + QUIZ_HISTORY_TABLE);
        onCreate(db);
        Log.d(DEBUG_TAG, "Table " + QUIZ_HISTORY_TABLE + " upgraded");

    }

    //Insert the data from the CSV file. Called only once due to single instance of the DBHelper class.
    public void insertDataFromCSV(SQLiteDatabase db, Context context) {
        Log.d(DEBUG_TAG, "Inside the insertDataFromCSV method");
        BufferedReader reader = null;
        try {
            //Reading contents from the csv file which is in assets folder
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("StateCapitals.csv")));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                //Assigning the data to the content values to insert.
                ContentValues values = new ContentValues();
                values.put("State", data[0]);
                values.put("Capitalcity", data[1]);
                values.put("Secondcity", data[2]);
                values.put("Thirdcity", data[3]);
                values.put("Statehood", data[4]);
                values.put("Capitalsince", (data[5]));
                values.put("Sizerank", (data[6]));

                //Inserting the data into the StateCapitals table
                db.insert("StateCapitals", null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
