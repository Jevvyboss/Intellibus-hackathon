import { useState } from 'react';
import ChatInterface from './components/ChatInterface';
import ResourceList from './components/ResourceList';
//import 'App.css';
import './App.css';

type ActiveTab = 'chat' | 'resources';

function App(){
  const [activeTab, setActiveTab] = useState<ActiveTab>('chat');

  return (
    <div className="App">
      <header className="app-header">
        <div className="header-content">
          <h1>MindCare Jamaica</h1>
          <p>Your AI Mental Health Companion</p>
        </div>
        <nav className="nav-tabs">
          <button 
            className={`tab-button ${activeTab === 'chat' ? 'active' : ''}`}
            onClick={() => setActiveTab('chat')}
          >
            Chat Session
          </button>
          <button 
            className={`tab-button ${activeTab === 'resources' ? 'active' : ''}`}
            onClick={() => setActiveTab('resources')}
          >
            Local Resources
          </button>
        </nav>
      </header>

      <main className="main-content">
        {activeTab === 'chat' ? <ChatInterface /> : <ResourceList />}
      </main>

      <footer className="app-footer">
        <p>
          <strong>Important:</strong> This is an AI assistant and not a substitute for professional medical advice. 
          In case of emergency, please contact the Jamaica Mental Health Crisis Line at <strong>888-NEW-LIFE (639-5433)</strong>
        </p>
      </footer>
    </div>
  );
}

export default App;