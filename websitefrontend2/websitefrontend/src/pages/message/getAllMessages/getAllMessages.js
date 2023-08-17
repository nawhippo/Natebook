import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../login/UserContext';

const GetAllMessages = () => {
  const { user } = useUserContext();
  const [searchText, setSearchText] = useState('');
  const [allMessages, setAllMessages] = useState([]);
  const [displayedMessages, setDisplayedMessages] = useState([]);

  const handleSearchTextChange = (event) => {
    const searchText = event.target.value;
    setSearchText(searchText);
    filterMessages(searchText);
  };

  const filterMessages = (searchText) => {
    const filteredMessages = allMessages.filter((message) =>
      message.content.toLowerCase().includes(searchText.toLowerCase())
    );
    setDisplayedMessages(filteredMessages);
  };

  useEffect(() => {
    fetch(`/api/message/${user.appUserID}/allMessages`)
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        setAllMessages(data);
        setDisplayedMessages(data);
      })
      .catch((error) => console.error('Error fetching messages:', error));
  }, [user]);

  return (
    <div>
      <h1>Messages Page</h1>
      <input
        type="text"
        value={searchText}
        onChange={handleSearchTextChange}
        placeholder="Search messages"
      />
      <div>
        <h2>All Messages:</h2>
        {displayedMessages.length > 0 ? (
  <ul>
    {displayedMessages.map((message) => (
      <li key={message.id}>
        <p>{message.content}</p>
        {message.sender && <p>Sender: {message.sender}</p>}
        {message.recipients && message.recipients.length > 0 && (
          <p>
            Recipients: {message.recipients.map((recipient) => recipient.username).join(', ')}
          </p>
        )}
      </li>
    ))}
  </ul>
) : (
  <p>No messages found.</p>
)}
      </div>
    </div>
  );
};

export default GetAllMessages;