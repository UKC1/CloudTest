import "@testing-library/jest-dom/extend-expect"
import {render, screen, waitFor} from '@testing-library/react'
import '@testing-library/jest-dom';
import userEvent from '@testing-library/user-event';
import Login from "./Login";
import MockAdapter from 'axios-mock-adapter';
import axios from 'axios';
import {TextDecoder, TextEncoder} from "util";

if (typeof TextEncoder === "undefined") {
    global.TextEncoder = require("util").TextEncoder;
}
if (typeof TextDecoder === "undefined") {
    global.TextDecoder = require("util").TextDecoder;
}

const mock = new MockAdapter(axios);

// localStorage 모킹
const localStorageMock = (function() {
    let store = {};
    return {
        getItem(key) {
            return store[key] || null;
        },
        setItem(key, value) {
            store[key] = value.toString();
        },
        removeItem(key) {
            delete store[key];
        },
        clear() {
            store = {};
        }
    };
})();
Object.defineProperty(window, 'localStorage', { value: localStorageMock });



/* 컴포넌트 렌더링 확인 1 */
test('renders login component', () => {
    render(<Login/>);
    const phoneNumberInput = screen.getByPlaceholderText(/아이디를 입력하세요/);
    const passwordInput = screen.getByPlaceholderText(/비밀번호를 입력하세요/);
    const loginButton = screen.getByText(/로그인/);

    expect(phoneNumberInput).toBeInTheDocument();
    expect(passwordInput).toBeInTheDocument();
    expect(loginButton).toBeInTheDocument();
})

/* 컴포넌트 렌더링 확인 2 */
test('Login 컴포넌트 렌더링 확인', () => {
    render(<Login />);
    screen.debug();
});

/* 사용자 입력을 받을 수 있는지 1 */
test('컴포넌트가 처음 렌더링될 때 각 필드가 비어있는지 확인', () => {
    render(<Login />);
    expect(screen.getByPlaceholderText("id")).toHaveValue('');
    expect(screen.getByPlaceholderText("password")).toHaveValue('');
});

/*사용자 입력을 받을 수 있는지 2 */
test('사용자가 아이디,비밀번호를 입력', async () => {
    render(<Login />);
    screen.debug();

    const phoneNumberInput = await screen.findByPlaceholderText(/id/i);
    const passwordInput = await screen.findByPlaceholderText(/password/i);

    await userEvent.type(phoneNumberInput, '01012345678');
    await userEvent.type(passwordInput, 'user1234');

    await expect (phoneNumberInput).toHaveValue('01012345678');
    await expect (passwordInput).toHaveValue('user1234');
});

test('로그인 버튼을 눌렀을때 동작', async () => {
    render(<Login />);
    const loginButton = await screen.findByText(/로그인/i);
    await userEvent.click(loginButton);
})

describe('Login Component', () => {

    const mock = new MockAdapter(axios);

    const setItemMock = jest.spyOn(Storage.prototype, 'setItem');

    beforeEach(() => {
        // localStorage.clear();
        mock.reset();
        setItemMock.mockClear();
        // 로그인 요청에 대한 모의 응답 설정
        // mock.onPost('/api/login').reply(200, {
        //     token: 'fake-jwt-token'
        // });
    });

    it('로그인 성공 시 로컬 스토리지에 JWT 저장', async () => {
        mock.onPost('/api/login').reply(200, {
            token: 'fake-jwt-token'
        });

        render(<Login />);

        userEvent.type(screen.getByPlaceholderText(/id/i), '01012345678');
        userEvent.type(screen.getByPlaceholderText(/password/i), 'user1234');
        userEvent.click(screen.getByText(/로그인/i));

        await waitFor(() => {
            expect(setItemMock).toHaveBeenCalledWith('jwt', 'fake-jwt-token');
        });
    });
});


// describe('Login Component', () => {
//     beforeEach(()=>{
//         localStorage.clear();
//         mock.reset();
//     });
//
//     test('로그인 성공 시 로컬 스토리지에 JWT 저장', async () => {
//       mock.onPost('/api/login').reply(200, {
//         token: 'fake-jwt-token'
//       });
//         render(<Login />);
//
//         userEvent.type(screen.getByPlaceholderText(/id/i), '01012345678');
//         userEvent.type(screen.getByPlaceholderText(/password/i), 'user1234');
//
//         userEvent.click(screen.getByText(/로그인/i));
//
//         await waitFor(() => {
//             // expect(localStorage.getItem('jwt')).toEqual('fake-jwt-token');
//             expect(localStorage.setItem).toHaveBeenCalledWith('jwt', 'fake-jwt-token');
//         });
//     });
//
// });



