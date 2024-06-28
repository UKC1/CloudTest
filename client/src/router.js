import Login from "./views/auth/login/Login";
import Register from "./views/auth/register/Register";
import {Header1, Header2, Header3, Header4} from "./views/common/header/Header";
import Footer from "./views/common/footer/Footer";
import Main from "./views/pages/board/main/Main";
import OnBoarding from "./views/pages/onboarding/OnBoarding";
import MyPage from "./views/pages/mypage/MyPage";
import Drawers from "./components/drawer/Drawers";
import Search from "./views/pages/search/Search";
import SearchAddress from "./views/pages/searchaddress/SearchAddress";
import MainDetail from "./views/pages/board/main/MainDetail";
import MainForm from "./views/pages/board/main/MainForm";
import ChatRoomList from "./views/Chat/ChatRoomList";
import Chat from "./views/Chat/Chat";
import AddrTest from "./components/address/PlaceSearch";
import PlaceSearch from "./components/address/PlaceSearch";

function Notifications() {
    return null;
}

const router = {
    routes: [
        {
            path: '/login',
            component: <Login/>,
            title: null,
            header:<Header1/>,
            footer:null
        },
        {
            path: '/register',
            component: <Register/>,
            title: '회원가입',
            header:<Header1/>,
            footer: null,
        },
        {
            path: '/main',
            component: <Main/>,
            title: null,
            header:<Header4/>,
            footer:<Footer/>
        },
        {
            path: '/main/foods/:id',
            component: <MainDetail/>,
            title: null,
            header:<Header3/>,
            footer:null
        },
        {
            path: '/main/update/:id' ,
            component: <MainForm/>,
            title: null,
            header:<Header1/>,
            footer:null
        },
        {
            path: '/main/add',
            component: <MainForm/>,
            title: null,
            header:<Header1/>,
            footer:null
        },
        {
            path: '/',
            component: <OnBoarding/>,
            title: null,
            header:null,
            footer:null
        },
        {
            path: '/mypage',
            component: <MyPage/>,
            title: '나의 냉장고',
            header: <Header1/>,
            footer:<Footer/>
        },
        {
            path: '/search/by-category',
            component: <Search/>,
            title: null,
            header: null,   
            footer: null
        },
        {
            path: '/searchAddress',
            component: <SearchAddress/>,
            title: null,
            header: null,
            footer: null
        },
        {
            path: '/chat/getChatList',
            component: <ChatRoomList/>,
            header: <Header4/>,
            footer: <Footer/>
        },
        {
            path: '/chat/getChat/:chatRoomId',
            component: <Chat/>,
            header: <Header4/>,

        },
        {
            path: 'notification',
            component: <Notifications/>,
            header: <Header4/>,
        },
        {
            path: '/test',
            component: <PlaceSearch/>,
            header: <Header4/>,
            footer: <Footer/>
        }



    ]
}
export default router;