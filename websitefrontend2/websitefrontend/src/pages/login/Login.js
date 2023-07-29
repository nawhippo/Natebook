import React, { useState } from 'react';
import { useHistory } from 'react-router-dom'; // Import useHistory hook
import { useUserContext } from './UserContext';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const { setUser } = useUserContext();
  const history = useHistory(); // Get access to the history object

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    if (name === 'username') {
      setUsername(value);
    } else if (name === 'password') {
      setPassword(value);
    }
    setError('');
  };

  const handleLogin = (event) => {
    event.preventDefault();

    fetch('/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    })
      .then((response) => {
        if (response.ok) {
          // If login is successful, fetch user data
          return response.json();
        } else {
          // If login fails, handle error
          setError('Invalid username or password.');
          throw new Error('Invalid username or password.');
        }
      })
      .then((data) => {
        // Set user data in context and redirect to home page
        setUser(data);
        history.push('/api/home');
      })
      .catch((error) => {
        console.error('Error logging in:', error);
      });
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label>Username:</label>
          <input type="text" name="username" value={username} onChange={handleInputChange} />
        </div>
        <div>
          <label>Password:</label>
          <input type="password" name="password" value={password} onChange={handleInputChange} />
        </div>
        <button type="submit">Login</button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
    </div>
  );
};



export default Login