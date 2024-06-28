import React, {useEffect} from 'react';
import useSSEConnection from "./useSSEConnection";
import NotificationMessage from "./NotificationMessage";


const SSEComponent = () => {
    const {messages, isConnected, setMessages} = useSSEConnection();

    useEffect(() => {
        console.log("SSE Messages:", messages); // 상태 업데이트 확인
    }, [messages]);
    return (
        <div>
            {isConnected ? (
                <>
                    {messages.map((msg, index) => {
                        const count = msg.count || 0;
                        return (
                            <NotificationMessage
                                key={index}
                                message={count > 0 ? `${msg.message} (메시지 수: ${count})` : msg.message}
                                onClose={() => setMessages((prev) => prev.filter((_, i) => i !== index))} // 닫기
                            />
                        );
                    })}
                </>
            ) : (
                <div></div>
            )}
        </div>
    );
};

export default SSEComponent;
