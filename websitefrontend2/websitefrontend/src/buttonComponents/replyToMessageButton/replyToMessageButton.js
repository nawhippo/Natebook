import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext'; // Adjust the path if needed
const ReplyMessageForm = ({ messageId, posterId }) => {
  const [showForm, setShowForm] = useState(false);
  const [content, setContent] = useState('');
  const user = useUserContext();
  
  const handleToggle = () => setShowForm(!showForm);
  
  const handleSubmit = () => {
    fetch(`/api/message/${user.appUserID}/${messageId}/replyMessage`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content }),
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
          <button onClick={handleSubmit}>Send Reply</button>
        </div>
      )}
    </div>
  );
};

export default ReplyMessageForm;