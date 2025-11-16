package ru.netology

fun main() {
//    println(ChatService.getChats())
    println(ChatService.getMessagesFromChat(2, 4))
    println(ChatService.getLastMessagesFromChats())
    ChatService.sendMessageToMe(2, "hi")
    ChatService.sendMessageToMe(2, "hi")
    ChatService.sendMessageToMe(2, "hi")
    ChatService.sendMessageToMe(2, "hi")
    ChatService.sendMessageToMe(3, "hi")
    println(ChatService.getUnreadChatsCount())
    println(ChatService.getChats())
    println(ChatService.getLastMessagesFromChats())
    println(ChatService.getMessagesFromChat(2, 4))
    println(ChatService.getMessagesFromChat(2, 4))
    println(ChatService.getUnreadChatsCount())

    println()
    ChatService.sendMessage(2, "helllo")
    println(ChatService.getMessagesFromChat(2, 5))
    println(ChatService.getChats())

    ChatService.deleteMessage(1)
    println(ChatService.getMessagesFromChat(2, 5))

    println()
    ChatService.deleteChat(2)
    println(ChatService.getMessagesFromChat(2, 5))

    println()
    ChatService.sendMessageToMe(2, "hi")
    println(ChatService.getMessagesFromChat(2, 5))
    println(ChatService.getUnreadChatsCount())
    ChatService.sendMessageToMe(2, "helllo")
    ChatService.deleteMessage(8)
    println(ChatService.getUnreadChatsCount())
}