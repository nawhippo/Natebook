import React from 'react';
import { useUserContext } from '../usercontext/UserContext';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";

const Message = ({ message, style }) => {
    const { user } = useUserContext();

    const isCurrentUserMessage = user.appUserID === message.senderid;

    const messageContainerBaseStyle = {
        display: "flex",
        minWidth: "300px",
        maxWidth: "70%",
        padding: "10px",
        margin: isCurrentUserMessage ? "10px 10px 10px auto" : "10px auto 10px 10px",
        borderRadius: "10px",
        border: isCurrentUserMessage ? '2px solid lightBlue' : '2px solid white',
        background: isCurrentUserMessage ? 'lightBlue' : 'gret',
        transform: isCurrentUserMessage ? 'none' : 'translateX(20%)',
    };


    const messageContainerStyle = { ...messageContainerBaseStyle, ...style };

    const profilePicStyle = {
        height: '50px',
        width: '50px',
        borderRadius: '50%',
    };

    const messageTextStyle = {
        padding: "0 10px",
    };

    return (
        <li style={messageContainerStyle}>
            <ProfilePictureComponent userid={message.senderid} style={profilePicStyle} />
            <p style={messageTextStyle}>{message.content}</p>
        </li>
    );
};

export default Message;