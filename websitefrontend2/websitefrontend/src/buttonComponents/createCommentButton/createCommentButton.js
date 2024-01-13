import React, {useState} from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import AddCommentIcon from '@mui/icons-material/AddComment';
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import '../../global.css';
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
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
        commenterid: user.appUserID,
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
    if (!user) {
      console.log("user is null");
      return;
    }
    event.preventDefault();
    await createComment();
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    border: '3px solid black',
    padding: '10px 20px',
    borderRadius: '5px',
    cursor: 'pointer',
    margin: '10px 0'
  };


  return (
      <div>
        <div>
          <AddCommentIcon button
              style={{fontSize: '50px', marginLeft: '20px', transform: 'translateY(10px) translateX(200px)', borderRadius: '4px', color: user && user.backgroundColor ? user.backgroundColor : getRandomColor()}}
              onClick={() => setShowForm(!showForm)}
          />
        </div>

        {showForm && (
            <form onSubmit={handleSubmit} style={{ transform:"translateX(-65px)", marginTop: "10px", display: "flex", flexDirection: "column", alignItems: "center", width: "300px" }}>
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