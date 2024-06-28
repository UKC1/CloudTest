import {useEffect, useState} from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default function useWebSocketConnection(connectUrl, subscribePath, onMessageReceived) {
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        if (stompClient) {
            return;
        }

        const socket = new SockJS(connectUrl);
        const client = Stomp.over(socket);


        client.connect(
            {},
            () => {
                console.log("웹소켓 연결 성공");
                client.subscribe(subscribePath, (message) => {
                    try {

                        const parsedMessage = JSON.parse(message.body);
                        onMessageReceived(parsedMessage); // 메시지 처리
                    } catch (error) {
                        console.error("메시지 파싱 오류:", error); // 예외 처리
                    }
                });

                setStompClient(client);
            },
            (error) => {
                console.error("웹소켓 연결에러: ", error);
            }
        );

        return () => {
            if (client.connected) {
                client.disconnect(() => console.log("웹소켓 끊김"));
            }
        };
    }, [connectUrl, subscribePath]);

    return stompClient;
}