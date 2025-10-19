// Mock API function - Replace with your actual AI service integration
export const sendMessageToAI = async (message: string): Promise<string> => {
  // Simulate API call delay
  await new Promise(resolve => setTimeout(resolve, 1000 + Math.random() * 2000));
  
  // This is a mock response - Integrate with your actual AI service
  const mockResponses: { [key: string]: string } = {
    'anxious': "I understand you're feeling anxious. Let's try some deep breathing exercises together. Breathe in slowly for 4 seconds, hold for 4 seconds, and exhale for 6 seconds. Would you like to talk more about what's causing your anxiety?",
    'stress': "Stress can be overwhelming. Remember to take breaks and practice self-care. In Jamaica, we have beautiful nature spots that can help you relax. Have you considered taking a walk in one of our lovely parks or beaches?",
    'depressed': "I'm sorry you're feeling this way. Depression can be really challenging. It's important to remember that you're not alone. Have you considered speaking with a professional? I can connect you with local resources in Jamaica.",
    'default': "Thank you for sharing. Your mental health is important, and I'm here to support you. Could you tell me more about what you're experiencing? This will help me provide better guidance and connect you with the most appropriate resources here in Jamaica."
  };

  const lowerMessage = message.toLowerCase();
  
  if (lowerMessage.includes('anxious') || lowerMessage.includes('anxiety')) {
    return mockResponses.anxious;
  } else if (lowerMessage.includes('stress') || lowerMessage.includes('stressed')) {
    return mockResponses.stress;
  } else if (lowerMessage.includes('depress') || lowerMessage.includes('sad')) {
    return mockResponses.depressed;
  } else {
    return mockResponses.default;
  }
};