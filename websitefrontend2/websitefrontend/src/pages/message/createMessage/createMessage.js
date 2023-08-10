import React, { useState } from "react";
import { useUserContext } from "../../login/UserContext";

const CreateMessage = () => {
  const { userId } = useUserContext();
  const [formData, setFormData] = useState({
    recipients: [],
    content: "",
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    const { recipients, content } = formData;
    fetch(`/users/${userId}/sendMessage`, { 
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ recipients, content }),
    })
      .then((response) => {
        if (response.ok) {
      
          console.log("Message created successfully!");
        } else {
        
          console.error("Failed to send message.");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
      });
    setFormData({
      recipients: [],
      content: "",
    });
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div>
          <label>
            Recipients (comma-separated):
            <input
              type="text"
              name="recipients"
              value={formData.recipients}
              onChange={handleChange}
            />
          </label>
          <label>
            Content:
            <textarea
              name="content"
              value={formData.content}
              onChange={handleChange}
            />
          </label>
        </div>
        <button type="submit">Send Message</button>
      </form>
    </div>
  );
};

export default CreateMessage;