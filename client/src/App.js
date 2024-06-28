import './style/style.scss';
import {matchPath, Route, Routes, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import router from "./router";
function App() {
    const location = useLocation();
    const [currentHeader, setCurrentHeader] = useState(null);
    const [currentFooter, setCurrentFooter] = useState(null);

    function matchCustomPath(routePath, currentPath) {
        const routeParts = routePath.split('/');
        const currentParts = currentPath.split('/');
        if (routeParts.length !== currentParts.length) return false;

        return routeParts.every((part, index) => {
            return part.startsWith(':') || part === currentParts[index];
        });
    }

    useEffect(() => {
        const route = router.routes.find(route => matchCustomPath(route.path, location.pathname));
        if (route) {
            if (currentHeader !== route.header || currentFooter !== route.footer) {
                setCurrentHeader(route.header);
                setCurrentFooter(route.footer);
            }
        } else {
            if (currentHeader !== null || currentFooter !== null) {
                setCurrentHeader(null);
                setCurrentFooter(null);
            }
        }
    }, [location, currentHeader, currentFooter]);


    const isOnboarding = location.pathname === '/onBoarding';

    return (
        <>
            <main className={isOnboarding ? 'noPadding' : ''}>
                {currentHeader}
                <Routes>
                    {router.routes.map((route, index) => (
                        <Route path={route.path} element={route.component} key={index} />
                    ))}
                </Routes>
                {currentFooter}
            </main>
        </>
    );
}

export default App;

