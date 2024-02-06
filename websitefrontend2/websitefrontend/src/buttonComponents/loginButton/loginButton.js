import React, {useState} from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import Cookies from 'js-cookie';
import LoginIcon from '@mui/icons-material/Login';
import styles from "./loginButton.module.css";
import '../../global.css';
import { useHistory } from 'react-router-dom';
import ForgotPasswordButton from "../forgotPasswordButton/forgotPasswordButton";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
import {fetchWithJWT} from "../../utility/fetchInterceptor";
const LoginButton = () => {
  const [isVisible, setIsVisible] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  const [isVerified, setIsVerified] = useState(false);
  const [error, setError] = useState('');
  const history = useHistory();
  const { user, updateUser } = useUserContext();

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

    const requestBody = new URLSearchParams();
    requestBody.append('username', username);
    requestBody.append('password', password);

    fetchWithJWT('/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: requestBody.toString(),
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
          console.log('fetch data:', data);
          updateUser(data.user);
          Cookies.set('userData', JSON.stringify(data.user));
          Cookies.set('jwt', data.jwt);
          history.push('/AllUsersPage');
        })
        .catch((error) => {
          console.error('Error logging in:', error);
          setError('Error logging in. Please try again later.');
        });
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
  };



  return (
      <div>
        <LoginIcon
            className="button-common"
            onClick={() => setIsVisible(!isVisible)}
            style={{ width: '50px', height: 'auto', background: 'none', color: 'white' }}
        />
        {isVisible && (
            <div className={styles.overlay}>
              <div className={styles.loginFormContainer}>
                <button
                    className={styles.closeButton}
                    onClick={() => setIsVisible(false)}
                    style={{ ...buttonStyle, border: '2px solid black', borderRadius: '5px' }}
                >
                  X
                </button>
                <h2 style={{ fontSize: "30px" }}>Login</h2>
                <form onSubmit={handleLogin}>
                  <div className={styles.inputGroup}>
                    <label style={{ fontSize: "15px" }}>Username:</label>
                    <input type="text" name="username" value={username} onChange={handleInputChange} />
                  </div>
                  <div className={styles.inputGroup}>
                    <label style={{ fontSize: "15px" }}>Password:</label>
                    <input type="password" name="password" value={password} onChange={handleInputChange} autoComplete={'off'} />
                  </div>
                  <ForgotPasswordButton />
                  <div className={styles.buttonContainer}>
                    <button
                        type="submit"
                        style={{ ...buttonStyle, border: '3px solid black', borderRadius: '40px', color: "white", alignItems: "center" }}
                        className={`${styles.buttonCommon} ${styles.buttonScale}`}
                    >
                      Login
                    </button>
                  </div>
                  {error && <p style={{ color: 'red', transform: "translateX(60px)" }}>{error}</p>}
                </form>
              </div>
            </div>
        )}
      </div>
  );
};


export default LoginButton;