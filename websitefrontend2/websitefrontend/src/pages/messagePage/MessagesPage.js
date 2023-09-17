import React, { useEffect, useState } from 'react';
import { useUserContext } from '../usercontext/UserContext';
import Message from '../objects/message';
import ReplyMessageForm from '../../buttonComponents/replyToMessageButton/replyToMessageButton';
import CreateMessageForm from '../../buttonComponents/createMessageButton/createMessageButton';

const MessagesPage = () => {
  const { user } = useUserContext();
  const [searchText, setSearchText] = useState('');
  const [showIncoming, setShowIncoming] = useState(true);
  const [allMessages, setAllMessages] = useState([]);
  const [displayedMessages, setDisplayedMessages] = useState([]);

  useEffect(() => {
    fetch(`/api/message/${user.appUserID}/allMessages`)
      .then((response) => response.json())
      .then((data) => {
        setAllMessages(data);
        filterMessages(searchText, data);
      })
      .catch((error) => console.error('Error fetching messages:', error));
  }, [user]);

  const handleSearchTextChange = (event) => {
    const textToSearch = event.target.value;
    setSearchText(textToSearch);
    filterMessages(textToSearch, allMessages);
  };

  const filterMessages = (textToSearch, messages) => {
    const filtered = messages.filter((message) => {
      return message.content.toLowerCase().includes(textToSearch.toLowerCase());
    });
    setDisplayedMessages(filtered);
  };

  const messagesToDisplay = displayedMessages.filter((message) => {
    return showIncoming ? message.isIncoming : !message.isIncoming;
  });

  return (
    <div>
      <h1>Messages Page</h1>
      <CreateMessageForm userId={user.appUserID}></CreateMessageForm>
      <input 
        type="text"
        placeholder="Search messages..."
        value={searchText}
        onChange={handleSearchTextChange}
      />
      <button onClick={() => setShowIncoming(!showIncoming)}>
        {showIncoming ? 'Show Sent Messages' : 'Show Incoming Messages'}
      </button>
      <h2>{showIncoming ? 'Incoming Messages:' : 'Sent Messages:'}</h2>
      <ul>
        {messagesToDisplay.map((message) => (
          <li key={message.id}>
            <Message message={message} user={user} />
            <ReplyMessageForm messageId={message.id} posterId={message.senderid} userId={user.appUserID}/>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MessagesPage;