import React, {useEffect, useState} from 'react';
import Post from '../../objects/post';
import CreatePostButton from '../../../buttonComponents/createPostButton/createPostButton';
import SearchIcon from '@mui/icons-material/Search';
import '../../../global.css';
import {useUserContext} from '../../usercontext/UserContext';

const FriendFeed = () => {
  const { user } = useUserContext();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredPosts, setFilteredPosts] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    if (user && user.appUserID) {
      fetchData(user.appUserID);
    }
  }, [user]);

  useEffect(() => {
    if (filteredPosts && searchTerm) {
      setFilteredPosts(filteredPosts.filter(post => post.posterusername.toLowerCase().includes(searchTerm.toLowerCase())));
    }
  }, [searchTerm, filteredPosts]);

  const fetchData = (userId) => {
    const endpoint = userId ? `/api/post/${userId}/friendPosts` : '/api/publicFeed';

    setIsLoading(true);
    fetch(endpoint)
        .then((response) => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then((data) => {
          setFilteredPosts(data);
          setIsLoading(false);
        })
        .catch((err) => {
          setError(err.message);
          setIsLoading(false);
        });
  };

  const handleInputChange = (event) => setSearchTerm(event.target.value);

  return (
      <div>
        <h1>{user ? 'Friend Posts' : 'Feed'}</h1>

        {user && (
            <div className="create-post-section">
              <CreatePostButton />
            </div>
        )}

        <div className="search-bar-container">
          <input
              className="search-input"
              type="text"
              value={searchTerm}
              placeholder="Search by Username"
              onChange={handleInputChange}
          />
          <button className="search-button" type="submit" onClick={() => fetchData(user.appUserID)}>
            <SearchIcon />
          </button>
        </div>

        {isLoading ? (
            <p>Loading...</p>
        ) : error ? (
            <p>Error: {error}</p>
        ) : filteredPosts && filteredPosts.length > 0 ? (
            filteredPosts.map((post) => (
                <Post
                    key={post.id}
                    post={post}
                    fetchData={() => fetchData(user.appUserID)}
                />
            ))
        ) : (
            <p>No posts found.</p>
        )}
      </div>
  );
};

export default FriendFeed;