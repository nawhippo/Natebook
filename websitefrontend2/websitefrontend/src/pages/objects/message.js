import React from 'react';
import {useUserContext} from '../usercontext/UserContext';

const Message = ({ message }) => {
    const user = useUserContext();

    return (
        <li
            className={user.appUserID !== message.senderid ? 'other-user' : ''}
            key={message.id}
        >
            {message.content}
        </li>
    );
};

export default Message;