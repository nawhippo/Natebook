import React, { useEffect, useState } from 'react';
import Post from '../../objects/post';
import { useUserContext } from '../../usercontext/UserContext';
import { fetchWithJWT } from '../../../utility/fetchInterceptor';
import SearchIcon from '@mui/icons-material/Search';
import {getRandomColor} from "../../../FunSFX/randomColorGenerator";
const UserPosts = ({ userid, profileUserId }) => {
  const [posts, setPosts] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const {user} = useUserContext();

  const buttonStyle = {
    backgroundColor: user?.backgroundColor || getRandomColor(),
    color: '#FFFFFF',
  };

  const fetchData = async () => {
    const response = await fetch(`/api/posts/${profileUserId}`);

    if (response.status === 204) {
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

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
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
        <div className="search-bar-container">
          <input
              className="search-input"
              type="text"
              value={searchTerm}
              placeholder="Search posts..."
              onChange={handleSearchChange}
          />
          <button className="search-button" type="submit" style={buttonStyle}>
            <SearchIcon/>
          </button>
        </div>
        {isLoading ? (
            <div>Loading...</div>
        ) : error ? (
            <div>{error}</div>
        ) : (
            <div>
              {filteredPosts.length > 0 ? (
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
              )}
            </div>
        )}
      </div>
  );
}

export default UserPosts;
