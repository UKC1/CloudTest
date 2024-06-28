import {useEffect, useCallback} from 'react';
import { throttle } from './utils/throttle';
export const useScrollManagement = (
    messagesContainerRef, messageList, hasMore,
    isLoading, loadChatMessages, isFirstLoaded,
    setIsFirstLoaded, lastSentMessageTimestamp,
    hasUserScrolled, setHasUserScrolled, userId
) => {


    // 초기에 스크롤을 가장 아래로 조정
    useEffect(() => {
        const firstLoadedScrollDown = () => {
            const container = messagesContainerRef.current;
            if (!container) return;

            if (isFirstLoaded && messageList.length > 0) {
                container.scrollTop = container.scrollHeight;
                setIsFirstLoaded(false);
            }
        };
        firstLoadedScrollDown();
    }, [messageList, isFirstLoaded, setIsFirstLoaded]);


    // 스크롤 위로 올리면 조회
    useEffect(() => {
        const loadMoreMessagesOnScrollUp = () => {
            const {scrollTop, scrollHeight} = messagesContainerRef.current;

            setHasUserScrolled(true);

            if (scrollTop === 0 && hasMore && !isLoading) {
                const currentTopMessage = messagesContainerRef.current.firstChild;
                loadChatMessages().then(() => {
                    if (currentTopMessage) {
                        currentTopMessage.scrollIntoView({behavior: "smooth"});
                    }
                });
            }
        };
        const container = messagesContainerRef.current;
        const throttledScrollHandler = throttle(loadMoreMessagesOnScrollUp, 1);
        container.addEventListener('scroll', throttledScrollHandler);
        return () => {
            container.removeEventListener('scroll', throttledScrollHandler)
        };
    }, [hasMore, loadChatMessages, isLoading , setHasUserScrolled]);




    // 메시지 전송 시 스크롤 자동 다운
    useEffect(() => {
        const scrollDownOnMessageSend = () => {
            const container = messagesContainerRef.current;
            if (!container) return;

            const lastMessage = messageList[messageList.length - 1];
            if (lastMessage && !hasUserScrolled && lastMessage.sender === userId &&
                new Date(lastMessage.timestamp).getTime() === new Date(lastSentMessageTimestamp).getTime()) {

                container.scrollTop = container.scrollHeight;
            }
        };

        scrollDownOnMessageSend();
    }, [messageList, lastSentMessageTimestamp, userId, hasUserScrolled, setHasUserScrolled]);


    //메시지를 보내면 스크롤 자동으로 내려가는거
    useEffect(() => {
        const resetScrollOnMessageSend = () => {
            if (lastSentMessageTimestamp) {
                setHasUserScrolled(false);
            }
        };

        resetScrollOnMessageSend();
    }, [lastSentMessageTimestamp]);


    // 새 메시지 도착 알림
    useEffect(() => {
        const notifyOnNewMessage = () => {
            const container = messagesContainerRef.current;
            if (!container) return;

            const lastMessage = messageList[messageList.length - 1];
            if (lastMessage && new Date(lastMessage.timestamp).getTime() === new Date(lastSentMessageTimestamp).getTime()) {
                if (container.scrollTop + container.clientHeight < container.scrollHeight) {

                }
            }
        };
        notifyOnNewMessage();
    }, [messageList, lastSentMessageTimestamp]);




};