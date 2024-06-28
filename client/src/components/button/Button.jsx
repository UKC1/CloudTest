import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPencil} from "@fortawesome/free-solid-svg-icons";
import './Button.scss';
import {NavLink} from "react-router-dom";
function MypageButton({path, name, icon}) {
    return (
        <button className={'mypage_button'} path={path}>
            <div className={'btn_wrap'}>
                <FontAwesomeIcon icon={icon} />
                <p>{name}</p>
            </div>
        </button>
    )
}

function AddButton ({className}) {
    return(
        <button className={`add_button ${className}`}>
            <FontAwesomeIcon icon={faPencil}/>
            <span className={'write'}>글쓰기</span>
        </button>
    )
}

function SquareButton({ name, onClick }) {
    return (
        <button className={'square_button chat_button'} onClick={onClick}>
            <span>{name}</span>
        </button>
    );
}
function IconButton({ icon, onClick, style }) {
    return (
        <button className={'icon_button'} style={style} onClick={onClick}>
            <FontAwesomeIcon icon={icon}/>
        </button>
    );
}
export {MypageButton, AddButton, SquareButton, IconButton};