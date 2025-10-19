export interface Message {
  id: number;
  text: string;
  sender: 'user' | 'bot';
  timestamp: Date;
}

export interface Resource {
  name: string;
  description: string;
  phone?: string;
  website?: string;
  email?: string;
  location?: string;
  hours?: string;
}

export interface ResourceCategory {
  [category: string]: Resource[];
}

export interface ChatInterfaceProps {}

export interface MessageProps {
  message: Message;
}

export  interface ResourceListProps {}

export interface TypingIndicatorProps {
    avatar?: String;
}