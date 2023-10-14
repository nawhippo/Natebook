import React, { useState } from 'react';
import PublicFeed from './components/publicFeed'; 
import FriendFeed from './components/friendFeed';

const FeedPage = () => {
  const [showFriendFeed, setShowFriendFeed] = useState(false);

  return (
    <div>
      <button onClick={() => setShowFriendFeed(false)}>All Posts</button>
      <button onClick={() => setShowFriendFeed(true)}>Friend Posts</button>

      {showFriendFeed ? <FriendFeed /> : <PublicFeed />}
    </div>
  );
};

export default FeedPage;