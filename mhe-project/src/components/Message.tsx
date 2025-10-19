import React from 'react';
import type { MessageProps } from '../types/index';

const Message: React.FC<MessageProps> = ({ message }) => {
  const formatTime = (timestamp: Date): string => {
    return timestamp.toLocaleTimeString('en-JM', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
  };

  return (
    <div className={`message ${message.sender}`}>
      <div className="message-content">
        <div className="message-text">
          {message.text}
        </div>
        <div className="message-time">
          {formatTime(message.timestamp)}
        </div>
      </div>
      <div className="message-avatar">
        {message.sender === 'bot' ? '🤖' : '👤'}
      </div>
    </div>
  );
};

export default Message;