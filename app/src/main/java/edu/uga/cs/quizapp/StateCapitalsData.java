package edu.uga.cs.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StateCapitalsData {

    private SQLiteOpenHelper dbHelper;

    public static final String DEBUG_TAG = "StateCapitalsData";

    private SQLiteDatabase db;
    public static final String STATE_CAPITAL_ID = "id";
    public static final String STATE_CAPITAL_STATE = "State";
    public static final String STATE_CAPITAL_CAPITAL_CITY = "Capitalcity";
    public static final String STATE_CAPITAL_SECOND_CITY = "Secondcity";
    public static final String STATE_CAPITAL_THIRD_CITY = "Thirdcity";
    public static final String STATE_CAPITAL_STATEHOOD = "Statehood";
    public static final String STATE_CAPITAL_CAPITAL_SINCE = "Capitalsince";
    public static final String STATE_CAPITAL_SIZE_RANK = "Sizerank";


    public static final String[] allColumns = {
            STATE_CAPITAL_ID, STATE_CAPITAL_STATE, STATE_CAPITAL_CAPITAL_CITY, STATE_CAPITAL_SECOND_CITY, STATE_CAPITAL_THIRD_CITY,
            STATE_CAPITAL_STATEHOOD, STATE_CAPITAL_CAPITAL_SINCE, STATE_CAPITAL_SIZE_RANK
    };
    public static final String[] allQuizHistoryColumns = {
            StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_ID, StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_RESULT,
            StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_DATE
    };

    /**
     * Method to get the instance of the database from the DBHelper class
     *
     * @param context
     */
    public StateCapitalsData(Context context) {
        //dataBase = new StateCapitalsDBHelper(context, "stateCaptialsQuiz.db", null, 1);
        this.dbHelper = StateCapitalsDBHelper.getInstance(context);
    }

    /**
     * Open the database connection to get the writable database
     */
    public void open() {
        db = dbHelper.getWritableDatabase();
        Log.d(DEBUG_TAG, "StateCapitalsData: db open");
    }

    /**
     * Close the database
     */
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
            Log.d(DEBUG_TAG, "StateCapitalsData: db closed");
        }
    }

    public boolean isDBOpen() {
        return db.isOpen();
    }

    /**
     * Method to get the list of all state's data from the questions table
     *
     * @return List of State capitals
     */
    public List<StateCapitalPOJO> retrieveAllRecords() {
        Log.d(DEBUG_TAG, "In retrieveAllRecords method");
        ArrayList<StateCapitalPOJO> capitalsList = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;
        try {

            cursor = db.query("StateCapitals", allColumns, null,
                    null, null, null, STATE_CAPITAL_ID, null);
            if (cursor != null && cursor.getCount() >= 1) {
                while (cursor.moveToNext()) {
                    if (cursor.getColumnCount() >= 8) {
                        columnIndex = cursor.getColumnIndex(STATE_CAPITAL_ID);
                        long id = cursor.getLong(columnIndex);

                        columnIndex = cursor.getColumnIndex(STATE_CAPITAL_STATE);
                        String state = cursor.getString(columnIndex);

                        columnIndex = cursor.getColumnIndex(STATE_CAPITAL_CAPITAL_CITY);
                        String capitalCity = cursor.getString(columnIndex);

                        columnIndex = cursor.getColumnIndex(STATE_CAPITAL_SECOND_CITY);
                        String secondCity = cursor.getString(columnIndex);

                        columnIndex = cursor.getColumnIndex(STATE_CAPITAL_THIRD_CITY);
                        String thirdCity = cursor.getString(columnIndex);

                        columnIndex = cursor.getColumnIndex(STATE_CAPITAL_STATEHOOD);
                        long stateHood = cursor.getLong(columnIndex);

                        columnIndex = cursor.getColumnIndex(STATE_CAPITAL_CAPITAL_SINCE);
                        long capitalSince = cursor.getLong(columnIndex);

                        columnIndex = cursor.getColumnIndex(STATE_CAPITAL_SIZE_RANK);
                        long sizeRank = cursor.getLong(columnIndex);

                        StateCapitalPOJO stateCapital = new StateCapitalPOJO(id, state, capitalCity, secondCity,
                                thirdCity, stateHood, capitalSince, sizeRank);

                        stateCapital.setId(id);
                        capitalsList.add(stateCapital);
                    }
                }
            }
            capitalsList.forEach(capital -> {
                Log.i("CAPITAL LIST -----", String.valueOf(capital));
            });
            if (cursor != null)
                Log.d(DEBUG_TAG, "Number of records from DB: " + cursor.getCount());
            else
                Log.d(DEBUG_TAG, "Number of records from DB: 0");

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);

        } finally {
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        return capitalsList;
    }

    /**
     * This method gets a list of questions to provide for quiz.
     *
     * @return A list of questions for quiz.
     */
    public List<StateCapitalPOJO> createQuiz() {
        List<StateCapitalPOJO> capitalsList = retrieveAllRecords();
        List<StateCapitalPOJO> quizQuestions;
        quizQuestions = getRandomRecords(capitalsList, 6);
        return quizQuestions;
    }

    /**
     * Method to get 6 random records without duplicates from all questions available in database.
     *
     * @param list
     * @param numberOfRecordsToGet
     * @return list of random questions.
     */
    private List<StateCapitalPOJO> getRandomRecords(List<StateCapitalPOJO> list, int numberOfRecordsToGet) {
        List<StateCapitalPOJO> randomRecords = new ArrayList<>();
        Random random = new Random();

        // Ensure the number of records to get is not more than the size of the list
        numberOfRecordsToGet = Math.min(numberOfRecordsToGet, list.size());

        // Generate unique random indices to get random records
        List<Integer> indices = new ArrayList<>();
        while (indices.size() < numberOfRecordsToGet) {
            int randomIndex = random.nextInt(list.size());
            if (!indices.contains(randomIndex)) {
                indices.add(randomIndex);
                randomRecords.add(list.get(randomIndex));
            }
        }
        return randomRecords;
    }

    /**
     * Stores a new quiz record in the database.
     *
     * @param quizResult The QuizResultPOJO object containing quiz result details.
     * @return The ID (primary key) of the inserted record in the database.
     */
    public int storeQuizRecord(QuizResultPOJO quizResult) {

        // Prepare the values for all of the necessary columns in the table
        ContentValues values = new ContentValues();
        values.put(StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_RESULT, quizResult.getResult());
        values.put(StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_DATE, quizResult.getDate());

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        int id = (int) db.insert(StateCapitalsDBHelper.QUIZ_HISTORY_TABLE, null, values);

        // store the id (the primary key) in the QuizResultPOJO instance, as it is now persistent
        quizResult.setId(id);

        Log.d(DEBUG_TAG, "Stored new quiz with id: " + String.valueOf(quizResult.getId()));
        db.close();
        return id;

    }


    /**
     * Updates the quiz result in the database for a specific quiz ID.
     *
     * @param quizId     The ID of the quiz record to be updated.
     * @param quizResult The updated QuizResultPOJO object containing the new quiz result details.
     * @return The number of rows affected by the update operation in the database.
     */
    public int updateQuizResult(int quizId, QuizResultPOJO quizResult) {
        // Prepare the values to be updated in the database
        ContentValues values = new ContentValues();
        values.put(StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_RESULT, quizResult.getResult());
        // Define the WHERE clause to identify the record to be updated
        String whereClause = StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(quizId)};
        // Perform the update operation in the database
        int rowsAffected = db.update(StateCapitalsDBHelper.QUIZ_HISTORY_TABLE, values, whereClause, whereArgs);
        // Check if the update was successful
        if (rowsAffected > 0) {
            Log.d(DEBUG_TAG, "Quiz record with ID " + quizId + " updated successfully.");
        } else {
            Log.d(DEBUG_TAG, "No records updated for ID " + quizId);
        }
        return rowsAffected;
    }

    /**
     * Retrieves all quiz records from the database.
     *
     * @return A List containing QuizResultPOJO objects representing all quiz records.
     */
    public List<QuizResultPOJO> retrieveAllQuizzes() {
        Log.d("In StateCapitalPOJO", "In retrieve all quizzes");
        ArrayList<QuizResultPOJO> quizList = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;
        try {

            cursor = db.query(StateCapitalsDBHelper.QUIZ_HISTORY_TABLE, allQuizHistoryColumns, null,
                    null, null, null, StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_DATE + " DESC", null);
            if (cursor != null && cursor.getCount() >= 1) {
                // Iterate through the cursor to extract quiz records
                while (cursor.moveToNext()) {
                    if (cursor.getColumnCount() >= 3) {
                        columnIndex = cursor.getColumnIndex(StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_ID);
                        int id = cursor.getInt(columnIndex);

                        columnIndex = cursor.getColumnIndex(StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_RESULT);
                        long result = cursor.getLong(columnIndex);

                        columnIndex = cursor.getColumnIndex(StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_DATE);
                        String date = cursor.getString(columnIndex);

                        QuizResultPOJO quizResult = new QuizResultPOJO(result, date);

                        quizResult.setId(id);
                        quizList.add(quizResult);
                    }
                }
            }
            quizList.forEach(result -> {
                Log.i("QUIZ LIST -----", String.valueOf(result));
            });
            if (cursor != null)
                Log.d(DEBUG_TAG, "Number of records from DB: " + cursor.getCount());
            else
                Log.d(DEBUG_TAG, "Number of records from DB: 0");

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);

        } finally {
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        return quizList;
    }

    /**
     * Inserts quiz questions into the database for a specific quiz ID.
     *
     * @param quizId        The ID of the quiz to which the questions belong.
     * @param questionsList A List containing StateCapitalPOJO objects representing quiz questions.
     * @return The number of successfully inserted quiz questions.
     */
    public int insertQuizQuestions(int quizId, List<StateCapitalPOJO> questionsList) {
        List<ContentValues> list = new ArrayList<>();
        Log.d(DEBUG_TAG, "Called insertQuizQuestions method");

        // Iterate through the questionsList to create ContentValues for each question
        questionsList.forEach(question -> {
            ContentValues values = new ContentValues();
            values.put(StateCapitalsDBHelper.QUIZ_QUESTIONS_COLUMN_QUIZ_ID, quizId);
            values.put(StateCapitalsDBHelper.QUIZ_QUESTIONS_COLUMN_QUESTION_ID, question.getId());
            list.add(values);
        });

        // Iterate through the ContentValues list to insert quiz questions into the database
        int successfulInserts = 0;
        for (ContentValues value : list) {
            long id = db.insert(StateCapitalsDBHelper.QUIZ_QUESTIONS_TABLE, null, value);
            if (id == -1) {
                // Handle insertion failure if needed
            } else {
                Log.d(DEBUG_TAG, "Inserted quiz question ID: " + id);
                successfulInserts++;
            }
        }

        // Return the number of successfully inserted quiz questions
        return successfulInserts;
    }


    public int deleteAllQuestionsRecords() {
        Log.d(DEBUG_TAG, "Table " + StateCapitalsDBHelper.QUIZ_QUESTIONS_TABLE + " rows deleted");
        int id = db.delete(StateCapitalsDBHelper.QUIZ_QUESTIONS_TABLE, null, null);
        return id;
    }

    public int deleteAllQuizRecords() {
        Log.d(DEBUG_TAG, "Table " + StateCapitalsDBHelper.QUIZ_HISTORY_TABLE + " rows deleted");
        int id = db.delete(StateCapitalsDBHelper.QUIZ_HISTORY_TABLE, null, null);
        return id;
    }

    /**
     * Deletes an incomplete quiz record from the database based on the provided quiz ID.
     *
     * @param quizId The ID of the quiz record to be deleted.
     * @return The number of rows affected by the deletion (should be 0 or 1).
     */
    public int deleteIncompleteQuizRecord(int quizId) {
        Log.d(DEBUG_TAG, "Table " + StateCapitalsDBHelper.QUIZ_HISTORY_TABLE + " rows deleted");
        // Specify the WHERE clause and arguments to identify the quiz record to be deleted
        String whereClause = StateCapitalsDBHelper.QUIZ_HISTORY_COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(quizId)};
        // Perform the deletion operation
        int id = db.delete(StateCapitalsDBHelper.QUIZ_HISTORY_TABLE, whereClause, whereArgs);
        return id;
    }
}

