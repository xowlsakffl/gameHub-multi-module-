import { useState } from "react";
import { Tooltip } from "react-tooltip";
import { FaPlus, FaUserFriends, FaComments, FaHome } from "react-icons/fa";

export default function LeftSidebar() {
    const [active, setActive] = useState("home");

    const menuItems = [
        { id: "home", icon: <FaHome />, label: "홈" },
        { id: "myParty", icon: <FaUserFriends />, label: "내 파티" },
        { id: "joined", icon: <FaComments />, label: "참여중 파티" },
    ];

    return (
        <div className="flex flex-col items-center bg-onion-surface border-r border-onion-border w-20 py-4 space-y-4">

            {/* 메뉴 아이콘 목록 */}
            <div className="flex flex-col items-center space-y-3 flex-1">
                {menuItems.map((item) => (
                    <button
                        key={item.id}
                        data-tooltip-id={`tip-${item.id}`}
                        data-tooltip-content={item.label}
                        className={`w-12 h-12 flex items-center justify-center rounded-xl text-xl transition-all duration-200
              ${
                            active === item.id
                                ? "bg-onion-primary text-white"
                                : "text-gray-400 hover:text-white hover:bg-onion-input"
                        }`}
                        onClick={() => setActive(item.id)}
                    >
                        {item.icon}
                        <Tooltip id={`tip-${item.id}`} place="right" />
                    </button>
                ))}
            </div>

            {/* 파티 추가 버튼 */}
            <button
                data-tooltip-id="tip-add"
                data-tooltip-content="새 파티 만들기"
                className="w-12 h-12 flex items-center justify-center rounded-xl text-2xl bg-onion-input hover:bg-onion-primary hover:text-white transition-all duration-200"
            >
                <FaPlus />
                <Tooltip id="tip-add" place="right" />
            </button>
        </div>
    );
}
