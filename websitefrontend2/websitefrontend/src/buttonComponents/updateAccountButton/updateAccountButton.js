import React, { useState } from "react";
import { useUserContext } from "../../pages/usercontext/UserContext";
import styles from './updateAccount.module.css';
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

    const convertToBase64 = (file) => {
        return new Promise((resolve, reject) => {
            const fileReader = new FileReader();
            fileReader.readAsDataURL(file);
            fileReader.onload = () => resolve(fileReader.result);
            fileReader.onerror = (error) => reject(error);
        });
    };

    const handleFileChange = async (event) => {
        const file = event.target.files[0];
        if (file) {
            try {
                const base64 = await convertToBase64(file);
                setBase64Image(base64.split(',')[1]); // Extract the base64 part only
            } catch (error) {
                console.error('Error converting file:', error);
                setError('Error processing file');
            }
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

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : '#FF6D00',
        color: '#FFFFFF',
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
            <button onClick={() => setIsVisible(!isVisible)} style={buttonStyle} className='button-common'>Update Account</button>
            {isVisible && (
                <div className={styles.overlay}>
                    <div className={styles.loginFormContainer}>
                        <button className={styles.closeButton} onClick={() => setIsVisible(false)} style={buttonStyle}>X</button>
                        <form onSubmit={handleSubmit} className="updateAccountForm">
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
                                />
                            </div>
                            <div className={styles.inputGroup}>
                                <label htmlFor="icon-button-file">Profile Picture:</label>
                                <input accept="image/*" id="icon-button-file" type="file" onChange={handleFileChange}/>
                            </div>
                            <div className={styles.buttonContainer}>
                                <button type="submit" className={styles.submitButton}   className='button-common' style={buttonStyle}>Submit</button>
                            </div>
                        </form>
                        {message && <p>{message}</p>}
                        {error && <p>{error}</p>}
                    </div>
                </div>
            )}
        </div>
    );
};
export default UpdateAccountButton;