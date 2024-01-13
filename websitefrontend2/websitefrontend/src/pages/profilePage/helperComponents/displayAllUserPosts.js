import React, { useEffect, useState } from 'react';
import Post from '../../objects/post';
import { useUserContext } from '../../usercontext/UserContext';
import { fetchWithJWT } from '../../../utility/fetchInterceptor';

const UserPosts = ({ userid, profileUserId }) => {
  const [posts, setPosts] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useUserContext();

  const fetchData = async () => {
    const response = await fetch(`/api/posts/${profileUserId}`);

    if (response.status === 404) {
      setError("User's posts not found.");
    } else if (response.status === 204) {
      setError("User hasn't posted anything yet.");
    } else {
      try {
        const data = await response.json();
        setPosts(data);
      } catch (error) {
        console.error('Error parsing JSON:', error);
        console.error('Response Data:', response.data);
        setError('An error occurred while parsing the response.');
      }
    }

    setIsLoading(false);
  };

  const filteredPosts = posts.filter((post) => {
    return post.description.toLowerCase().includes(searchTerm.toLowerCase());
  });

  useEffect(() => {
    fetchData();
  }, [userid, profileUserId]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div>
      <h1>User Posts</h1>
      <input
        type="text"
        placeholder="Search posts..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      {user ? (
        filteredPosts.length > 0 ? (
          filteredPosts.map((post) => (
            <Post
              key={post.id}
              post={post}
              user={user}
              posterid={profileUserId}
              fetchData={fetchData}
            />
          ))
        ) : (
          <p>User hasn't posted anything yet.</p>
        )
      ) : (
        <p>User is not available. Please log in.</p>
      )}
    </div>
  );
};

export default UserPosts;
