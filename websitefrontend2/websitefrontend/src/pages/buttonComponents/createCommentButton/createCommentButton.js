import React, { useState } from 'react';
import { useUserContext } from '../../usercontext/UserContext';

const CommentForm = ({ posterusername, postId }) => {
  const { user } = useUserContext();
  const [commentContent, setCommentContent] = useState('');
  const [showForm, setShowForm] = useState(false);  // state to toggle form visibility


  const createComment = async () => {
    if (!user) {
      console.log("user is null");
      return;
    }
    console.log("User before creating comment:", user);
    const response = await fetch(`/api/post/${posterusername}/${postId}/createComment`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        commenterUsername: user.username,
        content: commentContent,
      }),
    });

    if (response.ok) {
      console.log('Comment created successfully');
      window.location.reload(true);
    } else {
      console.error('Failed to create comment');
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    await createComment();
  };

  return (
    <div>
      <button onClick={() => setShowForm(!showForm)}>
        {showForm ? "Hide Comment Form" : "Show Comment Form"}
      </button>
      {showForm && (
        <form onSubmit={handleSubmit}>
          <label>
            Comment:
            <input
              type="text"
              value={commentContent}
              onChange={(e) => setCommentContent(e.target.value)}
            />
          </label>
          <button type="submit">Submit</button>
        </form>
      )}
    </div>
  );
};

export default CommentForm;