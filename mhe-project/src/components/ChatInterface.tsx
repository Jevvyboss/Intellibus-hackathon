import React, { useState, useRef, useEffect } from 'react';
import Message from './Message';
import TypingIndicator from './TypingIndicator';
import { sendMessageToAI } from '../utils/api';
import type { Message as MessageType } from '../types';

const ChatInterface: React.FC = () => {
  const [messages, setMessages] = useState<MessageType[]>([
    {
      id: 1,
      text: "Hello! I'm here to support you. I'm MindCare, your AI mental health assistant. How are you feeling today?",
      sender: 'bot',
      timestamp: new Date()
    }
  ]);
  const [inputMessage, setInputMessage] = useState<string>('');
  const [isTyping, setIsTyping] = useState<boolean>(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = (): void => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSendMessage = async (e: React.FormEvent): Promise<void> => {
    e.preventDefault();
    
    if (!inputMessage.trim()) return;

    // Add user message
    const userMessage: MessageType = {
      id: messages.length + 1,
      text: inputMessage,
      sender: 'user',
      timestamp: new Date()
    };

    setMessages(prev => [...prev, userMessage]);
    setInputMessage('');
    setIsTyping(true);

    try {
      const botResponse = await sendMessageToAI(inputMessage);
      
      const botMessage: MessageType = {
        id: messages.length + 2,
        text: botResponse,
        sender: 'bot',
        timestamp: new Date()
      };

      setMessages(prev => [...prev, botMessage]);
    } catch (error) {
      console.error('Error sending message:', error);
      const errorMessage: MessageType = {
        id: messages.length + 2,
        text: "I apologize, but I'm having trouble responding right now. Please try again in a moment.",
        sender: 'bot',
        timestamp: new Date()
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsTyping(false);
    }
  };

  const quickReplies: string[] = [
    "I'm feeling anxious",
    "I'm dealing with stress",
    "I feel depressed",
    "I need coping strategies",
    "I want to talk about my feelings"
  ];

  return (
    <div className="chat-interface">
      <div className="chat-messages">
        {messages.map(message => (
          <Message key={message.id} message={message} />
        ))}
        {isTyping && <TypingIndicator />}
        <div ref={messagesEndRef} />
      </div>

      {messages.length === 1 && (
        <div className="quick-replies">
          <p>Quick options to get started:</p>
          <div className="quick-reply-buttons">
            {quickReplies.map((reply, index) => (
              <button
                key={index}
                className="quick-reply-btn"
                onClick={() => setInputMessage(reply)}
              >
                {reply}
              </button>
            ))}
          </div>
        </div>
      )}

      <form className="chat-input-form" onSubmit={handleSendMessage}>
        <div className="input-container">
          <input
            type="text"
            value={inputMessage}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setInputMessage(e.target.value)}
            placeholder="Type your message here..."
            className="message-input"
            disabled={isTyping}
          />
          <button 
            type="submit" 
            className="send-button"
            disabled={!inputMessage.trim() || isTyping}
          >
            Send
          </button>
        </div>
      </form>
    </div>
  );
};

export default ChatInterface;