import './App.css';
import React,{ useState } from 'react';
import SearchComponent from './components/js/SearchComponent';
import AddDocument from './components/js/AddDocument';

function App() {
  const [activeTab,setActiveTab] = useState('search');

  const renderTab = () => {
      switch(activeTab) {
        case 'search': 
          return <SearchComponent />

        case 'uploadDocument':
          return <AddDocument/>
        
        default: 
          return <SearchComponent />
      }
  }

  return (
    <div className="app-container">
      <div className="app-content">
        <h1>Search Engine</h1>
          <div className='tab-navigation'>
            <button onClick={() => setActiveTab('search')}>Search</button>
            <button onClick={() => setActiveTab('uploadDocument')}>Upload File</button>
          </div>
          <div className='tab-render'>
            {renderTab()}
          </div>
      </div>
    </div>
  );
}
export default App;
