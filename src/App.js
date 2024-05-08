
import React from 'react';
import ItemList from './components/ItemList';
import Register from './components/Register';
import { createRoot } from 'react-dom/client'; // Import createRoot from react-dom/client

// Use createRoot instead of ReactDOM.render
createRoot(document.getElementById('root')).render(<App />);

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';




function App() {
    return (
        <Router>
            <Routes>
                <Route path="/register" element={<Register />} />
                <Route path="/itemList" element={<ItemList />} />
            </Routes>
        </Router>
    );
}



export default App;