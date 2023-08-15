import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import { useUserContext } from './UserContext';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const { user, setUser } = useUserContext();
  const history = useHistory();

  useEffect(() => {
    if (user) {
      history.push('/home');
    }
  }, [user, history]);

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

    const requestBody = {
      username: username,
      password: password,
    };
    fetch('/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody),
    })
      .then((response) => {
        if (response.ok) {
          console.log('login successful');
          return response.json();
        } else {
          setError('Invalid username or password.');
          throw new Error('Invalid username or password.');
        }
      })
      .then((data) => {
        setUser(data);
        history.push('/home');
      })
      .catch((error) => {
        console.error('Error logging in:', error);
        setError('Error logging in. Please try again later.');
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

export default Login;