import React, { useState } from "react";

const createAccount = () => {
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

    //perform the fetch request here to send the form data to the server
    fetch("/createAccount", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then((response) => {
        if (response.ok) {
          //handle success
          console.log("Account created successfully!");
        } else {
          //handle error
          console.error("Failed to create account.");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    //clear the form fields after submission
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
          </label>
          <label>
            Last Name:
            <input
              type="text"
              name="lastname"
              value={formData.lastname}
              onChange={handleChange}
            />
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
          <label>
            User Name:
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
            />
          </label>
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

export default createAccount;