import { FaBell, FaSearch, FaUserCircle } from "react-icons/fa";

export default function Header() {
    return (
        <div className="h-16 bg-onion-surface flex items-center justify-between px-6 border-b border-onion-border">
            {/* 왼쪽: 로고 + 검색창 */}
            <div className="flex items-center gap-4 w-2/3">
                <h1 className="text-xl font-bold text-white">🧅 Onion</h1>
                <div className="flex items-center bg-onion-input rounded-lg px-3 py-2 flex-1">
                    <FaSearch className="text-gray-400 mr-2" />
                    <input
                        type="text"
                        placeholder="파티 검색..."
                        className="bg-transparent w-full outline-none text-gray-200"
                    />
                </div>
            </div>

            {/* 오른쪽: 알림 + 프로필 */}
            <div className="flex items-center gap-6">
                <FaBell className="text-gray-400 hover:text-white cursor-pointer text-xl" />
                <FaUserCircle className="text-gray-400 hover:text-white cursor-pointer text-2xl" />
            </div>
        </div>
    );
}
