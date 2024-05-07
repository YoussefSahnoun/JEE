import {useState} from "react";
import axios from "axios";

const Register =() =>{
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8081/api/users/register', {
                username,
                email,
                password
            });
            if (response.status === 201) {
                // Redirect to items page on successful registration
                window.location = 'http://localhost:8081/api/v1/items';
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
        <div>
            <h2>Register</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleRegister}>
                <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} required /><br />
                <input type="text" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required /><br />
                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required /><br />
                <button type="submit">Register</button>
            </form>
        </div>
    );
}
export default Register;