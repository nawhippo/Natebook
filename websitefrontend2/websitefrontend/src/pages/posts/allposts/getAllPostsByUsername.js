import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../usercontext/UserContext';
import CommentForm from '../comment/createComment';
import Post from './post';
import Comment from './comment';
const PostsPage = () => {
  const { user } = useUserContext();
  const [targetUsername, setTargetUsername] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [inputValue, setInputValue] = useState('');
  const [currentPostId, setCurrentPostId] = useState(null);
  const [allPostsData, setAllPostsData] = useState(null);
  const [showCommentForm, setShowCommentForm] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  const handleInputChange = (e) => {
    setInputValue(e.target.value);
  };
  
  const handleSearchClick = () => {
    fetchData(inputValue);
  };

  const handleCommentClick = async (postid, comment) => {
    if (comment) {
    const endpoint = `/api/post/${user.appUserID}/${postid}/createComment`;
    try {
      const response = await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ comment }),
      });
  
      if (!response.ok) {
        throw new Error('Failed to create comment.');
      }
  
      fetchData(targetUsername); // refresh data after commenting
    } catch (error) {
      setError(error.message);
    }
    setShowCommentForm(false);
  } else {
    setShowCommentForm(!showCommentForm);
  }
  };

  const fetchData = async (event) => {
    const endpoint = event 
      ? `/api/post/${user.appUserID}/postsByUsername/${event}`
      : `/api/post/${user.appUserID}/friendPosts`;
    setIsLoading(true);
    try {
      const response = await fetch(endpoint);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setAllPostsData(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteComment = async (userId, postId, commentId) => {
    try {
      const response = await fetch(`/api/post/${userId}/${postId}/${commentId}/deleteComment`, { method: 'DELETE' });
      if (!response.ok) {
        throw new Error('Failed to delete.');
      }
      fetchData(targetUsername);
    } catch (error) {
      setError(error.message);
    }
  };

  const handleDeletePost = async (userId, postId) => {
    try {
      const response = await fetch(`/api/post/${userId}/${postId}/deletePost`, { method: 'DELETE' });
      if (!response.ok) {
        throw new Error('Failed to delete.');
      }
      fetchData(targetUsername);
    } catch (error) {
      setError(error.message);
    }
  };
  

  const handleReaction = async (type, postId, posterId, commentId = null, action) => {
    let updateReactionEndpoint;
  
    if (type === "post") {
      updateReactionEndpoint = `/api/post/${user.appUserID}/${posterId}/${postId}/updateReactionPost`;
    } else if (type === "comment") {
      updateReactionEndpoint = `/api/post/${user.appUserID}/${posterId}/${postId}/${commentId}/updateReactionComment`;
    } else {
      console.error("Invalid reaction type");
      return;
    }
  
    try {
        const response = await fetch(updateReactionEndpoint, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ action }),
        });
    
        if (!response.ok) {
          throw new Error("Failed to update reaction.");
        }
    
        // Refresh the post data
        fetchData(targetUsername);
      } catch (error) {
        setError(error.message);
      }

};

return (
  <div>
    <h1>Friend Posts</h1>
    <input type="text" value={inputValue} placeholder="Enter username" onChange={handleInputChange} />
    <button onClick={handleSearchClick}>Search</button>

    {allPostsData && allPostsData.length > 0 ? allPostsData.map((post) => (
      <div key={post.id}>
        <Post
          post={post}
          user={user}
          handleReaction={handleReaction}
          handleDeletePost={handleDeletePost}
          handleCommentClick={() => handleCommentClick(post.id)}
          currentPostId={currentPostId}
          CommentFormComponent={CommentForm}
        >
          {post.commentList.map((comment) => (
            <Comment
              comment={comment}
              user={user}
              postId={post.id}
              handleReaction={handleReaction}
              handleDeleteComment={handleDeleteComment}
            />
          ))}
        </Post>
        {showCommentForm && currentPostId === post.id ? (
          <CommentForm
            onSubmit={(comment) => {
              handleCommentClick(post.id, comment);
            }}
          />
        ) : null}
      </div>
    )) : <p>No posts found.</p>}
  </div>
);
          }
export default PostsPage;