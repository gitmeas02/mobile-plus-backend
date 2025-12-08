package com.example.mobile.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
public class TelegramOTPService extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Handle incoming messages if needed
        // For OTP service, we mainly send messages
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();
            
            if (messageText.equals("/start")) {
                sendWelcomeMessage(chatId);
            } else if (messageText.equals("/getchatid")) {
                sendChatId(chatId);
            }
        }
    }

    /**
     * Send OTP code via Telegram
     * @param chatId Telegram chat ID
     * @param otpCode OTP code to send
     * @param username Username of the user
     */
    public void sendOTPViaTelegram(String chatId, String otpCode, String username) {
        try {
            String message = buildOTPMessage(username, otpCode);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            sendMessage.enableMarkdown(true);
            
            execute(sendMessage);
            System.out.println("OTP sent via Telegram to chat ID: " + chatId);
        } catch (TelegramApiException e) {
            System.err.println("Failed to send Telegram OTP: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP via Telegram. Please try again later.");
        }
    }

    /**
     * Build OTP message for Telegram
     */
    private String buildOTPMessage(String username, String otpCode) {
        return String.format("""
            🔐 *Mobile Plus - OTP Verification*
            
            Hello *%s*,
            
            Your One-Time Password (OTP) is:
            
            `%s`
            
            ⏱️ This OTP is valid for *5 minutes*.
            ⚠️ Please do not share this code with anyone.
            
            If you didn't request this code, please ignore this message.
            
            ---
            _Mobile Plus Security Team_
            """, 
            username, 
            otpCode
        );
    }

    /**
     * Send welcome message when user starts the bot
     */
    private void sendWelcomeMessage(String chatId) {
        try {
            String message = """
                👋 Welcome to Mobile Plus Bot!
                
                This bot is used to send OTP codes for authentication.
                
                To link your account:
                1. Use the `/getchatid` command to get your Chat ID
                2. Add this Chat ID to your Mobile Plus account settings
                3. Select Telegram as your OTP delivery method
                
                For support, please contact our team.
                """;
            
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println("Failed to send welcome message: " + e.getMessage());
        }
    }

    /**
     * Send chat ID to user
     */
    private void sendChatId(String chatId) {
        try {
            String message = String.format("""
                🆔 *Your Telegram Chat ID*
                
                `%s`
                
                Copy this ID and add it to your Mobile Plus account to receive OTP codes via Telegram.
                """, 
                chatId
            );
            
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            sendMessage.enableMarkdown(true);
            
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println("Failed to send chat ID: " + e.getMessage());
        }
    }
}
