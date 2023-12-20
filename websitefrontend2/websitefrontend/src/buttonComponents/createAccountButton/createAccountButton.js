import React, { useState } from "react";
import { useHistory } from 'react-router-dom';
import Cookies from 'js-cookie';
import { useUserContext } from "../../pages/usercontext/UserContext";
import styles from './createAccount.module.css';
import AccountBoxIcon from '@mui/icons-material/AccountBox';
const CreateAccount = () => {
  const [isVisible, setIsVisible] = useState(false);
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    email: "",
    password: "",
    username: "",
  });
  const [error, setError] = useState('');
  const { updateUser, user } = useUserContext();
  const history = useHistory();

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevFormData) => ({ ...prevFormData, [name]: value }));
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'orange'
  };
  const handleSubmit = (event) => {
    event.preventDefault();

    if (!formData.firstname || !formData.lastname || !formData.username || !formData.email || !formData.password) {
      setError("Please fill in all required fields.");
      return;
    }

    fetch("/api/account/createAccount", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
        .then((response) => {
          if (response.ok) {
            console.log("Account created successfully!");
            return response.json();
          } else if (response.status === 409) {
            setError("Username is already taken. Please choose a different username.");
            throw new Error("Username is already taken.");
          } else {
            setError("Failed to create account.");
            throw new Error("Failed to create account.");
          }
        })
        .then((userData) => {
          updateUser(userData);
          Cookies.set('userData', JSON.stringify(userData));
          history.push('/feed');
        })
        .catch((error) => {
          console.error("Error:", error);
          setError('Error creating account. Please try again later.');
        });
  };

  return (
      <div>
        <AccountBoxIcon className="button-common" onClick={() => setIsVisible(!isVisible)} style={{ width: '50px', height: 'auto', background: 'none', color: 'white' }} />
        {isVisible && (
            <div className={styles.overlay}>
              <div className={styles.createAccountFormContainer}>
                <button className={styles.closeButton} onClick={() => setIsVisible(false)} style={buttonStyle}>X</button>
                <h2>Create Account</h2>
                <form onSubmit={handleSubmit} className="createAccountForm">
                  <div className={styles.inputGroup}>
                    <label>First Name:</label>
                    <input type="text" name="firstname" value={formData.firstname} onChange={handleChange} />
                  </div>
                  <div className={styles.inputGroup}>
                    <label>Last Name:</label>
                    <input type="text" name="lastname" value={formData.lastname} onChange={handleChange} />
                  </div>
                  <div className={styles.inputGroup}>
                    <label>User Name:</label>
                    <input type="text" name="username" value={formData.username} onChange={handleChange} />
                  </div>
                  <div className={styles.inputGroup}>
                    <label>Email:</label>
                    <input type="text" name="email" value={formData.email} onChange={handleChange} />
                  </div>
                  <div className={styles.inputGroup}>
                    <label>Password:</label>
                    <input type="password" name="password" value={formData.password} onChange={handleChange} />
                  </div>
                  <div className={styles.buttonContainer}>
                    <button type="submit" style={buttonStyle}>Submit</button>
                  </div>
                  {error && <p style={{ color: 'red' }}>{error}</p>}
                </form>
              </div>
            </div>
        )}
      </div>
  );
};

export default CreateAccount;