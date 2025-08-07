package np.com.softwel.timetacker.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import np.com.softwel.timetacker.model.WorkingHr

class dbHelper(context: Context) : SQLiteOpenHelper(context, DATABASENAME, null, 1) {
    companion object {
        const val DATABASENAME = "chargingStation.db"
        const val WORKING = "working"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(
            "CREATE TABLE $WORKING(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "month  TEXT NOT NULL," +
                    "day  TEXT NOT NULL," +
                    "date TEXT NOT NULL,"+
                    "clockIn TEXT NOT NULL,"+
                    "clockOut TEXT NOT NULL,"+
                    "hour TEXT NOT NULL," +
                    "min TEXT NOT NULL)"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insertWorkingHour(db: SQLiteDatabase, workingHr: WorkingHr): Long {
        val values = ContentValues().apply {
            put("id", workingHr.id)
            put("month", workingHr.month)
            put("day", workingHr.day)
            put("date", workingHr.date)
            put("clockIn", workingHr.clockIn)
            put("clockOut", workingHr.clockOut)
            put("hour", workingHr.hour)
            put("min", workingHr.min)

        }
        return db.insert("WorkingHr", null, values)
    }



}