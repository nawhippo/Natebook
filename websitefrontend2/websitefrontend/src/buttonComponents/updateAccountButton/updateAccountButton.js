import React, { useState } from "react";
import { useUserContext } from "../../pages/usercontext/UserContext";

const UpdateAccountButton = () => {
  const {user} = useUserContext();
  const [isVisible, setIsVisible] = useState(false);
  const [formData, setFormData] = useState({
    firstname: user.firstname,
    lastname: user.lastname,
    email: user.email,
    password: "",
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState("");

  const handleChange = (event) => {
    const {name, value} = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const clearFields = () => {
    setFormData({
      firstname: "",
      lastname: "",
      email: "",
      password: "",
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    setError(null);
    setMessage("");

    try {
      const response = await fetch(`/api/account/${user.appUserID}/updateAccountDetails`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      console.log("Account updated successfully!");
      setMessage("Account Updated!");
    } catch (error) {
      console.error("Error:", error);
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
    clearFields();
  };

  return (
      <div>
        <button onClick={() => setIsVisible(!isVisible)}>Update Account</button>
        {isVisible && (
            <div>
              <form onSubmit={handleSubmit}>
                <div>
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
                <div>
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
                <div>
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
                <div>
                  <label htmlFor="password">Password:</label>
                  <input
                      type="password"
                      id="password"
                      name="password"
                      value={formData.password}
                      onChange={handleChange}
                  />
                </div>
                <button type="submit">Submit</button>
              </form>
              {message && <p>{message}</p>}
              {error && <p>{error}</p>}
            </div>
        )}
      </div>
  );
}
export default UpdateAccountButton;