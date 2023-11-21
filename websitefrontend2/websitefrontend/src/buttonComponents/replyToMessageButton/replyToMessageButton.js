import React, { useState } from 'react';

const ReplyToMessageForm = ({ userId, prefillData, onClose }) => {
  const [messageData, setMessageData] = useState({
    title: prefillData?.title || '',
    body: '',
    groupChatId: prefillData?.groupChatId,
    participants: prefillData?.participants || [],
  });

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!messageData.participants.includes(userId)) {
      messageData.participants.push(userId);
    }

    try {
      const response = await fetch('/api/message/send', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(messageData),
      });
      if (response.ok) {
        alert('Reply sent successfully');
        onClose();
      } else {
        alert('Failed to send reply');
      }
    } catch (error) {
      console.error('Error sending reply:', error);
      alert('Error sending reply');
    }
  };

  return (
      <form onSubmit={handleSubmit}>
        <h3>Reply to Message</h3>
        <input
            type="text"
            placeholder="Title"
            value={messageData.title}
            onChange={(e) => setMessageData({ ...messageData, title: e.target.value })}
        />
        <textarea
            placeholder="Body"
            value={messageData.body}
            onChange={(e) => setMessageData({ ...messageData, body: e.target.value })}
        />
        <button type="submit">Send Reply</button>
        <button type="button" onClick={onClose}>Cancel</button>
      </form>
  );
};

export default ReplyToMessageForm;