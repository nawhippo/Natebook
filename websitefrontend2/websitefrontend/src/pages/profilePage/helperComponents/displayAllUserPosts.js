import React, { useEffect, useState } from 'react';
import Post from '../../objects/post';
import { useUserContext } from '../../usercontext/UserContext';
const UserPosts = ({ userid, profileUserId }) => {
  const [posts, setPosts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useUserContext();
  const fetchData = async () => {
    try {
      const response = await fetch(`/api/user/${userid}/${profileUserId}`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setPosts(data);
      setIsLoading(false);
    } catch (error) {
      setError(error.message);
      setIsLoading(false);
    }
  };

  const filteredPosts = posts.filter(post => {
    return post.content.toLowerCase().includes(searchTerm.toLowerCase());
  });


  useEffect(() => {
    fetchData();
  }, [userid, profileUserId]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

 return (
    <div>
      <h1>User Posts</h1>
      <input 
        type="text" 
        placeholder="Search posts..." 
        value={searchTerm}
        onChange={e => setSearchTerm(e.target.value)}
      />
      {filteredPosts.length > 0 ? (
        filteredPosts.map((post) => (
          <Post 
            key={post.id}
            post={post}
            user={user} 
            fetchData={fetchData}
          />
        ))
      ) : (
        <p>No posts found.</p>
      )}
    </div>
  );
};

export default UserPosts;

