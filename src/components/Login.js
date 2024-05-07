import React, { useState } from 'react';
import axios from 'axios';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const requestData = {
                username,
                password
            };

            console.log('Sending request with data:', requestData);

            const response = await axios.post(
                'http://localhost:8081/api/users/login',
                requestData,
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            );

            console.log('Received response:', response);

            if (response.status === 200) {
                // Redirect to items page on successful login
                window.location = '/api/v1/items';
            }
        } catch (error) {
            console.error('Error during login:', error);
            setError('Invalid username or password');
        }
    };

    return (
        <div>
            <h2>Login</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleLogin}>
                <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} required /><br />
                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required /><br />
                <button type="submit">Login</button>
            </form>
        </div>
    );
};

export default Login;
