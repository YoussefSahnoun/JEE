import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8081/api', // Set your base URL
    timeout: 5000, // Set a timeout for requests (optional)
    headers: {
        'Content-Type': 'application/json', // Set default content type
    },
});

export default axiosInstance;