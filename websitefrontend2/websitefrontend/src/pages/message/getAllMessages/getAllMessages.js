import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../login/UserContext';

const GetAllMessages = ({ targetUsername }) => {
  const { user } = useUserContext(); 
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    fetch(`/api/${user.appUserID}/messagesByUsername/${targetUsername}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => setMessages(data))
      .catch(error => console.error('Error fetching messages:', error));
  }, [user, targetUsername]);

  return (
    <div>
      <h2>All Messages:</h2>
      {messages.length > 0 ? (
        <ul>
          {messages.map(message => (
            <li key={message.id}>
              <p>{message.content}</p>
              <p>Sender: {message.sender.name}</p>
              <p>Recipients: {message.recipients.map(user => user.name).join(', ')}</p>
            </li>
          ))}
        </ul>
      ) : (
        <p>No messages found.</p>
      )}
    </div>
  );
};

const MessagesPage = () => {
  const { userId } = useUserContext();
  const [targetUsername, setTargetUsername] = useState('');
  
  const handleUsernameChange = (event) => {
    setTargetUsername(event.target.value);
  };

  return (
    <div>
      <h1>Messages Page</h1>
      

      <input
        type="text"
        value={targetUsername}
        onChange={handleUsernameChange}
        placeholder="Enter username"
      />

      <GetAllMessages targetUsername={targetUsername} />
    </div>
  );
};

export default MessagesPage;