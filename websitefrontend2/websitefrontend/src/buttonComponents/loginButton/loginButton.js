import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import Cookies from 'js-cookie';
import GoogleSignInButton from "../googleSigninButton/googleSignInButton";
import ForgotPasswordButton from "../forgotPasswordButton/forgotPasswordButton";
import styles from "./loginButton.module.css"
import LoginIcon from '@mui/icons-material/Login';
const LoginButton = () => {
  const [isVisible, setIsVisible] = useState(false); 
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const { setUser, clearUserContext, user } = useUserContext();

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
            return response.json();
          } else {
            setError('Invalid username or password.');
            throw new Error('Invalid username or password.');
          }
        })
        .then((data) => {
          console.log('Fetched data:', data);
          setUser(data); // Update the user context with the fetched data
          Cookies.set('userData', JSON.stringify(data)); // Store user data in a cookie
        })
        .catch((error) => {
          console.error('Error logging in:', error);
          setError('Error logging in. Please try again later.');
        });
  };

  return (
      <div>
        <LoginIcon className="button-common" onClick = {() => setIsVisible(!isVisible)} style={{ width: '50px', height: 'auto', background: 'none', color: 'white' }} /> {/* Toggle button */}
        {isVisible && (
            <div className={styles.overlay}>
              <div className={styles.loginFormContainer}>
                <button className={styles.closeButton} onClick={() => setIsVisible(false)}>X</button>
                <h2>Login</h2>
                <form onSubmit={handleLogin}>
                  <div className={styles.inputGroup}>
                    <label>Username:</label>
                    <input type="text" name="username" value={username} onChange={handleInputChange} />
                  </div>
                  <div className={styles.inputGroup}>
                    <label>Password:</label>
                    <input type="password" name="password" value={password} onChange={handleInputChange} autoComplete={"off"} />
                  </div>
                  <div className={styles.buttonContainer}>
                    <button type="submit">Login</button>
                    <ForgotPasswordButton />
                  </div>
                  {error && <p style={{ color: 'red' }}>{error}</p>}
                </form>
              </div>
            </div>
        )}
      </div>
  );
};

export default LoginButton;