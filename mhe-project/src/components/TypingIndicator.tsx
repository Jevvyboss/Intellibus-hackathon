import type { TypingIndicatorProps } from '../types';

const TypingIndicator = ({ avatar = '🤖' }: TypingIndicatorProps) => {
  return (
    <div className="message bot" role="status" aria-live="polite">
      <div className="message-content">
        <div className="typing-indicator" aria-label="Bot is typing">
          <span className="typing-dot"></span>
          <span className="typing-dot"></span>
          <span className="typing-dot"></span>
        </div>
      </div>
      <div className="message-avatar" aria-hidden="true">
        {avatar}
      </div>
    </div>
  );
};

export default TypingIndicator;
