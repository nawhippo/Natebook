import React, { useState } from "react";
import { useUserContext } from "../../login/UserContext";

const CreateMessage = () => {
  const { user } = useUserContext();
  const [formData, setFormData] = useState({
    recipients: "",
    content: "",
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
  
    const { recipients, content } = formData;
    const recipientNamesList = recipients.split(',').map(recipient => recipient.trim());
  
    // Check for missing or empty fields
    if (recipientNamesList.length === 0 || content.trim() === "") {
      console.error("Recipient names and content are required.");
      return;
    }
  
    try {
      const response = await fetch(`/api/message/${user.appUserID}/sendMessage`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ 
          senderUsername: user,
          recipientNames: recipientNamesList, 
          content }),
      });
  
      if (response.ok) {
        console.log("Message created successfully!");
      } else {
        console.error("Failed to send message.");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  
    setFormData({
      recipients: "",
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
        </div>
        <div>
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