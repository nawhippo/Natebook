import React, { useState } from 'react';
import { useUserContext } from '../../usercontext/UserContext';
const CommentForm = ({ userId, postId }) => {
  const { user } = useUserContext();
  const [commentContent, setCommentContent] = useState('');

  const createComment = async () => {
    const response = await fetch(`/api/post/${user.appUserID}/${postId}/createComment`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
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
    </div>
  );
};

export default CommentForm;