import './List.scss'
import {NavLink} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleRight} from "@fortawesome/free-solid-svg-icons";
export default function List({itemName, iconLeft, path, onclick}) {
    return(
        <li onClick={onclick}>
            <NavLink to={path} className={'list'}>
                <div className={'list_wrap'}>
                    <span>
                        <FontAwesomeIcon icon={iconLeft}/>
                    </span>
                    <p>{itemName}</p>
                </div>
                <div className={'list_wrap'}>
                    <FontAwesomeIcon icon={faAngleRight}/>
                </div>
            </NavLink>
        </li>
    )
}