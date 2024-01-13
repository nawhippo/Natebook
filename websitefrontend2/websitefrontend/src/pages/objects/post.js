import React, {useEffect, useState} from 'react';
import './PostStyle.css';
import Comment from './comment';
import CommentForm from '../../buttonComponents/createCommentButton/createCommentButton';
import ReactionButtons from '../../buttonComponents/reactPostButtons/reactPostButtons';
import {DeletePostButton} from '../../buttonComponents/deletePostButton/deletePostButton';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {useUserContext} from "../usercontext/UserContext";
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import CommentIcon from '@mui/icons-material/Comment';
import CommentsDisabledIcon from '@mui/icons-material/CommentsDisabled';
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
const Post = ({ post, fetchData, posterid }) => {
    const {user} = useUserContext();
    const [localLikesCount, setLocalLikesCount] = useState(post.likesCount);
    const [localDislikesCount, setLocalDislikesCount] = useState(post.dislikesCount);
    const [comments, setComments] = useState([]);
    const [images, setImages] = useState([]);
    const [isUserLoggedIn, setIsUserLoggedIn] = useState(!!user); // Check if user is logged in
    const [areCommentsCollapsed, setAreCommentsCollapsed] = useState(true);

    const updateLikesDislikes = (newLikes, newDislikes) => {
        setLocalLikesCount(newLikes);
        setLocalDislikesCount(newDislikes);
    };

    const fetchComments = async () => {
        try {
            const response = await fetch(`/api/post/${post.id}/comments`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const commentsData = await response.json();
            setComments(commentsData);
        } catch (error) {
            console.error('Error fetching comments:', error);
        }
    };

    const fetchImages = async () => {
        try {
            const response = await fetchWithJWT(`/api/post/${post.id}/images`);
            console.log(response);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const imagesData = await response.json();
            setImages(imagesData);
        } catch (error) {
            console.error('Error fetching Images:', error);
        }
    };

    useEffect(() => {
        setLocalLikesCount(post.likesCount);
        setLocalDislikesCount(post.dislikesCount);
        fetchComments();
        fetchImages();
    }, [post]);

    const toggleButtonStyle = {
        border: 'none',
        cursor: 'pointer',
        margin: '0px',
        fontSize: '50px',
        color: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
        marginTop: '0px',
        width: '50px',
        height: '50px',
        background: 'none',
    };

    return (
        <div className="post-container">
            <div style={{display: 'flex', justifyContent: "center"}}>
                <div style={{ transform: 'translateY(50px)', fontSize: '20px' }}>{post.posterUsername} </div>
                <ProfilePictureComponent userid={posterid}/>
            </div>
            <div className="post-title">{post.title}</div>
            <div className="post-description">{post.description}</div>
            <div className="post-content">{post.content}</div>
            <div className="post-datetime">{post.datetime}</div>
                <br/>
            <div className="image" style={{display: 'flex', justifyContent: "center"}}>
            {images.map((image) => (
                <img
                    key={image.id}
                    src={`data:image/${image.format};base64,${image.base64EncodedImage}`}
                    alt="Post"
                    style={{ width: image.width, height: image.height, marginBottom: '30px' }}
                />
            ))}
            </div>

            <div className="interactions-container" style={{ display:'flex'}}>
                <ReactionButtons
                    postId={post.id}
                    updateLikesDislikes={updateLikesDislikes}
                    likesCount={localLikesCount}
                    dislikesCount={localDislikesCount}
                />

                    <button style={{...toggleButtonStyle, transform: 'translateX(300px)'}} onClick={() => setAreCommentsCollapsed(!areCommentsCollapsed)}>
                        {areCommentsCollapsed ?
                            <CommentIcon style={{ fontSize: '50px' }}/> :
                            <CommentsDisabledIcon style={{ fontSize: '50px' }}/>
                        }
                    </button>
                    <CommentForm
                        userId={user ? user.appUserID : null}
                        posterusername={post.posterUsername}
                        postId={post.id}
                        updateComments={fetchComments}
                    />
            </div>
            <div style={{transform: 'translateX(100px)'}}>
            {user && user.username === post.posterUsername && (
                <DeletePostButton
                    postId={post.id}
                    fetchData={fetchData}
                />
            )}
            </div>


            <div>{comments.size > 0 && comments.size}</div>
            {areCommentsCollapsed && comments.map((comment) => (
                <Comment
                    key={comment.id}
                    comment={comment}
                    commenterId={comment.commenterid}
                    user={user}
                    postId={post.id}
                    posterId={post.posterId}
                    posterusername={post.posterUsername}
                    fetchData={fetchData}
                    updateComments={fetchComments}
                />
            ))}
        </div>
    );
};

export default Post;