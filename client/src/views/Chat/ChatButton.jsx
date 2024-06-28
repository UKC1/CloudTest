import React from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';
const ChatButton = ({ foodId, giverId}) => {
    const navigate = useNavigate();




    const token = localStorage.getItem('jwt');
    const decoded = jwtDecode(token);
    const userId = decoded.mobileNumber;


    const handleCreateChatRoom = () => {
        if (window.confirm("사용자와 채팅하시겠습니까?")) {
            axios.post('/api/chat/createRoom', {
                firstUserMobileNumber: giverId,
                secondUserMobileNumber: userId,
                foodId: foodId
            })
                .then(response => {
                    const newChatRoomId = response.data.urlIdentifier;
                    console.log("newChatRoomId",newChatRoomId)
                    if(newChatRoomId) {
                        navigate(`/chat/GetChat/${newChatRoomId}`);  // 채팅 컴포넌트로 이동
                    } else {
                        console.error("Chat room ID is not provided in the response");
                    }
                })
                .catch(error => {
                    console.error("Failed to create chat room:", error);
                });
        }
    };

    return (
        <button onClick={handleCreateChatRoom}>
            채팅하기
        </button>
    );
};

export default ChatButton;