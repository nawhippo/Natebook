import React, { useState } from 'react';
import { user, useUserContext } from '../usercontext/UserContext';
import GroupChats from './HelperComponents/GroupChats'; // Adjust the path as necessary
import ReplyToMessageForm from '../../buttonComponents/replyToMessageButton/replyToMessageButton'; // Adjust the path as necessary

const MessagesPage = () => {
    const { user } = useUserContext();
    const [showReplyForm, setShowReplyForm] = useState(false);
    const [prefillData, setPrefillData] = useState({});

    const handleReply = (message) => {
        setShowReplyForm(true);
        setPrefillData({
            groupChatId: message.groupChatId,
            participants: message.participants
        });
    };

    return (
        <div>
            <h1>Messages Page</h1>
            {showReplyForm && (
                <ReplyToMessageForm
                    userId={user.appUserID}
                    prefillData={prefillData}
                    onClose={() => setShowReplyForm(false)}
                />
            )}
            <GroupChats onReply={handleReply} />
        </div>
    );
};

export default MessagesPage;