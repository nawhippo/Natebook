import React, { useState } from 'react';
import PublicFeed from './components/publicFeed';
import FriendFeed from './components/friendFeed';

const FeedPage = ({ userContext }) => {
    const [showFriendFeed, setShowFriendFeed] = useState(false);

    return (
        <div>
            {userContext && (
                <>
                    <button onClick={() => setShowFriendFeed(!showFriendFeed)}>Swap Feed</button>
                </>
            )}

            {showFriendFeed && userContext ? <FriendFeed /> : <PublicFeed />}
        </div>
    );
};

export default FeedPage;