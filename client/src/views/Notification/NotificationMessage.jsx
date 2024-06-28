import React, {useEffect} from 'react';
import './NotificationMessage.scss'

const NotificationMessage = ({message, duration = 10000, onClose}) => {
    useEffect(() => {
        const timer = setTimeout(onClose, duration);
        return () => clearTimeout(timer);
    }, [duration, onClose]);

    return (
        <div className="notification-message">
            <img src="/images/fooding.png" alt="Icon" className="icon"/>
            <span className="message-text">{message}</span>
            <button onClick={onClose} className="close-button">×</button>
        </div>
    );
};

export default NotificationMessage;