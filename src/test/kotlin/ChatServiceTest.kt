import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import kotlin.test.assertFailsWith

class ChatServiceTest {

    @Before
    fun clear() {
        ChatService.clear()
    }

    @Test
    fun createMessage() {
        val result = ChatService.createMessage(1, 1, "text1")
        assertTrue(result)
    }

    @Test
    fun getChats() {
        ChatService.getChats(1)
        ChatService.createMessage(2, 2, "text2")
        val result = ChatService.getChats(2)
        val expected = ChatService.getChats(2)
        assertEquals(expected, result)

    }

    @Test
    fun getUnreadChatsCount() {
        ChatService.createMessage(2, 2, "text2")
        ChatService.createMessage(3, 3, "text3")
        val result = ChatService.getUnreadChatsCount(1)
        val expected = 0
        assertEquals(expected, result)
    }

    @Test
    fun getMessages() {
        ChatService.createMessage(5, 5, "text5")
        ChatService.createMessage(6, 6, "text6")
        ChatService.createMessage(7, 7, "text7")
        val result = ChatService.getMessages(2, 1, 1)
        val expected = listOf(Message(2, 6, "text6", true, isDelete = false))
        assertEquals(expected, result)
    }

    @Test
    fun editMessage() {
        ChatService.createMessage(9, 9, "text9")
        val edit = Message(9, 9, "text99", true, isDelete = false)
        val result = ChatService.editMessage(9,9, edit)
        val expected = false
        assertEquals(expected, result)
    }

    @Test
    fun deleteMessage() {
        ChatService.createMessage(1, 1, "text1")
        val result= ChatService.deleteMessage(1, 1)
        assertTrue(result)
    }
}