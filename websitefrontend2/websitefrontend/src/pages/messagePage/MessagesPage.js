import React, { useEffect, useState } from 'react';
import { useUserContext } from '../usercontext/UserContext';
import Message from '../objects/message';
import ReplyMessageForm from '../../buttonComponents/replyToMessageButton/replyToMessageButton';

const MessagesPage = () => {
  const { user } = useUserContext();
  const [showIncoming, setShowIncoming] = useState(true);
  const [searchText, setSearchText] = useState('');
  const [allMessages, setAllMessages] = useState([]);
  const [displayedMessages, setDisplayedMessages] = useState([]);

  useEffect(() => {
    fetch(`/api/message/${user.appUserID}/allMessages`)
      .then((response) => response.json())
      .then((data) => {
        setAllMessages(data);
        setDisplayedMessages(data);
      })
      .catch((error) => console.error('Error fetching messages:', error));
  }, [user]);

  const handleSearchTextChange = (event) => {
    const newSearchText = event.target.value;
    setSearchText(newSearchText);
    filterMessages(newSearchText);
  };

  const filterMessages = (textToSearch) => {
    const filteredMessages = allMessages.filter((message) =>
      message.content.toLowerCase().includes(textToSearch.toLowerCase()) ||
      (message.title && message.title.toLowerCase().includes(textToSearch.toLowerCase())) ||
      (message.recipients && message.recipients.join(', ').toLowerCase().includes(textToSearch.toLowerCase()))
    );
    setDisplayedMessages(filteredMessages);
  };

  const messagesToDisplay = showIncoming 
    ? displayedMessages.filter((message) => message.isIncoming)
    : displayedMessages.filter((message) => !message.isIncoming);

  return (
    <div>
      <h1>Messages Page</h1>
      <input
        type="text"
        value={searchText}
        onChange={handleSearchTextChange}
        placeholder="Search Messages"
      />
      <button onClick={() => setShowIncoming(!showIncoming)}> 
        {showIncoming ? 'Show Sent Messages' : 'Show Incoming Messages'} 
      </button>
      <h2>{showIncoming ? 'Incoming Messages:' : 'Sent Messages:'}</h2>
      <ul>
        {messagesToDisplay.map((message) => (
          <li key={message.id}>
            <Message message={message} user={user} />
            <ReplyMessageForm userId={user.appUserID} parentSenderId={message.senderId} messageId={message.id} />  {/* <-- Add ReplyMessageForm */}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MessagesPage;