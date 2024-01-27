import React from 'react';
import { useUserContext } from '../usercontext/UserContext';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";

const Message = ({ message, style }) => {
    const { user } = useUserContext();

    const isCurrentUserMessage = user.appUserID === message.senderid;

    const messageContainerBaseStyle = {
        position: "relative",
        display: "flex",
        minWidth: "300px",
        maxWidth: "70%",
        padding: "10px",
        margin: isCurrentUserMessage ? "10px 10px 10px auto" : "10px auto 10px 10px",
        borderRadius: "10px",
        border: '3px solid black',
        background: isCurrentUserMessage ? 'lightBlue' : 'gret',
        transform: isCurrentUserMessage ? 'none' : 'translateX(20%)',
    };


    const messageContainerStyle = { ...messageContainerBaseStyle, ...style };

    const profilePicStyle = {
        height: '100px',
        width: '100px',
        borderRadius: '60%',

    };

    const messageTextStyle = {
        padding: "0 10px",
    };

    const formatTimestamp = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleString();
    };


    return (
        <li style={messageContainerStyle}>
            <ProfilePictureComponent userid={message.senderid} style={profilePicStyle} />
            <p style={messageTextStyle}>{message.content}</p>
            <div style={{position:"absolute",bottom:'5px', right:'5px', marginRight:'15px'}}>
            <p style={{fontSize:"10px"}}> {formatTimestamp(message.timestamp)} </p>
            </div>
        </li>
    );
};

export default Message;