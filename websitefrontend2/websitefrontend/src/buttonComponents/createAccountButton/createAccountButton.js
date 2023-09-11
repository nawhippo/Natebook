import React, { useState } from "react";
import { useHistory } from 'react-router-dom';
import Cookies from 'js-cookie';
import { useUserContext } from "../../pages/usercontext/UserContext";
import './createAccount.css';

const CreateAccount = () => {
  const [isVisible, setIsVisible] = useState(false); 
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    email: "",
    password: "",
    username: "",
  });

  const { setUser } = useUserContext();
  const history = useHistory();

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevFormData) => ({ ...prevFormData, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!formData.firstname || !formData.lastname || !formData.username || !formData.email || !formData.password) {
      console.error("Please fill in all required fields.");
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
        console.error("Username is already taken. Please choose a different username.");
        throw new Error("Username is already taken.");
      } else {
        console.error("Failed to create account.");
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
    });
  };

  return (
    <div>
      <button onClick={() => setIsVisible(!isVisible)}>Create Account</button> {/* Toggle button */}
      {isVisible && ( // Conditionally render form
        <form onSubmit={handleSubmit} className="createAccountForm">
          <div>
            <label>
              First Name:
              <input
                type="text"
                name="firstname"
                value={formData.firstname}
                onChange={handleChange}
              />
            </label>
            <br />
            <label>
              Last Name:
              <input
                type="text"
                name="lastname"
                value={formData.lastname}
                onChange={handleChange}
              />
            </label>
            <br />
            <label>
              User Name:
              <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
              />
            </label>
            <br />
            <label>
              Email:
              <input
                type="text"
                name="email"
                value={formData.email}
                onChange={handleChange}
              />
            </label>
            <br />
            <label>
              Password:
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
              />
            </label>
          </div>
          <button type="submit">Create Account</button>
        </form>
      )}
    </div>
  );
};

export default CreateAccount;