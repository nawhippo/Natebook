import React, { useState } from "react";
import { useHistory } from 'react-router-dom';
import Cookies from 'js-cookie';
import { useUserContext } from "../../pages/usercontext/UserContext";
import styles from './createAccount.module.css';
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import '../../global.css';
import { getRandomColor } from "../../FunSFX/randomColorGenerator";
import { fetchWithJWT } from "../../utility/fetchInterceptor";

const CreateAccount = ({ onBackToVerifyEmail, email }) => {
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    email: email || "",
    password: "",
    username: "",
  });
  const [error, setError] = useState('');
  const [isVisible, setIsVisible] = useState(false);
  const {updateUser} = useUserContext();
  const history = useHistory();

  const handleChange = (event) => {
    const {name, value} = event.target;
    setFormData(prevFormData => ({...prevFormData, [name]: value}));
  };

  const buttonStyle = {
    backgroundColor: getRandomColor(),
  };

  const handleSubmit = async (event) => {
    event.preventDefault();


    const isEmailVerified = await checkEmailVerification(formData.email);
    if (!isEmailVerified) {
      setError("Your email has not been verified. Please verify your email before creating an account.");
      onBackToVerifyEmail && onBackToVerifyEmail();
      return;
    }

    const response = await fetchWithJWT("/api/account/createAccount", {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(formData),
    });

    if (response.ok) {
      const data = await response.json();
      updateUser(data.user);
      Cookies.set('userData', JSON.stringify(data.user));
      Cookies.set('jwt', data.jwt);
      history.push('/feed');
    } else {
      setError("Failed to create account. Please try again.");
    }
  };

  const checkEmailVerification = async (email) => {
    try {
      const response = await fetchWithJWT("/api/account/checkVerification", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({email}),
      });
      const result = await response.json();
      return result;
    } catch (error) {
      console.error("Error checking email verification:", error);
      setError("Failed to verify email. Please try again.");
      return false;
    }
  };

  return (
      <div>
            <div className={styles.overlay}>
              <div className={styles.createAccountFormContainer}>
                <button className={styles.closeButton} onClick={() => setIsVisible(false)} style={buttonStyle}>X
                </button>
                <h2 style={{fontSize: "30px"}}>Create Account</h2>
                <form onSubmit={handleSubmit} className="createAccountForm">
                  <div className={styles.inputGroup}>
                    <label htmlFor="firstname">First Name:</label>
                    <input
                        type="text"
                        id="firstname"
                        name="firstname"
                        value={formData.firstname}
                        onChange={handleChange}
                        required
                    />
                  </div>
                  <div className={styles.inputGroup}>
                    <label htmlFor="lastname">Last Name:</label>
                    <input
                        type="text"
                        id="lastname"
                        name="lastname"
                        value={formData.lastname}
                        onChange={handleChange}
                        required
                    />
                  </div>
                  <div className={styles.inputGroup}>
                    <label htmlFor="username">User Name:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                  </div>
                  <div className={styles.inputGroup}>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                  </div>
                  <div className={styles.inputGroup}>
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                  </div>
                  <div className={styles.buttonContainer}>
                    <button type="submit" style={{...buttonStyle, border: '3px solid black', color: 'white'}}>
                      Submit
                    </button>
                  </div>
                  {error && <p style={{color: 'red'}}>{error}</p>}
                </form>
              </div>
            </div>
      </div>
  )
}
export default CreateAccount;