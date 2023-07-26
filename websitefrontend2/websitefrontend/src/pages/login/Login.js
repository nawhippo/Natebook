import React, { useState } from 'react';
import { useUserContext } from './UserContext'; // Adjust the import path accordingly

const Login = (props) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const { setUser } = useUserContext();

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

    fetch(`/login?username=${username}&password=${password}`, {
      method: 'POST',
    })
      .then((response) => response.json())
      .then((data) => {
        fetch(`/users/${data.userId}`)
          .then((response) => response.json())
          .then((userData) => {
            setUser(userData);
            props.history.push('/home');
          })
          .catch((error) => {
            console.error('Error fetching user data:', error);
          });
      })
      .catch((error) => {
        console.error('Error logging in:', error);
        setError('Invalid username or password.');
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