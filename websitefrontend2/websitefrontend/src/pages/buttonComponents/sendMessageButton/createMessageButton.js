import React, { useEffect, useState } from "react";
import { useUserContext } from "../../usercontext/UserContext";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";

const SendMessageButton = ({ recipient }) => {
  const { user } = useUserContext();
  const [showForm, setShowForm] = useState(false);
  const [messageContent, setmessageContent] = useState("");
  const [messageTitle, setMessageTitle] = useState("");
  const [recipients, setrecipientData] = useState("");
  const location = useLocation();
  const recipientUsername = location.state?.recipient;
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    if (recipient) {
      setrecipientData(recipient);
    }
  }, [recipient]);

  const toggleForm = () => {
    setShowForm(!showForm);
  };

  const handleRecipientsChange = (event) => {
    setrecipientData(event.target.value);
  };

  const handleMessagesChange = (event) => {
    setmessageContent(event.target.value);
  };

  const handleTitleChange = (event) => {
    setMessageTitle(event.target.value);
  };

  const handleSendMessage = () => {
    const recipientNames = recipients ? recipients.split(',').map(r => r.trim()) : [];
    const requestBody = { content: messageContent, recipientNames };
    
    fetch(`/api/message/${user.appUserID}/sendMessage`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(requestBody),
    })
    .then(response => {
      if (!response.ok) throw new Error("Network response was not ok");
      return response.json();
    })
    .then(data => {
      setmessageContent('');
      setMessageTitle('');
      setrecipientData('');
      setSuccessMessage('Message sent.');
      setErrorMessage('');
    })
    .catch(error => {
      setSuccessMessage('');
      setErrorMessage("Couldn't send the message. Please try again.");
    });
  };

  return (
    <div>
      <button onClick={toggleForm}>Compose Message</button>
      {showForm && (
        <div>
          <h1>Send Message</h1>
          <div>
            <label>Recipients (comma-separated):</label>
            <input
              type="text"
              value={recipients}
              onChange={handleRecipientsChange}
            />
          </div>
          <div>
            <label>Message Title:</label>
            <input
              type="text"
              value={messageTitle}
              onChange={handleTitleChange}
            />
          </div>
          <div>
            <label>Message Content:</label>
            <textarea
              type="text"
              value={messageContent}
              onChange={handleMessagesChange}
            />
          </div>
          <button onClick={handleSendMessage}>Send Message</button>
          {successMessage && <p>{successMessage}</p>}
          {errorMessage && <p style={{color: 'red'}}>{errorMessage}</p>}
        </div>
      )}
    </div>
  );
};

export default SendMessageButton;