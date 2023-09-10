import React, { useState } from "react";
import { useUserContext } from "../../usercontext/UserContext";

const UpdateAccount = () => {
  const { user } = useUserContext();
  const [isVisible, setIsVisible] = useState(false); 
  const [formData, setFormData] = useState({
    firstname: user.firstname,
    lastname: user.lastname,
    email: user.email,
    password: "",
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    setError(null);

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
    } catch (error) {
      console.error("Error:", error);
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
      <button onClick={() => setIsVisible(!isVisible)}>Toggle Update Form</button>  {/* Toggle button */}
      {isVisible && (  // Conditionally render form
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
              type="text"
              name="password"
              value={formData.password}
              onChange={handleChange}
            />
          </label>
        </div>
            <button type="submit">Update Account</button>
          </form>
        </div>
      )}
    </div>
  );
};

export default UpdateAccount;