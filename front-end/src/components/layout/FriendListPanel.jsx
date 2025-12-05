import { useState, useEffect } from "react";
import api from "../../api/axios.js";
import {
    FiPlus,
    FiX,
    FiCheck,
    FiChevronDown,
    FiChevronRight,
} from "react-icons/fi";

export default function FriendListPanel({ open, setOpen }) {
    const [incomingRequests, setIncomingRequests] = useState([]);
    const [friends, setFriends] = useState([]);

    const [showRequests, setShowRequests] = useState(true);
    const [showFriends, setShowFriends] = useState(true);

    const [showSearchModal, setShowSearchModal] = useState(false);
    const [searchKeyword, setSearchKeyword] = useState("");

    /** ===============================
     * 받은 친구 요청 목록
     * =============================== */
    const loadIncomingRequests = async () => {
        try {
            const res = await api.get("/friends/requests/received");
            setIncomingRequests(res.data?.data || []);
        } catch (err) {
            console.error(err);
        }
    };

    /** ===============================
     * 친구 목록
     * =============================== */
    const loadFriends = async () => {
        try {
            const res = await api.get("/friends");
            setFriends(res.data?.data || []);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        if (open) {
            loadIncomingRequests();
            loadFriends();
        }
    }, [open]);

    /** ===============================
     * 친구 요청 보내기
     * =============================== */
    const sendFriendRequest = async () => {
        if (!searchKeyword.trim()) return;

        try {
            await api.post("/friends/request", {
                toNickname: searchKeyword.trim(),
            });

            alert("친구 요청을 보냈습니다.");
            closeModal();
        } catch (err) {
            alert(err.response?.data?.message || "요청 실패");
        }
    };

    /** 요청 수락 */
    const acceptRequest = async (id) => {
        try {
            await api.post(`/friends/request/${id}/accept`);
            loadIncomingRequests();
            loadFriends();
        } catch (err) {
            console.error("수락 실패:", err);
        }
    };

    /** 요청 거절 */
    const rejectRequest = async (id) => {
        try {
            await api.post(`/friends/request/${id}/reject`);
            loadIncomingRequests();
        } catch (err) {
            console.error("거절 실패:", err);
        }
    };

    const closeModal = () => {
        setShowSearchModal(false);
        setSearchKeyword("");
    };

    return (
        <>
            {/* 패널 */}
            <div
                className={`
                    fixed right-20 top-16
                    h-[calc(100%-64px)] w-72
                    bg-onion-surface border-l border-onion-border shadow-xl
                    p-4 transform transition-transform duration-300
                    z-20
                    ${open ? "translate-x-0" : "translate-x-full"}
                `}
            >
                <div className="flex items-center justify-between mb-6">
                    <h2 className="text-lg font-semibold text-white">친구</h2>

                    <div className="flex items-center gap-4">
                        <button onClick={() => setShowSearchModal(true)}>
                            <FiPlus size={20} className="text-gray-300 hover:text-white" />
                        </button>

                        <button onClick={() => setOpen(false)}>
                            <FiX size={22} className="text-gray-400 hover:text-white" />
                        </button>
                    </div>
                </div>

                {/* 받은 요청 */}
                <div className="mb-5">
                    <button
                        onClick={() => setShowRequests(!showRequests)}
                        className="flex items-center justify-between w-full text-gray-200 mb-4"
                    >
                        <span className="font-medium">받은 친구 요청</span>
                        {showRequests ? <FiChevronDown /> : <FiChevronRight />}
                    </button>

                    <div
                        className={`overflow-hidden transition-all ${
                            showRequests ? "max-h-60" : "max-h-0"
                        }`}
                    >
                        <div className="space-y-4">
                            {incomingRequests.length === 0 && (
                                <p className="text-gray-500 text-sm">요청 없음</p>
                            )}

                            {incomingRequests.map((req) => (
                                <div
                                    key={req.requestId}
                                    className="flex items-center justify-between text-gray-300"
                                >
                                    <span>{req.fromUser.nickname}</span>

                                    <div className="flex gap-4">
                                        <button
                                            className="hover:text-green-400"
                                            onClick={() => acceptRequest(req.requestId)}
                                        >
                                            <FiCheck size={18} />
                                        </button>
                                        <button
                                            className="hover:text-red-400"
                                            onClick={() => rejectRequest(req.requestId)}
                                        >
                                            <FiX size={18} />
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                <div className="border-t border-gray-700/50 my-4" />

                {/* 친구 목록 */}
                <div>
                    <button
                        onClick={() => setShowFriends(!showFriends)}
                        className="flex items-center justify-between w-full text-gray-200 mb-4"
                    >
                        <span className="font-medium">친구 목록</span>
                        {showFriends ? <FiChevronDown /> : <FiChevronRight />}
                    </button>

                    <div
                        className={`overflow-hidden transition-all ${
                            showFriends ? "max-h-[400px]" : "max-h-0"
                        }`}
                    >
                        <div className="space-y-4">
                            {friends.map((f, i) => (
                                <div
                                    key={i}
                                    className="flex items-center justify-between text-gray-300"
                                >
                                    <div className="flex items-center gap-3">
                                        <div className="w-2.5 h-2.5 rounded-full bg-gray-500" />

                                        <span>{f.friend.nickname}</span>
                                    </div>

                                    <button className="text-xs text-onion-primary hover:underline">
                                        DM
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>

            {/* 모달 */}
            {showSearchModal && (
                <div
                    className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-40"
                    onClick={closeModal}
                >
                    <div
                        onClick={(e) => e.stopPropagation()}
                        className="bg-onion-surface p-6 rounded-xl w-80 border border-onion-border shadow-xl"
                    >
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-lg font-semibold text-white">친구 추가</h3>

                            <button onClick={closeModal}>
                                <FiX size={22} className="text-gray-400 hover:text-white" />
                            </button>
                        </div>

                        <input
                            type="text"
                            value={searchKeyword}
                            onChange={(e) => setSearchKeyword(e.target.value)}
                            className="w-full p-2 rounded bg-onion-input text-white border border-onion-border mb-4"
                            placeholder="닉네임 검색"
                        />

                        <div className="flex justify-end gap-2">
                            <button
                                onClick={closeModal}
                                className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
                            >
                                취소
                            </button>

                            <button
                                onClick={sendFriendRequest}
                                className="px-4 py-2 bg-onion-primary text-white rounded hover:bg-onion-primary-hover"
                            >
                                검색
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}
