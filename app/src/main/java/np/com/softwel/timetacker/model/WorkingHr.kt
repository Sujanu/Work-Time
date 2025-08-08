package np.com.softwel.timetacker.model

data class WorkingHr(
    val id : Int,
    val workHour : String,
    val day : String,
    val date : String,
    val clockIn : String,
    val clockOut : String
)
