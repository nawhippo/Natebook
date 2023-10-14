import React, { useState } from "react";
import { useUserContext } from "../../pages/usercontext/UserContext";

const UpdateAccountButton = () => {
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
  const [message, setMessage] = useState(""); 

  const handleChange = (event) => {
    const { name, value } = event.target;
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
      setMessage("Message Sent!"); 
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
              {/* Your input fields here */}
            </div>
            <button type="submit">Submit</button>
          </form>
          {message && <p>{message}</p>}
          {error && <p>{error}</p>}
        </div>
      )}
    </div>
  );
};

export default UpdateAccountButton;