import { useState } from "react";
import axios from "axios";

export default function ForgotPassword() {
    const [email, setEmail] = useState("");
    const [emailError, setEmailError] = useState("");
    const [serverMessage, setServerMessage] = useState("");
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    const handleSubmit = async (e) => {
        e.preventDefault();
        setEmailError("");
        setServerMessage("");

        // 프론트 이메일 형식 검증
        if (!emailRegex.test(email)) {
            setEmailError("올바른 이메일 형식을 입력해주세요.");
            return;
        }

        try {
            const response = await axios.post("/api/auth/forgot-password", { email });

            const msg = response.data?.message || "이메일로 링크를 전송했습니다.";

            // 팝업으로 안내
            alert(`${email} 으로 재설정 링크가 전송되었습니다.`);

            // 상태 초기화
            setEmail("");
            setServerMessage(msg);
        } catch (err) {
            console.error(err);

            const message =
                err.response?.data?.message || "등록되지 않은 이메일입니다.";

            // 서버 오류
            setServerMessage(message);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen px-4">
            <div className="bg-onion-surface w-full max-w-sm sm:max-w-md md:max-w-lg lg:max-w-xl p-8 rounded-2xl shadow-2xl">
                <h2 className="text-2xl font-semibold mb-6 text-center text-[var(--color-onion-text)]">
                    비밀번호 찾기
                </h2>

                <form onSubmit={handleSubmit} className="space-y-4" noValidate>
                    {/* 이메일 입력 */}
                    <div>
                        <label className="block text-sm mb-2 text-[var(--color-onion-text-muted)]">
                            이메일 주소
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
                        {/* 이메일 형식 에러 */}
                        {emailError && (
                            <p className="text-red-400 text-xs mt-1">{emailError}</p>
                        )}
                        {/* 서버 응답 메시지 */}
                        {!emailError && serverMessage && (
                            <p className="text-red-400 text-xs mt-1">{serverMessage}</p>
                        )}
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-onion-primary hover:bg-onion-primary-hover text-white py-3 rounded-lg font-semibold transition-all duration-200"
                    >
                        링크 전송
                    </button>
                </form>
            </div>
        </div>
    );
}
