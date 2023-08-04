import React, { useState } from "react";

const CreateAccount = () => { // Changed function name to start with uppercase
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    email: "",
    password: "",
    username: "",
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevFormData) => ({ ...prevFormData, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    fetch("/api/createAccount", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then((response) => {
        if (response.ok) {
          console.log("Account created successfully!");
        } else if (response.status === 409) {

          console.error("Username is already taken. Please choose a different username.");
        } else {

          console.error("Failed to create account.");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    setFormData({
      firstname: "",
      lastname: "",
      email: "",
      password: "",
      username: "",
    });
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <label>
            First Name:
            <input
              type="text"
              name="firstname"
              value={formData.firstname}
              onChange={handleChange}
            />
            <br></br>
          </label>
          <label>
            Last Name:
            <input
              type="text"
              name="lastname"
              value={formData.lastname}
              onChange={handleChange}
            />
              <br></br>
          </label>
          <label>
            Email:
            <input
              type="text"
              name="email"
              value={formData.email}
              onChange={handleChange}
            />
          </label>
          <br></br>
          <label>
            User Name:
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
            />
          </label>
          <br></br>
          <label>
            Password:
            <input
              type="text"
              name="password"
              value={formData.password}
              onChange={handleChange}
            />
          </label>
        </div>
        <button type="submit">Create Account</button>
      </form>
    </div>
  );
};

export default CreateAccount;