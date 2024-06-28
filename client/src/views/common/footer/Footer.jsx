import './Footer.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse, faComments, faStore} from "@fortawesome/free-solid-svg-icons";
import {NavLink, useLocation} from "react-router-dom";
import useSSEConnection from "../../Notification/useSSEConnection";
export default function Footer() {
    const location = useLocation();
    const {totalChatUnreadCount} = useSSEConnection();
    const getActiveClass = (path) => location.pathname === path ? 'active-link' : '';
    return (
        <footer>
            <ul>
                <li>
                    <NavLink className={getActiveClass('/main')} to={'/main'} >
                        <span><FontAwesomeIcon icon={faHouse} /></span>
                        <p>홈</p>
                    </NavLink>
                </li>
                <li>
                    <NavLink to={"/chat/getChatList"} style={{position: "relative"}}>
                        <span>
                            <FontAwesomeIcon icon={faComments}/>
                            {totalChatUnreadCount > 0 && (
                                <span className="chatUnread-count">{totalChatUnreadCount}</span>
                            )}
                        </span>
                        <p>채팅</p>
                    </NavLink>
                </li>
                <li>
                    <NavLink className={getActiveClass('/mypage')} to={'/mypage'}>
                        <span><FontAwesomeIcon icon={faStore}/></span>
                        <p>나의 냉장고</p>
                    </NavLink>
                </li>
            </ul>
        </footer>
    );
}