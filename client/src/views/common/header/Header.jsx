import './Header.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleLeft, faEllipsisVertical, faMagnifyingGlass, faBell} from "@fortawesome/free-solid-svg-icons";
import useHeaderTitle from "../../../hook/useHeaderTitle";
import {NavLink, useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";
import Menu from "../../../components/menu/Menu";
import Drawers from "../../../components/drawer/Drawers";

function Header1() {
    const navigate = useNavigate();
    const title = useHeaderTitle();

    return (
        <header className={'header1'}>
            <div className={'prev'}>
                <button onClick={() => navigate(-1)}>
                    <span>
                        <FontAwesomeIcon icon={faAngleLeft}/>
                    </span>
                </button>
            </div>
            <div className={'title'}>
                <h2>{title}</h2>
            </div>
        </header>
    );
}

function Header2() {
    const title = useHeaderTitle();
    return (
        <header className={'header2'}>
            <div className={'title'}>
                <h2>{title}</h2>
            </div>
        </header>
    )
}
function Header3({food}) {
    const navigate = useNavigate();
    const title = useHeaderTitle();
    return(
        <header className={'header3'}>
            <div className={'prev'}>
                <button onClick={() => navigate(-1)}>
                    <span>
                        <FontAwesomeIcon icon={faAngleLeft}/>
                    </span>
                </button>
            </div>
            <div className={'title'}>
                <h2>{title}</h2>
            </div>
            <div className={'menu'}>
                <Drawers
                    trigger={
                        <button>
                            <span>
                                <FontAwesomeIcon icon={faEllipsisVertical}/>
                            </span>
                        </button>
                    }
                    drawerContent={<Menu food={food} />}
                    />
            </div>
        </header>
    )
}
function Header4(){
    const address = useSelector(state => state.address.currentAddress);
    return(
        <header className={'header4'}>
            <div className={'address_wrap'}>
                <NavLink to={'/searchAddress'}>
                    <span className={'address'}>{address}</span>
                </NavLink>
            </div>
            <div className={'menu'}>
                <ul>
                    <li>
                        <NavLink to={'/search/by-category'}>
                            <FontAwesomeIcon icon={faMagnifyingGlass}/>
                        </NavLink>
                    </li>
                    <li>
                        <NavLink to={'/notification'}>
                            <FontAwesomeIcon icon={faBell}/>
                        </NavLink>
                    </li>
                </ul>
            </div>
        </header>
    )
}


export {Header1, Header2, Header3, Header4};