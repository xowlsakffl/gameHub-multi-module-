import { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";

export default function Signup() {
    const navigate = useNavigate();

    // 입력값
    const [email, setEmail] = useState("");
    const [nickname, setNickname] = useState("");
    const [password, setPassword] = useState("");
    const [passwordConfirm, setPasswordConfirm] = useState("");

    // 에러
    const [emailError, setEmailError] = useState("");
    const [nicknameError, setNicknameError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [passwordConfirmError, setPasswordConfirmError] = useState("");

    // 중복 체크 상태
    const [emailChecked, setEmailChecked] = useState(false);
    const [nicknameChecked, setNicknameChecked] = useState(false);

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    /** 이메일 중복 체크 */
    const handleCheckEmail = async () => {
        setEmailError("");

        if (!emailRegex.test(email)) {
            setEmailError("올바른 이메일을 입력해주세요.");
            return;
        }

        try {
            const res = await axios.get("/api/auth/check-email", { params: { email } });

            if (res.data?.data?.available) {
                setEmailChecked(true);
                setEmailError("");
            } else {
                setEmailChecked(false);
                setEmailError("이미 사용 중인 이메일입니다.");
            }
        } catch (err) {
            setEmailChecked(false);
            setEmailError(
                err.response?.data?.message || "이메일 중복 확인 중 오류 발생"
            );
        }
    };

    /** 닉네임 중복 체크 */
    const handleCheckNickname = async () => {
        setNicknameError("");

        if (nickname.trim().length < 2) {
            setNicknameError("닉네임은 최소 2글자 이상이어야 합니다.");
            return;
        }

        try {
            const res = await axios.get("/api/auth/check-nickname", {
                params: { nickname }
            });

            if (res.data?.data?.available) {
                setNicknameChecked(true);
                setNicknameError("");
            } else {
                setNicknameChecked(false);
                setNicknameError("이미 사용 중인 닉네임입니다.");
            }
        } catch (err) {
            setNicknameChecked(false);
            setNicknameError(
                err.response?.data?.message || "닉네임 중복 확인 중 오류 발생"
            );
        }
    };

    /** 회원가입 요청 */
    const handleSignup = async (e) => {
        e.preventDefault();

        // 기존 에러 초기화
        setEmailError("");
        setNicknameError("");
        setPasswordError("");
        setPasswordConfirmError("");

        // 프론트 유효성 검사
        if (!emailRegex.test(email)) {
            setEmailError("올바른 이메일 형식을 입력해주세요.");
            return;
        }
        if (!emailChecked) {
            setEmailError("이메일 중복 확인을 해주세요.");
            return;
        }
        if (!nicknameChecked) {
            setNicknameError("닉네임 중복 확인을 해주세요.");
            return;
        }
        if (nickname.trim().length < 2) {
            setNicknameError("닉네임은 최소 2글자 이상이어야 합니다.");
            return;
        }
        if (password.length < 6) {
            setPasswordError("비밀번호는 6자리 이상이어야 합니다.");
            return;
        }
        if (password !== passwordConfirm) {
            setPasswordConfirmError("비밀번호가 일치하지 않습니다.");
            return;
        }

        try {
            await axios.post("/api/auth/signup", {
                email,
                password,
                nickname,
            });

            alert("회원가입 완료! 로그인 페이지로 이동합니다.");
            navigate("/login");

        } catch (err) {
            const message = err.response?.data?.message || "회원가입 실패";

            if (message.includes("이메일")) setEmailError(message);
            else if (message.includes("닉네임")) setNicknameError(message);
            else setPasswordError(message);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen px-4">
            <div className="bg-onion-surface w-full max-w-sm sm:max-w-md md:max-w-lg lg:max-w-xl p-8 rounded-2xl shadow-2xl">
                <h2 className="text-3xl font-semibold mb-8 text-center text-[var(--color-onion-text)]">
                    GAMEHUB 회원가입
                </h2>

                <form onSubmit={handleSignup} className="space-y-5" noValidate>

                    {/* 이메일 */}
                    <div>
                        <label className="block mb-2 text-[var(--color-onion-text-muted)]">
                            이메일
                        </label>
                        <div className="flex gap-2">
                            <input
                                type="text"
                                value={email}
                                onChange={(e) => {
                                    setEmail(e.target.value);
                                    setEmailChecked(false);
                                }}
                                className={`flex-1 p-3 rounded-lg bg-onion-input text-[var(--color-onion-text)] border ${
                                    emailError
                                        ? "border-red-500 focus:ring-red-500"
                                        : "border-onion-border focus:ring-onion-primary"
                                } focus:outline-none focus:ring-2`}
                                placeholder="example@email.com"
                            />
                            <button
                                type="button"
                                onClick={handleCheckEmail}
                                className="px-4 py-2 bg-onion-primary text-white rounded-lg hover:bg-onion-primary-hover"
                            >
                                중복 체크
                            </button>
                        </div>
                        {emailError && <p className="text-red-400 text-xs mt-1">{emailError}</p>}
                        {emailChecked && <p className="text-green-400 text-xs mt-1">✓ 사용 가능한 이메일입니다</p>}
                    </div>

                    {/* 닉네임 */}
                    <div>
                        <label className="block mb-2 text-[var(--color-onion-text-muted)]">
                            닉네임
                        </label>
                        <div className="flex gap-2">
                            <input
                                type="text"
                                value={nickname}
                                onChange={(e) => {
                                    setNickname(e.target.value);
                                    setNicknameChecked(false);
                                }}
                                className={`flex-1 p-3 rounded-lg bg-onion-input text-[var(--color-onion-text)] border ${
                                    nicknameError
                                        ? "border-red-500 focus:ring-red-500"
                                        : "border-onion-border focus:ring-onion-primary"
                                } focus:outline-none focus:ring-2`}
                                placeholder="닉네임 입력"
                            />
                            <button
                                type="button"
                                onClick={handleCheckNickname}
                                className="px-4 py-2 bg-onion-primary text-white rounded-lg hover:bg-onion-primary-hover"
                            >
                                중복 체크
                            </button>
                        </div>
                        {nicknameError && <p className="text-red-400 text-xs mt-1">{nicknameError}</p>}
                        {nicknameChecked && <p className="text-green-400 text-xs mt-1">✓ 사용 가능한 닉네임입니다</p>}
                    </div>

                    {/* 비밀번호 */}
                    <div>
                        <label className="block mb-2 text-[var(--color-onion-text-muted)]">
                            비밀번호
                        </label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className={`w-full p-3 rounded-lg bg-onion-input text-[var(--color-onion-text)] border ${
                                passwordError
                                    ? "border-red-500 focus:ring-red-500"
                                    : "border-onion-border focus:ring-onion-primary"
                            } focus:outline-none focus:ring-2`}
                            placeholder="6자리 이상"
                        />
                        {passwordError && <p className="text-red-400 text-xs mt-1">{passwordError}</p>}
                    </div>

                    {/* 비밀번호 확인 */}
                    <div>
                        <label className="block mb-2 text-[var(--color-onion-text-muted)]">
                            비밀번호 확인
                        </label>
                        <input
                            type="password"
                            value={passwordConfirm}
                            onChange={(e) => setPasswordConfirm(e.target.value)}
                            className={`w-full p-3 rounded-lg bg-onion-input text-[var(--color-onion-text)] border ${
                                passwordConfirmError
                                    ? "border-red-500 focus:ring-red-500"
                                    : "border-onion-border focus:ring-onion-primary"
                            } focus:outline-none focus:ring-2`}
                            placeholder="비밀번호 재입력"
                        />
                        {passwordConfirmError && <p className="text-red-400 text-xs mt-1">{passwordConfirmError}</p>}
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-onion-primary hover:bg-onion-primary-hover text-white py-3 rounded-lg font-semibold transition-all"
                    >
                        회원가입
                    </button>
                </form>

                <p className="text-sm text-center text-[var(--color-onion-text-muted)] mt-6">
                    이미 계정이 있으신가요?{" "}
                    <Link to="/login" className="text-onion-primary hover:underline">
                        로그인
                    </Link>
                </p>
            </div>
        </div>
    );
}
