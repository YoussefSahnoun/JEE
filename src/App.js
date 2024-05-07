
import React from 'react';
import Login from './components/Login';
import Register from './components/Register';
import { createRoot } from 'react-dom/client'; // Import createRoot from react-dom/client

// Use createRoot instead of ReactDOM.render
createRoot(document.getElementById('root')).render(<App />);

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';




function App() {
    return (
        <Router>
            <Routes>
                <Route path="/login" element={< Login />} />
                <Route path="/register" element={<Register />} />
            </Routes>
        </Router>
    );
}



export default App;