package ru.mdimension.stand_bot.command;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@FunctionalInterface
public interface Command {
    SendMessage apply(long chatId);
}