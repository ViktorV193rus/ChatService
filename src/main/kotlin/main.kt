data class Message(
    val id: Int,
    val ownerId: Int,
    val text: String,
    val isRead: Boolean = false,
    val isDelete: Boolean = false

)

data class Chat(
    val id: Int,
    val users: List<Int>,
    val messages: List<Message>
)

object ChatService {
    private var chats = mutableListOf<Chat>()
    private var idCha = 1
    private var idForMessage = 1

    fun clear() {
        chats.clear()
        idCha = 1
        idForMessage = 1

    }

    fun createMessage(
        senderId: Int, // отправитель
        recipientId: Int, //получатель
        text: String
    ): Boolean {
        val message = Message(
            id = idForMessage++,
            ownerId = senderId,
            text = text
        )
        val newChat = chats.firstOrNull { chat ->
            chat.users
                .containsAll(listOf(recipientId, senderId))
        }
            ?.let { chat ->
                chat.copy(messages = chat.messages + message)
            } ?: Chat(
            id = idCha++,
            users = listOf(senderId, recipientId),
            messages = listOf(message)
        )

        chats.removeIf { newChat.id == it.id }
        chats.add(newChat)
        return true
    }

    fun getChats(userId: Int): List<Chat> {
        val gettingChats = chats.filter {
            it.users.contains(userId)
        }
        return gettingChats

    }
    fun getUnreadChatsCount(userId: Int): Int { // получаем непрочитаные чаты
        var count = 0
        chats.forEach { chat ->
            if (chat.users.contains(userId)) {
                chat.messages.forEachIndexed { _, message ->
                    if (!message.isRead) {
                        count++
                        return@forEachIndexed
                    }
                }
            }
        }

        return count
    }
    private fun deleteChat(idChat: Int): Boolean {
        chats.removeIf { idChat == it.id }
        return true
    }

    fun getMessages(idChat: Int, idLastMessageFrom: Int, countMessage: Int): List<Message> {
        val chat = chats.firstOrNull { it.id == idChat } ?: return emptyList()
        val updateMessages = chat.messages
            .filter { it.id >= idLastMessageFrom }
            .take(countMessage)
            .map { it.copy(isRead = true) }

        val notUpdateMessages = chat.messages.filterNot { it.id >= idLastMessageFrom }

        val updatesChat = chat.copy(messages = notUpdateMessages + updateMessages)

        chats.removeIf { updatesChat.id == it.id }
        chats.add(updatesChat)

        return updateMessages

    }
    fun editMessage(idChat: Int, idMessage: Int, message: Message): Boolean {
        val chat = chats.firstOrNull { it.id == idChat } ?: return false
        val editMessage = chat.messages
            .filter { it.id == idMessage }
            .map { it.copy(text = message.text, isDelete = message.isDelete) }
        val updateChat = chat.copy(messages = editMessage)
        chats.removeIf { updateChat.id == it.id }
        chats.add(updateChat)
        return true
    }

    fun deleteMessage(idChat: Int, idMessage: Int): Boolean {
        val chat = chats.firstOrNull { it.id == idChat } ?: return false
        val updateMessage = chat.messages
            .filter { it.id == idMessage }
            .map { it.copy(isDelete = true) }

        val notUpdateMessage = chat.messages.filterNot { it.id == idMessage }
        val updatedChat = chat.copy(messages = notUpdateMessage + updateMessage)
        if (notUpdateMessage.isEmpty()) {
            deleteChat(idChat)
        } else {
            chats.removeIf { updatedChat.id == it.id }
            chats.add(updatedChat)
        }

        return true
    }
}

fun main() {
ChatService.createMessage(1, 1, "text1")
    println(ChatService.getChats(1))
    println(ChatService.getUnreadChatsCount(1))
    ChatService.editMessage(1, 1, message = Message(1, 1, "text2", true, isDelete = false))
    println(ChatService.getUnreadChatsCount(1))
    ChatService.deleteMessage(1, 1)
    println(ChatService.getMessages(1, 1, 1))
    ChatService.clear()
    println(ChatService.getChats(1))
}