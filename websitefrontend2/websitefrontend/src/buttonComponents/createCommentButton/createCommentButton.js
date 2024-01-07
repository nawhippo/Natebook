import React, {useState} from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import AddCommentIcon from '@mui/icons-material/AddComment';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import '../../global.css';
const CommentForm = ({ postId, updateComments }) => {
  const {user} = useUserContext();
  const [commentContent, setCommentContent] = useState('');
  const [showForm, setShowForm] = useState(false);


  const createComment = async () => {
    if (!user) {
      console.log("user is null");
      return;
    }
    console.log("User before creating comment:", user);
    const response = await fetchWithJWT(`/api/comment/${postId}/createComment`, {
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
      updateComments();
    } else {
      console.error('Failed to create comment');
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    await createComment();
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'grey',
    color: '#FFFFFF',
    border: 'none',
    padding: '10px 20px',
    borderRadius: '4px',
    cursor: 'pointer',
    margin: '10px 0'
  };


  return (
      <div>
        {/* Wrapper div for the icon with flex styles */}
        <div style={{ transform: 'TranslateX(350px)' }}>
          <AddCommentIcon
              style={{fontSize: '50px', cursor: 'pointer', color: user && user.backgroundColor ? user.backgroundColor : 'grey' }}
              onClick={() => setShowForm(!showForm)}
          />
        </div>

        {showForm && (
            <form onSubmit={handleSubmit} style={{ transform:"translateX(30px)",display: "flex", flexDirection: "column", alignItems: "center", width: "300px" }}>
              <div>
                <input
                    type="text"
                    placeholder={'Share your thoughts..'}
                    value={commentContent}
                    onChange={(e) => setCommentContent(e.target.value)}
                    style={{ width: '300px' }}
                />
              </div>
              <button type="submit" style={{ ...buttonStyle, width: '300px' }}>Submit</button>
            </form>
        )}
      </div>
  );
};

export default CommentForm;