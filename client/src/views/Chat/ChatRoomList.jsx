import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';
import {Link} from "react-router-dom";
import './ChatRoomList.scss'

import useWebSocketConnection from "./useWebSocketConnection";

function ChatRoomList() {
    const [rooms, setRooms] = useState([]);
    const [selectedRooms, setSelectedRooms] = useState([]);
    const [error, setError] = useState('');

    const token = localStorage.getItem('jwt');
    const decoded = jwtDecode(token);
    const userId = decoded.mobileNumber;


    const userName = decoded.userId;


    const fetchRooms = () => {
        if (!token) {
            setError('로그인이 필요합니다.');
            return;
        }

        axios.get('/api/chat/ListRooms', {
            headers: {
                //      'Authorization': `Bearer ${token}`
            },
            params: {
                userId: decoded.mobileNumber,

            }
        })
            .then((response) => {
                if (Array.isArray(response.data)) {
                    setRooms(response.data);

                } else {
                    setError("채팅방 목록을 가져오는데 실패했습니다. 잘못된 데이터 형식입니다.");
                    console.log("채팅방 데이터", response.data);
                }
            })
            .catch(error => {
                    if (error.response && error.response.status === 401) {
                        console.error("인증되지 않았습니다. 로그인이 필요합니다.");
                        setError("로그인이 필요합니다.");
                    }
                    console.error('채팅방 목록을 가져오는데 실패했습니다.', error);
                    setError('채팅방 목록을 가져오는데 실패했습니다.');
                }
            );
    };

    useEffect(() => {
        fetchRooms();
    }, []);

    const stompClient = useWebSocketConnection(
        'http://localhost:8080/ws',
        '/topic/chat/room/updates',
        () => {
            console.log("채팅방 목록 업데이트 수신");
            fetchRooms();
        }
    );


    const toggleRoomSelection = (chatRoomId) => {
        if (selectedRooms.includes(chatRoomId)) {

            setSelectedRooms(selectedRooms.filter((id) => id !== chatRoomId));
        } else {

            setSelectedRooms([...selectedRooms, chatRoomId]);
        }
    };


    const hideRooms = (chatRoomId) => {
        if (selectedRooms.length > 0 && window.confirm('선택한 채팅방을 숨기시겠습니까?')) {
            console.log("if  hideRooms")

            selectedRooms.forEach((chatRoomId) => {
                const hideRequestData = {
                    chatRoomId: chatRoomId,
                    userId: userId,
                };


                axios.put(`/api/chat/hideRoom/${chatRoomId}`, hideRequestData)
                    .then(() => {
                        fetchRooms();
                    })
                    .catch(error => {
                        console.error('채팅방 숨기기에 실패했습니다.', error);
                        console.log(JSON.stringify(hideRequestData));
                    });

            });
            setSelectedRooms([]);
        }
    };
    const trimMessage = (message, maxLength = 30) => {
        return message.length > maxLength
            ? `${message.slice(0, maxLength)}...`
            : message;
    };

    if (error) {
        return <div>오류: {error}</div>;
    }


    return (
        <div className="chat-room-list-container">
            <button id="chatHideBtn" onClick={hideRooms}>선택한 채팅방 숨기기</button>
            {/* 버튼 클릭 이벤트 */}
            <ul className="chat-room-list">
                {rooms.map(chatRoom => (
                    <li key={chatRoom.chatRoomId} className="chat-room">
                        <input
                            type="checkbox"
                            checked={selectedRooms.includes(chatRoom.chatRoomId)}
                            onChange={() => toggleRoomSelection(chatRoom.chatRoomId)}
                        />
                        <Link to={`/chat/getChat/${chatRoom.chatRoomId}`} className="chat-room-link">
                            <img src="/img/userImage.png" alt="User" className="chat-room-image"/>
                            <div className="chat-room-info">
                                <p className="chat-room-name">{chatRoom.chatRoomId}</p>
                                <p className="chat-room-last-message">
                                    {trimMessage(chatRoom.lastMessage)}
                                </p>
                                <p className="chat-room-time">{new Date(chatRoom.lastMessageTimestamp).toLocaleString()}</p>
                            </div>
                        </Link>
                        <div className="chat-room-actions">
                            {chatRoom.unreadCount > 0 && (
                                <div className="chat-room-unread-indicator">
                                    {chatRoom.unreadCount}
                                </div>
                            )}
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );

}

export default ChatRoomList;