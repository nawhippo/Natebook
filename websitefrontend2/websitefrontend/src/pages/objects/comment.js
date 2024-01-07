import React, {useState} from 'react';
import {DeleteCommentButton} from '../../buttonComponents/deleteCommentButton/deleteCommentButton';
import ReactionButtons from '../../buttonComponents/reactCommentButtons/reactCommentButtons';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {useUserContext} from "../usercontext/UserContext";

const Comment = ({ comment, updateComments }) => {
    const { user } = useUserContext();
    const[localLikes, setLocalLikes] = useState(comment.likesCount);
    const[localDislikes, setLocalDislikes] = useState(comment.dislikesCount);

    const handleUpdateLikesDislikes = (newLikes, newDislikes) => {
        setLocalLikes(newLikes);
        setLocalDislikes(newDislikes);
        updateComments(comment.id, newLikes, newDislikes);
    };

    return (
        <div key={comment.id}>
            <div className='comment' style={{ backgroundColor: 'lightgray', borderRadius: '20px', padding: '10px', margin: '10px 0', width: '95%' }}>
                <div style={{ display: 'flex', alignItems: 'center' , transform:"TranslateX(200px)" }}>
                    <p style={{ fontSize: '20px', marginRight: '5px' }}>{comment.commenterusername}</p>
                    <ProfilePictureComponent userid={comment.commenterId} style={{ width: '20px', height: '20px', fontSize: '5px' }} />
                </div>
                <p style={{ fontSize: '20px' }}>{comment.content}</p>
                {user &&
                    <ReactionButtons
                        commentId={comment.id}
                        updateLikesDislikes={handleUpdateLikesDislikes}
                        likesCount={localLikes}
                        dislikesCount={localDislikes}
                    />
                }
                {user && comment.commenterid === user.id &&
                    <DeleteCommentButton
                        commentId={comment.id}
                    />
                }
            </div>
        </div>
    );
};

export default Comment;