import React from 'react';

import { BrowserRouter as Router } from 'react-router-dom'
import { createBrowserHistory } from 'history';
import './App.css';
import Routes from './routes/routes';

const history = createBrowserHistory();

function App() {
  return (
    <div className="App">
      <Router>
        <Routes />
      </Router>
    </div>
  );
}

export default App;
