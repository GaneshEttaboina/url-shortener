import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import UrlForm from './components/UrlForm';
import ErrorPage from './components/ErrorPage';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    
    <Router>
      <Routes>
        <Route path="/" element={<UrlForm />} />
        <Route path="/error" element={<ErrorPage />} />
      </Routes>
    </Router>
    
  );
}

export default App;
