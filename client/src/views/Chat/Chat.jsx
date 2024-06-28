import React, {useCallback, useEffect, useRef, useState} from "react";
import './Chat.scss';
import {jwtDecode} from 'jwt-decode';
import useChatDetailList from "./useChatDetailList";
import {useNavigate, useParams} from "react-router-dom";
import {useScrollManagement} from "./useScrollManagement";
import useWebSocketConnection from "./useWebSocketConnection";
import axios from "axios";


function Chat() {
    const {chatRoomId} = useParams(); // URL에서 roomId 추출 . app.js ChatRoomList

    const [newMessage, setNewMessage] = useState("");

    const [isFirstLoaded, setIsFirstLoaded] = useState(true);
    const [lastMessageTimestamp, setLastMessageTimestamp] = useState(null); // 마지막으로 전송된 메시지의 ID 또는 타임스탬프를 저장
    const [hasUserScrolled, setHasUserScrolled] = useState(false);
    const messagesContainerRef = useRef(null);

    const token = localStorage.getItem('jwt');
    const decoded = jwtDecode(token);
    const userId = decoded.mobileNumber;

    const {messageList, isLoading, loadChatMessages, hasMore, addMessageList} = useChatDetailList(chatRoomId, userId);

    const stompClient = useWebSocketConnection(
        "http://localhost:8080/ws",
        `/topic/chat/room/${chatRoomId}`,
        (receivedMessage) => {
            addMessageList(receivedMessage);
        }
    );
    useScrollManagement(messagesContainerRef, messageList, hasMore, isLoading,
        loadChatMessages, isFirstLoaded, setIsFirstLoaded, lastMessageTimestamp,
        hasUserScrolled, setHasUserScrolled, userId);


    useEffect(() => {
        loadChatMessages();
    }, []);


    const sendMessage = () => {
        if (stompClient && stompClient.connected && newMessage.trim() !== '') {
            const timestamp = new Date().toISOString();
            const chatMessage = {
                chatRoomId: chatRoomId,
                sender: userId,
                content: newMessage,
                timestamp: timestamp,
            };

            stompClient.send(`/app/chat.room/${chatRoomId}/sendMessage`, {}, JSON.stringify(chatMessage));
            setLastMessageTimestamp(timestamp);
            setNewMessage('');
        } else {
            console.log("메시지를 전송할 수 없습니다.");
        }
    };


    return (
        <div>
            <div className="chatForm">
                {/*{isLoading && <div className="loadingContainer">Loading...</div>}*/}
                <div className="messagesContainer" id="messagesContainer" ref={messagesContainerRef}>
                    {messageList.map((msg, index) => (
                        <div key={index} className={`chatContent ${msg.sender === userId ? "me" : "them"}`}>
                            {msg.sender !== userId && <img src="/img/fooding.png" alt={`${msg.sender}`}/>}
                            <div className="messageArea">
                                <div className="messageInfo">
                                    <div className="sender">{msg.sender === userId ? "" : msg.sender}</div>
                                    <div className="content">{msg.content}</div>
                                    <div
                                        className="timestamp">
                                        {msg.timestamp ? new Date(msg.timestamp).toLocaleString('ko-KR',
                                            {
                                                year: 'numeric',
                                                month: '2-digit',
                                                day: '2-digit',
                                                hour: '2-digit',
                                                minute: '2-digit'
                                            }) : 'N/A'}

                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
                <div className="inputContainer">
                    <input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)}
                           onKeyPress={(e) => {
                               if (e.key === "Enter") {
                                   sendMessage();
                               }
                           }}/>
                    <button id="chatBtn" onClick={sendMessage}>보내기</button>
                </div>
            </div>
        </div>
    );
}

export default Chat;