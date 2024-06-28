import router from "../router";
import {useLocation} from "react-router-dom";

export default function useHeaderTitle() {
    const location = useLocation();
    const route = router.routes.find(route => route.path === location.pathname);
    if (route && route.header) {
        return route.title;
    } else {
        return null
    }
}
