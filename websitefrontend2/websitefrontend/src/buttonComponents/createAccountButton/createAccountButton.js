import React, { useState } from "react";
import { useHistory } from 'react-router-dom';
import Cookies from 'js-cookie';
import { useUserContext } from "../../pages/usercontext/UserContext";
import styles from './createAccount.module.css';

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

  const { setUser } = useUserContext();
  const history = useHistory();

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevFormData) => ({ ...prevFormData, [name]: value }));
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
          setUser(userData);
          Cookies.set('userData', JSON.stringify(userData));
          history.push('/home');
        })
        .catch((error) => {
          console.error("Error:", error);
          setError('Error creating account. Please try again later.');
        });
  };

  return (
      <div>
        <button onClick={() => setIsVisible(!isVisible)}>Create Account</button>
        {isVisible && (
            <div className={styles.overlay}>
              <div className={styles.createAccountFormContainer}>
                <button className={styles.closeButton} onClick={() => setIsVisible(false)}>X</button>
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
                    <button type="submit">Submit</button>
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