import React from 'react';

const Message = ({ message, user }) => {
  return (
    <div>
      <p>{message.title}</p>
      <p>{message.content}</p>
      <p>Sender: {message.incoming ? user.username : 'You'}</p>
      {message.recipients && (
        <p>Recipients: {message.recipients.join(', ')}</p>
      )}
    </div>
  );
};

export default Message;