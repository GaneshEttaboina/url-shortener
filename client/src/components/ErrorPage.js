// src/pages/ErrorPage.js
import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './Page.css';

function ErrorPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const reason = new URLSearchParams(location.search).get('reason');

  const getMessage = () => {
    switch (reason) {
      case 'expired':
        return '⚠️ This short URL has expired. Please generate a new one.';
      case 'not_found':
        return '❌ This short URL does not exist.';
      default:
        return 'Something went wrong. Please try again.';
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow p-4 text-center" style={{ width: '400px' }}>
        <h4 className="mb-3 text-danger">Link Error</h4>
        <p>{getMessage()}</p>
        <div className="d-flex justify-content-center mt-3">
      <button className="btn btn-primary navigate-btn" onClick={() => navigate('/')}>
        Create New Short URL
      </button>
    </div>
      </div>
    </div>
  );
}

export default ErrorPage;
