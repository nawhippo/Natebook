    import React, { useEffect, useState } from 'react';
    import './MessagesPage.css';
    import { useLocation } from 'react-router-dom';
    import { useUserContext } from '../usercontext/UserContext';
    import Message from '../objects/message';
    import ProfilePictureComponent from '../../buttonComponents/ProfilePictureComponent';
    import { fetchWithJWT } from "../../utility/fetchInterceptor";
    import CreateMessageForm from "../../buttonComponents/createMessageButton/createMessageButton";
    import ReplyToThreadForm from "../../buttonComponents/replyToThreadForm/ReplyToThreadForm";
    import ThreadNotification from "../../buttonComponents/MessageThreadNotification/MessageThreadNotification";
    const MessagesPage = ({ recipientUsername }) => {
        console.log("Recipient name: " + recipientUsername);
        const [userId, setUserId] = useState(1);
        const [threads, setThreads] = useState([]);
        const [selectedThreadId, setSelectedThreadId] = useState(null);
        const [messages, setMessages] = useState([]);
        const { user } = useUserContext();
        const location = useLocation();
        const [currentThreadParticipants, setCurrentThreadParticipants] = useState([]);
        const [localRecipientUsername, setLocalRecipientUsername] = useState("");
        const [threadNotifications, setThreadNotifications] = useState({});




        const getAllThreads = async () => {
            try {
                const response = await fetchWithJWT(`/api/message/${user.appUserID}/getAllThreads`);
                const data = await response.json();
                const newThreadNotifications = {};
                await Promise.all(data.map(async (thread) => {
                    const count = await getNotificationsCountByThread(thread.id);
                    newThreadNotifications[thread.id] = count;
                }));
                setThreads(data);
                setThreadNotifications(newThreadNotifications);
            } catch (error) {
                console.error('Error fetching threads:', error);
            }
        };

        const handleThreadClick = async (threadId) => {
            setSelectedThreadId(threadId);
            await getAllMessagesByThread(threadId);
        };

        const profilePicStyle = {
            width: '30px',
            height: '30px',
            position: 'relative',
            zIndex: 1,
        };

        const overlapStyle = {
            ...profilePicStyle,
            marginLeft: '-30px',
        };

        const sentMessageStyle = {
            textAlign: 'right',
            backgroundColor: '#ADD8E6',
            color: 'black',
            padding: '10px',
            borderRadius: '10px',
            margin: '5px 0',
            maxWidth: '80%',
            alignSelf: 'flex-end',
        };

        const receivedMessageStyle = {
            textAlign: 'left',
            backgroundColor: '#f0f0f0',
            color: 'black',
            padding: '10px',
            borderRadius: '10px',
            margin: '5px 0',
            maxWidth: '80%',
            alignSelf: 'flex-start',
        };
        const getAllMessagesByThread = async (threadId) => {
            try {
                const response = await fetchWithJWT(`/api/message/${threadId}/getAllMessages`);
                const data = await response.json();
                setMessages(data);
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        };

        const getNotificationsCountByThread = async (threadId) => {
            try {
                const response = await fetchWithJWT(`/api/message/${threadId}/getThreadNotificationsCount`);
                const notifications = await response.json();
                return notifications.length;
            } catch (error) {
                console.error('Error fetching notifications:', error);
                return 0;
            }
        };


        useEffect(() => {
            getAllThreads();
            console.log("Location object:", location.state);
            if (location.state && location.state.recipientUsername) {
                console.log("Setting localRecipientUsername:", location.state.recipientUsername);
                setLocalRecipientUsername(location.state.recipientUsername);
            }
        }, [location, location.state]);


        useEffect(() => {
            let interval;

            if (selectedThreadId) {
                const currentThread = threads.find(thread => thread.id === selectedThreadId);
                if (currentThread) {
                    setCurrentThreadParticipants(currentThread.participantsNames);
                    getAllMessagesByThread(selectedThreadId);
                }

                interval = setInterval(() => {
                    getAllMessagesByThread(selectedThreadId);
                }, 5000);
            }

            return () => {
                if (interval) {
                    clearInterval(interval);
                }
            };
        }, [selectedThreadId]);
    
        const formatParticipants = (participants) => {
    
            return participants.filter(name => name !== user.username).join(', ');
        };
    
        return (
            <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', padding: '20px' }}>
                <ul style={{ listStyleType: 'none', padding: 0, margin: 0, width: '20%' }}>
                    {threads && threads.length > 0 ? (
                        threads.map((thread) => (
                            <div key={thread.id} className="profile-picture-container" onClick={() => handleThreadClick(thread.id)}>

                                {thread.participants
                                    .filter(participant => participant !== user.appUserID)
                                    .map((participant, index) => {
                                        const isNotFirst = index > 0;
                                        return (
                                            <ProfilePictureComponent
                                                key={participant}
                                                userid={participant}
                                                style={isNotFirst ? overlapStyle : profilePicStyle}
                                            />
                                        );
                                    })}
                                <div style={{ position: 'relative', width: '100%', height: '100%' }}>
                                    <div style={{
                                        position: 'absolute',
                                        top: -40,
                                        right: -15,
                                    }}>
                                        <ThreadNotification threadId={thread.id} />
                                    </div>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p>No threads available.</p>
                    )}
                </ul>
                {selectedThreadId && (
                    <div style={{ width: '75%', marginLeft: '5%' }}>
                        <h3 style={{textAlign: "center"}}>Conversation with: {formatParticipants(currentThreadParticipants)}</h3>
                        <ul style={{ listStyleType: 'none', padding: 0 }}>
                            {messages && messages.length > 0 ? (
                                messages.map((message) => (
                                    <Message
                                        key={message.id}
                                        message={message}
                                        style={message.senderid === user.appUserID ? sentMessageStyle : receivedMessageStyle}
                                    />
                                ))
                            ) : (
                                <p>No messages available for this thread.</p>
                            )}
                        </ul>
                        <ReplyToThreadForm threadId={selectedThreadId} />
                    </div>
                )}
                <div style={{ width: '25%' }}>
                    <CreateMessageForm recipientUsername={localRecipientUsername} />
                </div>
            </div>
        );
    };
    
    export default MessagesPage;