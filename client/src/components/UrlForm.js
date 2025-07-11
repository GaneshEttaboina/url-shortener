import React, { useState } from 'react';
import axios from 'axios';
import './Page.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

function UrlForm() {
  const [url, setUrl] = useState('');
  const [shortUrl, setShortUrl] = useState('');
  const [error, setError] = useState('');
  const [isValid, setIsValid] = useState(null);
  const [loading, setLoading] = useState(false);
  const [showToast, setShowToast] = useState(false);
  const [allUrls, setAllUrls] = useState([]);
  const [fetchError, setFetchError] = useState('');
 
  const [showModal, setShowModal] = useState(false);

  const fetchAllUrls = async () => {
  setFetchError(''); // clear previous error

  try {
    const response = await axios.get("http://localhost:7070/api/urls");
    const urls = response.data;

    setAllUrls(urls);
    setShowModal(true);  // show modal regardless
  } catch (error) {
    console.error("Failed to fetch URLs", error);
    setAllUrls([]); // clear previous list
    setFetchError('⚠️ Unable to fetch URLs. Please ensure the backend server is running.');
    setShowModal(true); // still show modal
  }
};

  const validateUrl = (inputUrl) => {
    const regex = /^(https?:\/\/)([\w.-]+)(:[0-9]+)?(\/.*)?$/;
    return regex.test(inputUrl);
  };

  const handleChange = (e) => {
    const input = e.target.value.trim();
    setUrl(input);
    setIsValid(input ? validateUrl(input) : null);
    setError('');
    setShortUrl('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isValid) return;

    setLoading(true);
    setError('');
    try {
      const response = await axios.post('http://localhost:7070/api/shorten', { url });
      setShortUrl(response.data.shortUrl);
      navigator.clipboard.writeText(response.data.shortUrl);
      setShowToast(true);
      setTimeout(() => setShowToast(false), 2000);
    } catch (err) {
      setError('Something went wrong. Please try again.');
    }
    setLoading(false);
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-300" style={{ textAlign: 'center', marginTop: '50px' }}>
      <div className="card p-4 shadow" style={{ width: '500px' }}>
        <h4 className="text-center mb-4">Link Shortener</h4>

        <form onSubmit={handleSubmit} className="d-flex gap-2">
          <input
            type="text"
            className="form-control"
            placeholder="Enter your URL"
            value={url}
            onChange={handleChange}
            required
          />
          <button
            type="submit"
            className="btn btn-primary"
            disabled={!isValid || loading}
          >
            {loading ? 'Loading...' : 'Shorten'}
          </button>
            <i
    className="bi bi-list fs-4 text-primary cursor-pointer"
    title="Show Generated URLs"
    onClick={fetchAllUrls}
    style={{ cursor: 'pointer' }}
  ></i>
        </form>

        {isValid === false && url && (
          <p className="text-danger mt-2">❌ Invalid URL format</p>
        )}

        {shortUrl && (
          <div className="mt-3 text-center">
            <p>
              ✅ Short URL: <a href={shortUrl} target="_blank" rel="noreferrer">{shortUrl}</a>
            </p>

            {showToast && (
              <div className="custom-toast text-white p-2 rounded">
                Copied to clipboard!
              </div>
            )}
          </div>
        )}

        {error && <p className="text-danger mt-2 text-center">{error}</p>}

       
    

        {/* Conditional Table */}
        
        {showModal && (
  <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
    <div className="modal-dialog modal-lg modal-dialog-centered">
      <div className="modal-content">
        <div className="modal-header">
          <h5 className="modal-title">📋 Generated URLs</h5>
          <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
        </div>

        <div className="modal-body">
  {fetchError ? (
    <div className="alert alert-danger text-center">{fetchError}</div>
  ) : allUrls.length === 0 ? (
    <div className="text-center text-muted">No URLs have been generated yet.</div>
  ) : (
    <div className="custom-table-wrapper">
      <table className="table table-bordered table-striped">
        <thead className="table-dark sticky-header">
          <tr>
            <th style={{ width: '50%' }}>Original URL</th>
            <th style={{ width: '30%' }}>Short URL</th>
            <th style={{ width: '20%' }}>Status</th>
          </tr>
        </thead>
        <tbody>
          {allUrls.map((item, index) => (
            <tr key={index}>
              <td>{item.originalUrl}</td>
              <td>
                <a href={item.shortUrl} target="_blank" rel="noreferrer">
                  {item.shortUrl}
                </a>
              </td>
              <td>
                <span className={`badge ${item.status === 'active' ? 'bg-success' : 'bg-danger'}`}>
                  {item.status === 'active' ? 'Active' : 'Expired'}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )}
</div>



      </div>
    </div>
  </div>
)}

      </div>
    </div>
  );
}

export default UrlForm;
