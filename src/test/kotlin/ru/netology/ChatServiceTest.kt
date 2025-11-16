package ru.netology

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun sendMessage() {
        ChatService.sendMessage(2, "hello")
        assertEquals(1, ChatService.getChats().size)
    }

    @Test
    fun sendMessageCheckMessage() {
        ChatService.sendMessage(2, "hello")
        assertEquals("hello", ChatService.getMessagesFromChat(2, 1)[0].message)
    }

    @Test
    fun deleteMessage() {
        ChatService.sendMessage(2, "hello")
        ChatService.sendMessage(2, "hello")
        ChatService.deleteMessage(2)
        assertEquals(1, ChatService.getChats().size)
    }

    @Test
    fun deleteMessageCheckUnread() {
        ChatService.sendMessage(2, "hello")
        ChatService.sendMessageToMe(2, "hi")
        ChatService.deleteMessage(2)
        assertEquals(0, ChatService.getUnreadChatsCount())
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChatException() {
        ChatService.sendMessage(2, "hello")
        ChatService.sendMessage(2, "hello")
        ChatService.deleteChat(2)
        ChatService.getChats()
    }

    @Test
    fun deleteChat() {
        ChatService.sendMessage(2, "hello")
        ChatService.sendMessage(3, "hello")
        ChatService.deleteChat(2)
        assertEquals(1, ChatService.getChats().size)
    }

    @Test
    fun getUnreadChatsCount() {
        ChatService.sendMessageToMe(2, "hi")
        ChatService.sendMessageToMe(3, "hello")
        assertEquals(2, ChatService.getUnreadChatsCount())
    }

    @Test
    fun getUnreadChatsCountCheckDeleteMessage() {
        ChatService.sendMessageToMe(2, "hello")
        ChatService.sendMessageToMe(3, "hello")
        ChatService.deleteMessage(2)
        assertEquals(1, ChatService.getUnreadChatsCount())
    }

    @Test
    fun getUnreadChatsCountCheckDeleteChat() {
        ChatService.sendMessageToMe(2, "hello")
        ChatService.sendMessageToMe(3, "hello")
        ChatService.deleteChat(2)
        assertEquals(1, ChatService.getUnreadChatsCount())
    }

    @Test
    fun getChats() {
        ChatService.sendMessageToMe(2, "hello")
        ChatService.sendMessageToMe(3, "hello")
        assertEquals(2, ChatService.getChats().size)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getChatsException() {
        ChatService.getChats()
    }

    @Test
    fun getLastMessageFromChatsEmpty() {
        val exp = "Нет сообщений"
        assertEquals(exp, ChatService.getLastMessagesFromChats()[0])
    }

    @Test
    fun getLastMessageFromChats() {
        ChatService.sendMessageToMe(2, "hello")
        ChatService.sendMessageToMe(3, "hello")
        ChatService.sendMessageToMe(3, "hello1")
        val exp = mutableListOf("hello", "hello1")
        assertEquals(exp, ChatService.getLastMessagesFromChats())
    }

    @Test
    fun getMessagesFromChat() {
        ChatService.sendMessageToMe(2, "hello")
        ChatService.sendMessageToMe(3, "hello1")
        ChatService.sendMessageToMe(3, "hello2")
        assertEquals("hello1", ChatService.getMessagesFromChat(3, 2)[0].message)
    }

    @Test
    fun getMessagesFromChatCheckRead() {
        ChatService.sendMessageToMe(2, "hello")
        ChatService.sendMessageToMe(3, "hello1")
        ChatService.sendMessageToMe(3, "hello2")
        assertTrue(ChatService.getMessagesFromChat(2,2)[0].isRead)
    }

    @Test
    fun getMessagesFromChatCheckReadChat() {
        ChatService.sendMessageToMe(2, "hello")
        ChatService.sendMessageToMe(3, "hello1")
        ChatService.sendMessageToMe(3, "hello2")
        ChatService.getMessagesFromChat(3, 2)
        assertEquals(1, ChatService.getUnreadChatsCount())
    }

    @Test
    fun sendMessageToMe() {
        ChatService.sendMessageToMe(2, "hello")
        ChatService.sendMessageToMe(3, "hello1")
        assertEquals(2, ChatService.getUnreadChatsCount())
    }
}