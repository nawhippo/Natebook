import React, { useState } from 'react';
import PublicFeed from './components/publicFeed';
import FriendFeed from './components/friendFeed';

const FeedPage = ({ userContext }) => {
    const [showFriendFeed, setShowFriendFeed] = useState(false);

    return (
        <div>
            {userContext && (
                <>
                    <button onClick={() => setShowFriendFeed(false)}>All Posts</button>
                    <button onClick={() => setShowFriendFeed(true)}>Friend Posts</button>
                </>
            )}

            {showFriendFeed && userContext ? <FriendFeed /> : <PublicFeed />}
        </div>
    );
};

export default FeedPage;