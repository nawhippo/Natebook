import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../login/UserContext';
import { Link } from 'react-router-dom'; 
import CommentForm from '../comment/createComment';
import '../posts.css';
import { handleReactionPost, handleReactionComment } from './InteractWithPost'; // import the refactored functions

const PostsPage = () => {
    const { user } = useUserContext();
    const [targetUsername, setTargetUsername] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [inputValue, setInputValue] = useState('');
    const [currentPostId, setCurrentPostId] = useState(null); 
    const [currentPosterId, setCurrentPosterId] = useState(null);
    const [allPostsData, setAllPostsData] = useState(null);

    const fetchData = (username) => {
      if(username){
      setIsLoading(true);
      fetch(`/api/post/${user.appUserID}/postsByUsername/${username}`)
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
          .catch((error) => {
              setError(error.message);
              setIsLoading(false);
          });
        } else {
      setIsLoading(true);
      fetch(`/api/post/${user.appUserID}/posts`)
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
          .catch((error) => {
              setError(error.message);
              setIsLoading(false);
          });
  }
};

    const handleDeletePost = async (postId) => {
      try {
          const response = await fetch(`/post/${user.appUserID}/${postId}/deletePost`, {
              method: 'DELETE'
          });
  
          if (!response.ok) {
              throw new Error('Failed to delete the post.');
          }
          fetchData(targetUsername);
      } catch (error) {
          setError(error.message);
      }
  };

    const handleInputChange = (event) => {
        const value = event.target.value;
        setInputValue(value);
    };

    const handleSearchClick = () => {
        setTargetUsername(inputValue);
        fetchData(inputValue);
    };

    const handleCommentClick = (postId) => {
        setCurrentPostId(postId);
    };

    //modified handlers that use the refactored functions
    const handleLikeClickPost = (posterid, postid) => {
        handleReactionPost(user.appUserID, posterid, postid, "Like", () => fetchData(targetUsername))
            .catch(error => setError(error.message));
    };

    const handleDislikeClickPost = (posterid, postid) => {
        handleReactionPost(user.appUserID, posterid, postid, "Dislike", () => fetchData(targetUsername))
            .catch(error => setError(error.message));
    };

    const handleLikeClickComment = (posterid, postid, commentid) => {
        handleReactionComment(user.appUserID, posterid, postid, commentid, "Like", () => fetchData(targetUsername))
            .catch(error => setError(error.message));
    };

    const handleDislikeClickComment = (posterid, postid, commentid) => {
        handleReactionComment(user.appUserID, posterid, postid, commentid, "Dislike", () => fetchData(targetUsername))
            .catch(error => setError(error.message));
    };

    return (
        <div>
            <h1>All Posts</h1>
            <input
                type="text"
                value={inputValue}
                placeholder="Enter username"
                onChange={handleInputChange}
            />
            <button onClick={handleSearchClick}>Search</button>
            {allPostsData && allPostsData.length > 0 ? (
                allPostsData.map((post) => (
                    <div key={post.id} className="post-card">
                        <h2>{post.title}</h2>
                        <p>{post.description}</p>
                        <p>
                            Likes: {post.likesCount} <button onClick={() => handleLikeClickPost(post.posterid, post.id)}>Like</button>
                            Dislikes: {post.dislikesCount} <button onClick={() => handleDislikeClickPost(post.posterid, post.id)}>Dislike</button>
                        </p>
                        <p>{post.email}</p>
                        <p>{post.dateTime}</p>
                        <p>By: {post.posterusername}</p>
                        {post.posterId === user.appUserID && (
                            <button onClick={() => handleDeletePost(post.id)}>Delete Post</button>
                        )}
                        
                        <p>Comments:</p>
                        <button onClick={() => handleCommentClick(post.id)}>Comment</button>

                        {currentPostId === post.id && <CommentForm postId={post.id} />}
                        {post.commentList.map((comment) => (
                            <div key={comment.id} className='comment'>
                                <p>{comment.content}</p>
                                <p>By: {comment.commenterusername}</p>
                                <p> 
                                    Likes: {comment.likesCount}
                                    <button onClick={() => handleLikeClickComment(post.posterid, post.id, comment.id)}>Like</button>
                                    Dislikes: {comment.dislikesCount}
                                    <button onClick={() => handleDislikeClickComment(post.posterid, post.id, comment.id)}>Dislike</button> 
                                </p>
                            </div>
                        ))}
                    </div>
                ))
            ) : (
                <p>No posts found.</p>
            )}
        </div>
    );
            }
export default PostsPage;