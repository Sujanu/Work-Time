package np.com.softwel.timetacker.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import np.com.softwel.timetacker.model.WorkingHr

class WorkingHour(context: Context) : SQLiteOpenHelper(context, DATABASENAME, null, 1) {

    companion object {
        const val DATABASENAME = "workStation.db"
        const val WORKING = "working"
        const val MONTHLY = "monthly"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(
            "CREATE TABLE $WORKING(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "day  TEXT NOT NULL," +
                    "date TEXT NOT NULL,"+
                    "workHour TEXT NOT NULL,"+
                    "clockIn TEXT NOT NULL,"+
                    "clockOut TEXT NOT NULL)"
        )

        db?.execSQL(
            "CREATE TABLE $MONTHLY(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "month TEXT NOT NULL,"+
                    "date TEXT NOT NULL)"

//                    "leave TEXT NOT NULL," +
//                    "workingDays TEXT NOT NULL," +
//
//
//                    "businessDays TEXT NOT NULL," +
//                    "hours TEXT NOT NULL," +
//                    "overtime TEXT NOT NULL," +
//                    "deficientHours TEXT NOT NULL," +
//                    "min TEXT NOT NULL," +
//
        )

    }

    fun insertDataInTable(cv: ContentValues?, table_name: String): Boolean {
        val db = this.writableDatabase
        val value = db.insert(table_name, null, cv)
        db.close()
        return value > 0
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}



    fun getAll(): List<WorkingHr> {
        val wHourList = mutableListOf<WorkingHr>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $WORKING", null)

        if (cursor.moveToFirst()) {
            do {

                val station = WorkingHr(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    workHour = cursor.getString(cursor.getColumnIndexOrThrow("workHour")),
                    day = cursor.getString(cursor.getColumnIndexOrThrow("day")),
                    date = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                    clockIn = cursor.getString(cursor.getColumnIndexOrThrow("clockIn")),
                    clockOut = cursor.getString(cursor.getColumnIndexOrThrow("clockOut"))
                )
                wHourList.add(station)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return wHourList
    }




}