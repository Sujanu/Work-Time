package np.com.softwel.timetacker.model

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int = 300
)

