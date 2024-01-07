import React, {useEffect, useState} from 'react';
import './MessagesPage.css';
import {useUserContext} from '../usercontext/UserContext';
import Message from '../objects/message';
import ProfilePictureComponent from '../../buttonComponents/ProfilePictureComponent';
import {fetchWithJWT} from "../../utility/fetchInterceptor";

const MessagesPage = () => {
    const [userId, setUserId] = useState(1);
    const [content, setContent] = useState('');
    const [recipientUsernames, setRecipientUsernames] = useState([]);
    const [threads, setThreads] = useState([]);
    const [selectedThreadId, setSelectedThreadId] = useState(null);
    const [messages, setMessages] = useState([]);
    const {user} = useUserContext();

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'grey',
        color: '#FFFFFF',
        border: '4px solid black',
    };
    const getAllThreads = async () => {
        try {
            const response = await fetchWithJWT(`/api/message/${user.appUserID}/getAllThreads`);
            const data = await response.json();
            setThreads(data);
        } catch (error) {
            console.error('Error fetchwithCsrfing threads:', error);
        }
    };

    const getAllMessagesByThread = async () => {
        try {
            const response = await fetchWithJWT(`/api/message/${selectedThreadId}/getAllMessages`);
            const data = await response.json();
            setMessages(data);
        } catch (error) {
            console.error('Error fetchwithCsrfing messages:', error);
        }
    };

    const sendMessage = async () => {
        try {
            const response = await fetchWithJWT(`/api/message/${user.appUserID}/sendMessage`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    content,
                    recipientUsernames,
                }),
            });

            if (response.ok) {
                setContent('');
                setRecipientUsernames([]);
                getAllThreads();
            } else {
                console.error('Error sending message:', response.statusText);
            }
        } catch (error) {
            console.error('Error sending message:', error);
        }
    };

    const replyToThread = async () => {
        try {
            await fetchWithJWT(`/api/message/${selectedThreadId}/reply`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    content,
                    userId: Number(userId),
                }),
            });
            getAllMessagesByThread(selectedThreadId);
        } catch (error) {
            console.error('Error replying to thread:', error);
        }
    };

    useEffect(() => {
        getAllThreads();
    }, [userId]);

    useEffect(() => {
        if (selectedThreadId) {
            getAllMessagesByThread(selectedThreadId);
        }
    }, [selectedThreadId]);

    return (
            <div className="messages-container">
                <div className="threads">
                    <ul style={{ listStyleType: 'none', padding: 0 }}>
                        {threads && threads.length > 0 ? (
                            threads.map((thread) => (
                                <li key={thread.id}>
                                    <button
                                        onClick={() => setSelectedThreadId(thread.id)}
                                        style={buttonStyle}
                                        className='button-common'
                                    >
                                        {thread.participants.map((participant) => (
                                            <span key={participant}>
                                            <p>{participant}</p>
                                            <ProfilePictureComponent userid={participant.userId} sx={{ size: '100px !important' }}/>{' '}
                                        </span>
                                        ))}
                                    </button>
                                </li>
                            ))
                        ) : (
                            <p>No threads available.</p>
                        )}
                    </ul>
                </div>
                {selectedThreadId && (
                    <div className="messages-container">
                        <div className="user-messages">
                            <ul style={{ listStyleType: 'none', padding: 0 }}>
                                {messages && messages.length > 0 ? (
                                    messages.map((message) => (
                                        <li key={message.id}>
                                            {message.senderId === userId ? (
                                                <div className="my-message">
                                                    <Message message={message} />
                                                    <p>{message.content}</p>
                                                </div>
                                            ) : (
                                                <div className="other-person-message">
                                                    {message.senderId !== userId && (
                                                        <ProfilePictureComponent userid={message.senderId} />
                                                    )}
                                                    <Message message={message} />
                                                    <p>{message.content}</p>
                                                </div>
                                            )}
                                        </li>
                                    ))
                                ) : (
                                    <p>No messages available for this thread.</p>
                                )}
                            </ul>
                        </div>
                    </div>
                )}
            <div className="send-message" style={{backgroundColor: 'ghostwhite', marginTop: '30px', width: '400px'}}>
                <h2>Create a new Thread</h2>
                <div>
                    <input
                        style={{width: '300px', height: '40px'}}
                        type="text"
                        placeholder="Enter Recipient Usernames"
                        value={recipientUsernames.join(',')}
                        onChange={(e) => setRecipientUsernames(e.target.value.split(','))}
                    />
                </div>
                <div>
                    <br/>
                    <textarea
                        style={{ width: '300px', height: '200px' }}
                        placeholder="Enter your message"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                    />
                    <br />
                    <button
                        onClick={sendMessage}
                        style={{
                            backgroundColor: user && user.backgroundColor ? user.backgroundColor : '#FF6D00',
                            border: '1px solid #ccc',
                        }}
                        className='button-common'
                        style={buttonStyle}
                    >
                        Send
                    </button>
                </div>
            </div>
        </div>
    );
};

export default MessagesPage;