package ru.netology

const val MY_ID = 1

class ChatNotFoundException(message: String) : Exception(message)

data class Chat(
    val chatId: Int,
    val messageCount: Int = 0,
    val isRead: Boolean = true
)

data class Message(
    val messageId: Int,
    val chatId: Int,
    val toId: Int,
    val fromId: Int,
    val message: String,
    val isRead: Boolean = true
)

object ChatService {
    private var chats = mutableListOf<Chat>()
    private var messages = mutableListOf<Message>()
    private var messageId = 0

    fun sendMessage(toId: Int, message: String): Int {
        messages += Message(++messageId, toId, toId, MY_ID, message)
        for ((index, chat) in chats.withIndex()) {
            if (chat.chatId == toId) {
                chats[index] = chat.copy(messageCount = chat.messageCount + 1)
                return messages.last().messageId
            }
        }
        chats += Chat(toId, 1)
        return messages.last().messageId
    }

    fun deleteMessage(messageId: Int): Boolean {
        for ((indexMessage, message) in messages.withIndex()) {
            if (message.messageId == messageId) {
                messages.removeAt(indexMessage)
                for ((indexChat, chat) in chats.withIndex()) {
                    if (chat.chatId == message.chatId) {
                        if (chat.messageCount - 1 == 0) {
                            chats.removeAt(indexChat)
                        } else {
                            chats[indexChat] = chat.copy(messageCount = chat.messageCount - 1)
                            readingChat(chat.chatId)
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    fun deleteChat(chatId: Int): Boolean {
        for ((indexChat, chat) in chats.withIndex()) {
            if (chat.chatId == chatId) {
                chats.removeAt(indexChat)
                val iter = messages.iterator()
                while (iter.hasNext()) {
                    val message = iter.next()
                    if (message.chatId == chatId) {
                        iter.remove()
                    }
                }
                return true
            }
        }
        return false
    }

    fun getUnreadChatsCount() = chats.count { !it.isRead }

    fun getChats() = chats.ifEmpty { throw ChatNotFoundException("Chat not found") }

    fun getLastMessagesFromChats(): MutableList<String> {
        if (messages.isEmpty()) return mutableListOf("Нет сообщений")
        val ret = mutableListOf<String>()
        for (chat in chats) {
            val iter = messages.listIterator(messages.size)
            while (iter.hasPrevious()) {
                val message = iter.previous()
                if (message.chatId == chat.chatId) {
                    ret.add(message.message)
                    break
                }
            }
        }
        return ret
    }

    private fun readingChat(chatId: Int) {
        messages.find { it.chatId == chatId && !it.isRead }?.let { return }
        chats.forEachIndexed { index, chat -> if (chat.chatId == chatId) chats[index] = chat.copy(isRead = true) }
    }

    private fun readingMessages(message: Message): Message {
        messages.forEachIndexed { index, messageFor ->
            if (messageFor.messageId == message.messageId) {
                messages[index] = messageFor.copy(isRead = true)
                return messages[index]
            }
        }
        return message
    }

    fun getMessagesFromChat(chatId: Int, count: Int): MutableList<Message> {
        val ret = mutableListOf<Message>()
        val iter = messages.listIterator(messages.size)
        var size = 0
        while (iter.hasPrevious() && count != size) {
            val message = iter.previous()
            if (message.chatId == chatId) {
                ret.add(readingMessages(message))
                size++
            }
        }
        readingChat(chatId)
        ret.reverse()
        return ret
    }

    fun sendMessageToMe(fromId: Int, message: String): Int {
        messages += Message(++messageId, fromId, MY_ID, fromId, message, false)
        for ((index, chat) in chats.withIndex()) {
            if (chat.chatId == fromId) {
                chats[index] = chat.copy(messageCount = chat.messageCount + 1, isRead = false)
                return messages.last().messageId
            }
        }
        chats += Chat(fromId, 1, false)
        return messages.last().messageId
    }

    fun clear() {
        chats.clear()
        messages.clear()
        messageId = 0
    }
}