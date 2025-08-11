package np.com.softwel.timetacker.model

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)
