import React, { useState } from "react";
import axios from "axios";
import "./Register.css"; // Import CSS file

const Register = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8081/api/users/signup', {
                username,
                email,
                password
            });
            if (response.status === 201) {
                // Redirect to items page on successful registration
                window.location = 'http://localhost:3000/itemList';
            }
        } catch (error) {
            if (error.response && error.response.data) {
                setError(error.response.data);
            } else {
                setError('An unexpected error occurred. Please try again later.');
            }
        }
    };

    return (
        <div className="container">
            <h2>Register</h2>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleRegister}>
                <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)}
                       required/><br/>
                <input type="text" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)}
                       required/><br/>
                <input type="password" placeholder="Password" value={password}
                       onChange={(e) => setPassword(e.target.value)} required/><br/>
                <button type="submit">Register</button>
                <p>already have an account? <a href="http://localhost:8081/login.html">Login</a></p>
            </form>
            {error && error.includes('already exists')}
        </div>
    );
}

export default Register;
