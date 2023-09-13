import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
const CreateMessageForm = ({ userId, defaultRecipientName }) => {
  const [showForm, setShowForm] = useState(false);
  const [content, setContent] = useState('');
  const [recipientNames, setRecipientNames] = useState('');

  const handleToggle = () => setShowForm(!showForm);

  const handleSubmit = () => {
    fetch(`/api/message/${userId}/sendMessage`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content, recipientNames: recipientNames.split(',') }),
    })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error('Failed to send message');
    })
    .then((data) => {
      console.log('Message sent:', data);
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  };

  useEffect(() => {
    if (defaultRecipientName){
      setRecipientNames(defaultRecipientName);
    }
  }, [defaultRecipientName]);
  
  
  return (
    <div>
      <button onClick={handleToggle}>Create Message</button>
      <br></br>
      {showForm && (
        <div>
          <input 
            type="text" 
            value={recipientNames} 
            onChange={(e) => setRecipientNames(e.target.value)}
            placeholder="Recipients (comma separated)" 
          />
          <br></br>
          <textarea 
            type="text" 
            value={content} 
            onChange={(e) => setContent(e.target.value)}
            placeholder="Content" 
          />
          <br></br>
          <button onClick={handleSubmit}>Send</button>
          <br></br>
          <br></br>
          <br></br>
        </div>
      )}
    </div>
  );
};

export default CreateMessageForm;