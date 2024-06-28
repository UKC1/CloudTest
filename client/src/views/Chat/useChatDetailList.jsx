import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';

const useChatDetailList = (chatRoomId, userId) => {
    const [messageList, setMessageList] = useState([]);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [pageSize, setPageSize] = useState(10);
    const [isLoading, setIsLoading] = useState(false);

    const updateMessagesAsRead = useCallback(() => {
        if (!userId) {
            throw new Error("userId 필요");
        }

        const chatRequestData = {
            chatRoomId: chatRoomId,
            userId: userId,
        };

        axios.put(`/api/chat/detailRoom/updateAsRead`, chatRequestData)
            .then((response) => {
                console.log("읽음 상태 업데이트 성공");
            })
            .catch((error) => {
                console.error("읽음 업데이트 실패:", error);
            });
    }, [chatRoomId, userId]);

    const addMessageList = useCallback((newMessage) => {
        setMessageList((prevMessages) => [...prevMessages, newMessage]);
        updateMessagesAsRead();
    }, [updateMessagesAsRead]);

    const loadChatMessages = useCallback(() => {
        if (!hasMore || isLoading) return;

        setIsLoading(true);
        axios.get(`/api/chat/detailRoom/${chatRoomId}/messages`, {
            params: {userId, page, size: pageSize}
        }).then(response => {
            const newMessages = response.data.content || [];
            if (newMessages.length < pageSize) {
                setHasMore(false);
            }
            setMessageList(prevMessages => [...newMessages.reverse(), ...prevMessages]);
            setPage(prevPage => prevPage + 1);
        }).catch(error => {
            console.error("채팅방 상세 정보 조회 에러: ", error);
            setHasMore(false);
        }).finally(() => {
            setIsLoading(false);
        });
    }, [chatRoomId, userId, page, pageSize, isLoading, hasMore]);

    return { messageList, isLoading, loadChatMessages, hasMore, addMessageList };
};

export default useChatDetailList;