import React, { useEffect, useState } from 'react';
import { useUserContext } from '../usercontext/UserContext';
import Message from '../objects/message';
import ReplyMessageForm from '../../buttonComponents/replyToMessageButton/replyToMessageButton';
import CreateMessageForm from '../../buttonComponents/createMessageButton/createMessageButton';
import SentList from './HelperComponents/OutgoingMessages';
import RecList from './HelperComponents/IncomingMessages';
import SearchIcon from '@mui/icons-material/Search';
import AddIcon from '@mui/icons-material/Add';
import '../../global.css';
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

    const handleSearch = () => {
        filterMessages(searchText, allMessages);
    };

    return (
        <div>
            <h1>Messages</h1>
            <div className="search-bar-container"> {/* Use the same container class for consistent styling */}
                <input
                    className="search-input" // Apply the search input class
                    type="text"
                    placeholder="Search messages..."
                    value={searchText}
                    onChange={handleSearchTextChange}
                />
                <button className="search-button" onClick={handleSearch}> {/* Apply the search button class */}
                    <SearchIcon />
                </button>
            </div>

            <CreateMessageForm userId={user.appUserID}><AddIcon /></CreateMessageForm>

            <button onClick={() => setShowIncoming(!showIncoming)}>
                {showIncoming ? 'Sent Messages' : 'Incoming Messages'}
            </button>
            {showIncoming ? (
                <>
                    <h2>Incoming Messages:</h2>
                    <RecList messages={displayedMessages.filter(message => message.isIncoming)} />
                </>
            ) : (
                <>
                    <h2>Sent Messages:</h2>
                    <SentList messages={displayedMessages.filter(message => !message.isIncoming)} />
                </>
            )}
        </div>
    );
}

export default MessagesPage;