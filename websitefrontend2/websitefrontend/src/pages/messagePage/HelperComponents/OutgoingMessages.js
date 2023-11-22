import React, { useState, useEffect } from 'react';
import Message from '../../objects/message';
import { useUserContext } from '../../usercontext/UserContext';
const SentList = () => {
    const { user } = useUserContext();
    const userId = user.id;
    const [messages, setMessages] = useState([]);

    useEffect(() => {

        const fetchSentMessages = async () => {
            try {
                const response = await fetch(`/api/message/${user.appUserID}/sentMessages`);
                if (response.ok) {
                    const data = await response.json();
                    setMessages(data);
                } else {
                    console.error('Failed to fetch sent messages');
                }
            } catch (error) {
                console.error('Could not fetch sent messages:', error);
            }
        };

        fetchSentMessages();
    }, [userId]);

    return (
        <div>
            {messages.length === 0 ? (
                <p>No sent messages.</p>
            ) : (
                messages.map((message, index) => (
                    <Message key={index} message={message} user={userId} />
                ))
            )}
        </div>
    );
};

export default SentList;