import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import AddCommentIcon from '@mui/icons-material/AddComment';
const CommentForm = ({ posterusername, postId }) => {
  const { user } = useUserContext();
  const [commentContent, setCommentContent] = useState('');
  const [showForm, setShowForm] = useState(false); 


  const createComment = async () => {
    if (!user) {
      console.log("user is null");
      return;
    }
    console.log("User before creating comment:", user);
    const response = await fetch(`/api/comment/${postId}/createComment`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        commenterusername: user.username,
        content: commentContent,
      }),
    });

    if (response.ok) {
      console.log('Comment created successfully');
    } else {
      console.error('Failed to create comment');
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    await createComment();
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'darkgrey'
  };


  return (
      <div>
        <AddCommentIcon onClick={() => setShowForm(!showForm)}>
          {showForm ? "Or not..." : "Comment"}
        </AddCommentIcon>
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
              <button type="submit" style={buttonStyle}>Submit</button>
            </form>
        )}
      </div>
  );
};

export default CommentForm;