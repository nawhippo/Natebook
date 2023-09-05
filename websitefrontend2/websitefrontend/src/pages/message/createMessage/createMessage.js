import React, { useEffect, useState } from "react";
import { useUserContext } from "../../usercontext/UserContext";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
const CreateMessage = () =>{
  const { user } = useUserContext();
  const[messageContent, setmessageContent] = useState(``);
  const [messageTitle, setMessageTitle] = useState("");
  const[recipients, setrecipientData] = useState(``);
  const location = useLocation();
  const recipientUsername = location.state?.recipient;
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    if (recipientUsername) {
      setrecipientData(recipientUsername); 
    }
  }, [recipientUsername]); 

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
    const recipientNames = recipients ? recipients.split(',').map(recipient => recipient.trim()) : [];
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
      console.error('Fetch error:', error);
      setSuccessMessage('');
      setErrorMessage("Couldn't send the message. Please try again.");
    });
  };



  return (
    <div>
      <h1>Send Message</h1>

      <div>
        <label>Recipients (comma-seperated):</label>
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
      {/* display upon successfully send a message.*/}
      {successMessage && <p>{successMessage}</p>}
      {/* display upon failure to send message.*/}
      {errorMessage && <p style={{color: 'red'}}>{errorMessage}</p>} 
    </div>
  );
}

export default CreateMessage;