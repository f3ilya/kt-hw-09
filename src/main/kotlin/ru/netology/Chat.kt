package ru.netology

import kotlin.math.min

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
        messages.add(Message(++messageId, toId, toId, MY_ID, message))
        val chatIndex = chats.indexOfFirst { it.chatId == toId }
        chats.find { it.chatId == toId }?.let {
            chats[chatIndex] = it.copy(messageCount = it.messageCount + 1)
        } ?: run {
            chats.add(Chat(toId, 1))
        }
        return messages.last().messageId
    }

    fun deleteMessage(messageId: Int): Boolean {
        val chatId = messages.find { it.messageId == messageId }?.chatId ?: return false
        val chatIndex = chats.indexOfFirst { it.chatId == chatId }
        messages.removeIf { it.messageId == messageId }
            .also {
                chats.find { it.chatId == chatId }
                    .takeIf { it!!.messageCount - 1 != 0 }
                    ?: run {
                        chats.removeAt(chatIndex)
                        return true
                    }
            }
        val chat = chats[chatIndex]
        chats[chatIndex] = chat.copy(messageCount = chat.messageCount - 1)
        readingChat(chat.chatId)
        return true
    }

    fun deleteChat(chatId: Int): Boolean =
        chats.removeIf { it.chatId == chatId }
            .also {
                messages.removeAll { it.chatId == chatId }
            }

    fun getUnreadChatsCount() = chats.count { !it.isRead }

    fun getChats() = chats.ifEmpty { throw ChatNotFoundException("Chat not found") }

    fun getLastMessagesFromChats(): List<String> =
        messages
            .groupBy { it.chatId }
            .map { it.value.lastOrNull()?.message ?: "Нет сообщений" }
            .ifEmpty { listOf("Нет сообщений") }

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
        messages
            .filter { it.chatId == chatId }
            .reversed()
            .subList(0, min(count, messages.count { it.chatId == chatId }))
            .reversed()
            .asSequence()
            .let { list ->
                list.find { !it.isRead }
                    ?.let {
                    list.forEach {
                        ret.add(readingMessages(it))
                    }
                } ?: return list.toMutableList()
            }
        readingChat(chatId)
        return ret
    }

    fun sendMessageToMe(fromId: Int, message: String): Int {
        messages.add(Message(++messageId, fromId, MY_ID, fromId, message, false))
        val chatIndex = chats.indexOfFirst { it.chatId == fromId }
        chats.find { it.chatId == fromId }?.let {
            chats[chatIndex] = it.copy(messageCount = it.messageCount + 1, isRead = false)
        } ?: run {
            chats.add(Chat(fromId, 1, false))
        }
        return messages.last().messageId
    }

    fun clear() {
        chats.clear()
        messages.clear()
        messageId = 0
    }
}