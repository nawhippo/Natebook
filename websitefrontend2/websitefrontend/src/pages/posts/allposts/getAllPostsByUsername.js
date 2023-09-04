import React, { useState } from 'react';
import { useUserContext } from '../../usercontext/UserContext';
import CommentForm from '../comment/createComment';

const PostsPage = () => {
    const { user } = useUserContext();
    const [targetUsername, setTargetUsername] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [inputValue, setInputValue] = useState('');
    const [currentPostId, setCurrentPostId] = useState(null);
    const [allPostsData, setAllPostsData] = useState(null);

    const fetchData = (username) => {
        const endpoint = username 
            ? `/api/post/${user.appUserID}/postsByUsername/${username}`
            : `/api/post/${user.appUserID}/posts`;
            
        setIsLoading(true);
        fetch(endpoint)
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then((data) => {
                setAllPostsData(data);
                setIsLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setIsLoading(false);
            });
    };

    const handleDelete = async (endpoint) => {
        try {
            const response = await fetch(endpoint, { method: 'DELETE' });
            if (!response.ok) {
                throw new Error('Failed to delete.');
            }
            fetchData(targetUsername);
        } catch (error) {
            setError(error.message);
        }
    };

    const handleInputChange = (event) => setInputValue(event.target.value);
    const handleSearchClick = () => {
        setTargetUsername(inputValue);
        fetchData(inputValue);
    };


    const handleCommentClick = (postId) => setCurrentPostId(postId);

    const handleReaction = async (endpoint, action, callback) => {
        try {
            const response = await fetch(endpoint);
            const currentReaction = await response.json();

            let nextAction;
            if (currentReaction === "None") {
                nextAction = action;
            } else if (currentReaction === action) {
              //unlike etc
                nextAction = `Un${action.toLowerCase()}`;
            } else {
                nextAction = action;
            }

            await fetch(endpoint, {
                method: "PUT",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ action: nextAction })
            });

            callback();
        } catch (error) {
            setError("Error updating reaction.");
        }
    };

    return (
      <div>
          <h1>All Posts</h1>
          <input type="text" value={inputValue} placeholder="Enter username" onChange={handleInputChange} />
          <button onClick={handleSearchClick}>Search</button>
          
          {allPostsData && allPostsData.length > 0 ? allPostsData.map((post) => (
              <div key={post.id} className="post-card">
                  <h2>{post.title}</h2>
                  <p>{post.description}</p>
                  <p>Likes: {post.likesCount} <button onClick={() => handleReaction(post.posterid, post.id, null, "Like")}>Like</button> 
                  Dislikes: {post.dislikesCount} <button onClick={() => handleReaction("post", post.id, null, "Dislike")}>Dislike</button></p>
                  <p>{post.email}</p>
                  <p>{post.dateTime}</p>
                  <p>By: {post.posterusername}</p>
                  {post.posterId === user.appUserID && <button onClick={() => handleDelete(post.id)}>Delete Post</button>}
                  
                  <p>Comments:</p>
                  <button onClick={() => handleCommentClick(post.id)}>Comment</button>
                  {currentPostId === post.id && <CommentForm postId={post.id} />}
                  
                  {post.commentList.map((comment) => (
                      <div key={comment.id} className='comment'>
                          <p>{comment.content}</p>
                          <p>By: {comment.commenterusername}</p>
                          <p>Likes: {comment.likesCount} <button onClick={() => handleReaction("comment", post.id, comment.id, "Like")}>Like</button> 
                          Dislikes: {comment.dislikesCount} <button onClick={() => handleReaction("comment", post.id, comment.id, "Dislike")}>Dislike</button></p>
                          {comment.commenterId === user.appUserID && <button onClick={() => handleDelete(post.id, comment.id)}>Delete Comment</button>}
                      </div>
                  ))}
              </div>
          )) : <p>No posts found.</p>}
      </div>
  );
          }
export default PostsPage;