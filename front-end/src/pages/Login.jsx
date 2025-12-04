import {useEffect, useState} from "react";
import axios from "axios";
import {Link, useNavigate} from "react-router-dom";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [remember, setRemember] = useState(false);

    // 개별 에러 상태
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");

    const navigate = useNavigate();

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    useEffect(() => {
        const token = localStorage.getItem("token") || sessionStorage.getItem("token");
        if (token) {
            navigate("/home", { replace: true });
        }
    }, [navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 기존 에러 초기화
        setEmailError("");
        setPasswordError("");

        // 이메일 형식 프론트에서 체크
        if (!emailRegex.test(email)) {
            setEmailError("올바른 이메일 형식을 입력해주세요.");
            return;
        }

        try {
            const response = await axios.post("/api/auth/login", { email, password });
            const token = response.data?.data?.accessToken;

            if (token) {
                if (remember) localStorage.setItem("token", token);
                else sessionStorage.setItem("token", token);
                navigate("/home");
            } else {
                setPasswordError("서버 응답에 토큰이 없습니다.");
            }
        } catch (err) {
            console.error("로그인 실패:", err);

            // 백엔드에서 내려준 message 가져오기
            const message =
                err.response?.data?.message ||
                "유효하지 않은 이메일 또는 비밀번호입니다.";

            // 로그인 실패 시 두 필드 모두 강조
            setEmailError(message);
            setPasswordError(message);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen px-4">
            <div className="bg-onion-surface w-full max-w-sm sm:max-w-md md:max-w-lg lg:max-w-xl p-8 rounded-2xl shadow-2xl">
                <h2 className="text-3xl font-semibold mb-8 text-center text-[var(--color-onion-text)]">
                    GAMEHUB 로그인
                </h2>

                <form onSubmit={handleSubmit} className="space-y-5" noValidate>
                    {/* 이메일 */}
                    <div>
                        <label className="block text-sm sm:text-base mb-2 text-[var(--color-onion-text-muted)]">
                            이메일
                        </label>
                        <input
                            type="text"
                            placeholder="example@email.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className={`w-full p-3 rounded-lg bg-onion-input text-[var(--color-onion-text)] border ${
                                emailError
                                    ? "border-red-500 focus:ring-red-500"
                                    : "border-onion-border focus:ring-onion-primary"
                            } focus:outline-none focus:ring-2`}
                            required
                        />
                        {emailError && (
                            <p className="text-red-400 text-xs mt-1">{emailError}</p>
                        )}
                    </div>

                    {/* 비밀번호 */}
                    <div>
                        <label className="block text-sm sm:text-base mb-2 text-[var(--color-onion-text-muted)]">
                            비밀번호
                        </label>
                        <input
                            type="password"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className={`w-full p-3 rounded-lg bg-onion-input text-[var(--color-onion-text)] border ${
                                passwordError
                                    ? "border-red-500 focus:ring-red-500"
                                    : "border-onion-border focus:ring-onion-primary"
                            } focus:outline-none focus:ring-2`}
                            required
                        />
                        {passwordError && (
                            <p className="text-red-400 text-xs mt-1">{passwordError}</p>
                        )}
                    </div>

                    {/* 로그인 상태 유지 */}
                    <div className="flex items-center justify-between text-sm text-[var(--color-onion-text-muted)]">
                        <label className="flex items-center gap-2 cursor-pointer select-none">
                            <input
                                type="checkbox"
                                checked={remember}
                                onChange={(e) => setRemember(e.target.checked)}
                                className="w-4 h-4 accent-[var(--color-onion-primary)]"
                            />
                            <span>로그인 상태 유지</span>
                        </label>

                        <Link
                            to="/forgot-password"
                            className="text-onion-primary hover:underline"
                        >
                            비밀번호 찾기
                        </Link>
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-onion-primary hover:bg-onion-primary-hover hover:cursor-pointer text-white py-3 rounded-lg font-semibold transition-all duration-200 text-base"
                    >
                        로그인
                    </button>
                </form>

                <p className="text-sm text-center text-[var(--color-onion-text-muted)] mt-6">
                    계정이 없나요?{" "}
                    <Link
                        to="/signup"
                        className="text-onion-primary hover:underline"
                    >
                        회원가입
                    </Link>
                </p>
            </div>
        </div>
    );
}
