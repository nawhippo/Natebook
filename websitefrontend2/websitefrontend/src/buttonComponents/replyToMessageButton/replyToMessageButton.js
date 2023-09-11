import React, { useState } from 'react';

const ReplyMessageForm = ({ userId, parentSenderId, messageId }) => {
  const [showForm, setShowForm] = useState(false);
  const [content, setContent] = useState('');
  const [recipientNames, setRecipientNames] = useState('');

  const handleToggle = () => setShowForm(!showForm);

  const handleSubmit = () => {
    fetch(`/api/message/${userId}/${parentSenderId}/${messageId}/replyMessage`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content, recipientNames: recipientNames.split(',') }),
    })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error('Failed to send reply');
    })
    .then((data) => {
      console.log('Reply sent:', data);
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  };

  return (
    <div>
      <button onClick={handleToggle}>Reply to Message</button>
      
      {showForm && (
        <div>
          <input 
            type="text" 
            value={content} 
            onChange={(e) => setContent(e.target.value)}
            placeholder="Content" 
          />
          <input 
            type="text" 
            value={recipientNames} 
            onChange={(e) => setRecipientNames(e.target.value)}
            placeholder="Recipients (comma separated)" 
          />
          <button onClick={handleSubmit}>Send Reply</button>
        </div>
      )}
    </div>
  );
};

export default ReplyMessageForm;