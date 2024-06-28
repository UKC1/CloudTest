import './MyPage.scss';
import List from "../../../components/list/List";
import {
    faBell,
    faCircleExclamation,
    faGear,
    faList,
    faPaperPlane,
    faPowerOff, faUserAstronaut
} from "@fortawesome/free-solid-svg-icons";
import {faCircleQuestion, faCreditCard, faHeart} from "@fortawesome/free-regular-svg-icons";
import {useNavigate} from "react-router-dom";
import {MypageButton} from "../../../components/button/Button";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";


export default function MyPage() {
    const navigate = useNavigate();
    const handleLogout = () => {
        if(window.confirm('로그아웃을 하시겠습니까?')){
            localStorage.clear();
            navigate('/');
        }
    };

    return (
        <section className={'mypage'}>
            <div className={'profile'}>
                <div className={'profile_wrap'}>
                    <p className={'user_profile'}>
                        <FontAwesomeIcon icon={faUserAstronaut} />
                    </p>
                    <p className={"user_name"}>USER</p>
                    <p>서울시 가산동</p>
                </div>
            </div>
            <ul className={'iconButton_wrap'}>
                <li><MypageButton name={'Likes'} icon={faHeart}/></li>
                <li><MypageButton name={'Notice'} icon={faBell}/></li>
                <li><MypageButton name={'Setting'} icon={faGear}/></li>
                <li><MypageButton name={'Payment'} icon={faCreditCard}/></li>
            </ul>
            <ul className={'list_wrap'}>
                <List path={''} itemName={'쿡쉐어 내역'} iconLeft={faList}/>
                <List path={''} itemName={'피드백 보내기'} iconLeft={faPaperPlane}/>
                <List path={''} itemName={'자주하는 질문'} iconLeft={faCircleQuestion}/>
                <List path={''} itemName={'약관 및 정책'} iconLeft={faCircleExclamation}/>
                <List onclick={handleLogout} itemName={'로그아웃'} iconLeft={faPowerOff}/>
            </ul>
        </section>
    )
}