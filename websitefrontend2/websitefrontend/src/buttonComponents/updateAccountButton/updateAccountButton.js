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
  const [base64Image, setBase64Image] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState("");

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        if (typeof reader.result === 'string') {
          setBase64Image(reader.result);
        } else {
          console.error('FileReader result is not a string');
          setError('Error reading file');
        }
      };
      reader.readAsDataURL(file);
    }
  };

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
    setBase64Image('');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    setError(null);
    setMessage("");

    try {
      // Update account details
      const accountResponse = await fetch(`/api/account/${user.appUserID}/updateAccountDetails`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (!accountResponse.ok) {
        throw new Error("Network response was not ok for account update");
      }

      // Upload profile picture
      if (base64Image) {
        const pictureResponse = await fetch(`/api/account/${user.appUserID}/uploadProfilePicture`, {
          method: "PUT",
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ image: base64Image }),
        });

        if (!pictureResponse.ok) {
          throw new Error("Network response was not ok for picture upload");
        }
      }

      setMessage("Account and Picture Updated!");
    } catch (error) {
      console.error("Error:", error);
      setError(error.message);
    } finally {
      setIsLoading(false);
      clearFields();
    }
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
                <div>
                  <label htmlFor="icon-button-file">Profile Picture:</label>
                  <input accept="image/*" id="icon-button-file" type="file" onChange={handleFileChange}/>
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