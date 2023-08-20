import React, { useEffect, useState } from "react";
import { useUserContext } from "../../login/UserContext";
import { useLocation } from "react-router-dom/cjs/react-router-dom.min";
const CreateMessage = () =>{
  const { user } = useUserContext();
  const[messageContent, setmessageContent] = useState(``);
  const[recipients, setrecipientData] = useState(``);
  const location = useLocation();
  const recipientUsername = location.state?.recipient;



  useEffect(() => {
    if (recipientUsername) {
      setrecipientData(recipientUsername); // Optionally set recipients state with recipientUsername
    }
  }, [recipientUsername]); // Run effect when recipientUsername changes

  const handleRecipientsChange = (event) => {
    setrecipientData(event.target.value);
  };

  const handleMessagesChange = (event) => {
    setmessageContent(event.target.value);
  };



  const handleSendMessage = () => {
    console.log(recipients);
    const recipientNames = recipients ? recipients.split(',').map(recipient => recipient.trim()) : [];

    const requestBody = {
      content: messageContent,
      recipientNames: recipientNames,
    };
    
    fetch(`/api/message/${user.appUserID}/sendMessage`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    body: JSON.stringify(requestBody),
  })
  .then(response => {
    if (!response.ok){
      throw new Error("Network response was not ok");
    }
    return response.json();
  })
  .then(data => {

  })
  .catch(error => {
    console.error('Fetch error: ', error);
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
        <label>Message Content:</label>
        <textarea
        type="text"
        value={messageContent}
        onChange={handleMessagesChange}
        />
      </div>
      <button onClick={handleSendMessage}>Send Message</button>
    </div>
  );
}

export default CreateMessage;