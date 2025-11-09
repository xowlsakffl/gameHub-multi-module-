import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [remember, setRemember] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            // AuthController 기준 경로 수정
            const response = await axios.post("/api/auth/login", { email, password });

            // ApiResponse<TokenResponse> 구조 맞추기
            const token = response.data?.data?.accessToken;

            if (token) {
                if (remember) {
                    localStorage.setItem("token", token);
                } else {
                    sessionStorage.setItem("token", token);
                }

                navigate("/home");
            } else {
                setError("서버 응답에 토큰이 없습니다.");
            }
        } catch (err) {
            console.error("로그인 실패:", err);
            setError("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen px-4">
            <div className="bg-onion-surface w-full max-w-sm sm:max-w-md md:max-w-lg lg:max-w-xl p-8 rounded-2xl shadow-2xl">
                <h2 className="text-3xl font-semibold mb-8 text-center text-[var(--color-onion-text)]">
                    Onion 로그인
                </h2>

                <form onSubmit={handleSubmit} className="space-y-5">
                    {/* 이메일 */}
                    <div>
                        <label className="block text-sm sm:text-base mb-2 text-[var(--color-onion-text-muted)]">
                            이메일
                        </label>
                        <input
                            type="email"
                            placeholder="example@email.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full p-3 rounded-lg bg-onion-input text-[var(--color-onion-text)] border border-onion-border focus:outline-none focus:ring-2 focus:ring-onion-primary"
                            required
                        />
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
                            className="w-full p-3 rounded-lg bg-onion-input text-[var(--color-onion-text)] border border-onion-border focus:outline-none focus:ring-2 focus:ring-onion-primary"
                            required
                        />
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

                        <a href="#" className="text-onion-primary hover:underline">
                            비밀번호 찾기
                        </a>
                    </div>

                    {error && (
                        <p className="text-red-400 text-sm text-center">{error}</p>
                    )}

                    <button
                        type="submit"
                        className="w-full bg-onion-primary hover:bg-onion-primary-hover text-white py-3 rounded-lg font-semibold transition-all duration-200 text-base"
                    >
                        로그인
                    </button>
                </form>

                <p className="text-sm text-center text-[var(--color-onion-text-muted)] mt-6">
                    계정이 없나요?{" "}
                    <a href="#" className="text-onion-primary hover:underline">
                        회원가입
                    </a>
                </p>
            </div>
        </div>
    );
}
